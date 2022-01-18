;; code is highly borrowed from https://github.com/rakutenrewards/timbre-json-output-fn
;; if you want to use ring http server, please see https://github.com/nberger/ring-logger
(ns app-server.util.log
  (:require
   [cheshire.core :refer [generate-string]]
   [clojure.edn]
   [clojure.spec.alpha :as s]
   [clojure.stacktrace]
   [taoensso.timbre :as timbre]))

(defn timbre-level->google-str [timbre-level]
  (case timbre-level
    :trace "DEFAULT"
    :debug "DEBUG"
    :info "INFO"
    :warn "WARNING"
    :error "ERROR"
    :fatal "CRITICAL"
    :report "NOTICE"))

;; spec ;;;;;;
(def timbre-level? #{:trace :debug :info :warn :error :fatal :report})
(s/def ::timbre-level timbre-level?)
(s/def ::error (fn [inst] (instance? Error inst)))

;; impl ;;;;;;
(defmacro log
  ([level message payload err]
   (let [level (if (s/conform ::timbre-level level) level :report)
         message (if (s/conform string? message) message (str message))
         payload (if (s/conform map? payload) payload {:body payload})
         err (when (and (some? err) (s/conform ::error err)) err)]
     `(timbre/log ~level ~err ~message ~payload)))
  ([level message payload]
   `(log ~level ~message ~payload nil)))

(defn- stacktrace-text
  "clojure err -> stacktrace text"
  [err]
  (timbre/stacktrace err {:stacktrace-fonts {}}))

(defn- format-error
  "clojure err -> error map contains stacktrace text"
  [^java.lang.Exception err]
  (let [exception-map (Throwable->map err)]
    (cond->
     {:stack (stacktrace-text err)
      :message (.getMessage err)
      :kind (-> exception-map :via first :type)
      ;; :map exception-map
      }
      (instance? clojure.lang.ExceptionInfo err) (assoc :more (ex-data err)))))

(defn ->jsonPayload [vargs]
  (cond
    (zero? (count vargs)) {}
    (= 1 (count vargs)) {:message (first vargs)}
    (string? (first vargs)) (->jsonPayload (vec (cons nil vargs)))
    :else (cond-> {}
            (<= 2 (count vargs)) (assoc :message (second vargs))
            (and (<= 3 (count vargs)) (map? (nth vargs 2))) (merge (nth vargs 2)))))

(defn- output-fn [data]
  (let [{:keys [level ?err vargs ?file instant ?line]} data
        payload (->jsonPayload vargs)]
    (generate-string
     (cond->
      {:severity (timbre-level->google-str level)
       :message (get payload :message "no named")
       :caller (cond-> ?file ?line (str ":" ?line))
       :timestamp instant}
       payload (merge (dissoc payload :message))
       ?err (assoc :error (format-error ?err))))))

(defn init []
  (timbre/merge-config!
   {:output-fn output-fn}))
