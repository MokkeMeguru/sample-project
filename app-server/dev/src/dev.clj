(ns dev
  (:require
   [clojure.tools.namespace.repl :refer [refresh]]
   [eftest.runner   :refer [find-tests run-tests]]
   [malli.clj-kondo :as mc]
   [malli.dev]
   [ns-tracker.core :refer [ns-tracker]]
   [orchestra.spec.test :as st]))

(clojure.tools.namespace.repl/set-refresh-dirs "dev/src" "src" "test")

(def modified-namespaces
  (ns-tracker ["src/clj" "resources" "test"]))

(defn emit-malli!
  ([]
   (emit-malli! {:all-ns false}))
  ([{:keys [all-ns]}]
   (if all-ns
     (do
       (refresh)
       (mc/emit!))
     (mc/emit!))
   :emit-malli))

(defn reload []
  (let [updated-ns (modified-namespaces)]
    (doseq [ns-sym updated-ns]
      (require ns-sym :reload))
    (when updated-ns (run-tests (find-tests updated-ns)))
    (emit-malli!)
    (malli.dev/start!)
    (st/instrument))
  :reloaded)

(comment
  (reload))
