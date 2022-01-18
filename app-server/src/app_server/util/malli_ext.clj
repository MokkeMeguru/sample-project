(ns app-server.util.malli-ext
  (:require
   [clojure.pprint]
   [clojure.string]
   [app-server.util.error]
   [malli.core :as m]
   [malli.error :as me]))

;; spec ;;;;;;
(def =>->validate!
  [:function {:registry {:schema [:fn m/schema?]}}
   [:=> [:cat :schema :any] :boolean]
   [:=> [:cat :schema :any :string] :boolean]])

;; impl ;;;;;;
(defn doc
  "function document with malli's schema"
  [schema & docs]
  (str
   (clojure.string/join "\n  " docs)
   "\n\n"
   (with-out-str (clojure.pprint/pprint
                  [:function (vec (rest schema))]))))

(defmacro mdefn
  "define malli speced function"
  [name {:keys [schema docs] :as attr-map} & body]
  (let [schema (if (symbol? schema) (eval schema) schema)
        doc-string (apply doc schema
                          (cond (sequential? docs) docs
                                (string? docs) [docs]
                                :else []))
        attr-map (merge attr-map {:malli/schema schema
                                  :doc doc-string})]
    `(defn ~name
       ~attr-map
       ~@body)))

(defmacro mdefn-
  "define malli speced private function"
  [name {:keys [schema docs] :as attr-map} & body]
  (let [schema (if (symbol? schema) (eval schema) schema)
        doc-string (apply doc schema
                          (cond (sequential? docs) docs
                                (string? docs) [docs]
                                :else []))
        attr-map (merge attr-map {:malli/schema schema
                                  :doc doc-string})]
    `(defn- ~name
       ~attr-map
       ~@body)))

(mdefn ->validate!
  {:schema =>->validate!
   :docs ["validate value as the spec"
          "if validation is failed, raise clojure.lang.ExceptionInfo intead of return false"]}
  ([spec value]
   (->validate! spec value "invalid spec"))
  ([spec value message]
   (or (m/validate spec value)
       (throw
        (ex-info message
                 {:explain (me/humanize (m/explain spec value)
                                        {:wrap #(select-keys % [:value :message])})})))))
