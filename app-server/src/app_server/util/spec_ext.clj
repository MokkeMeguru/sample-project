(ns app-server.util.spec-ext
  (:require
   [clojure.spec.alpha :as s]))

(defn ->validate!
  "validate value as the spec
  if the validation is failed, raise clojure.lang.ExceptionInfo instead of return false"
  ([spec value]
   (->validate! spec value "invalid spec"))
  ([spec value message]
   (or (s/valid? spec value)
       (throw
        (ex-info message
                 {:explain (s/explain-data spec value)})))))
