(ns app-server.infrastructure.server
  (:require
   [clojure.tools.reader]
   [com.walmartlabs.lacinia.pedestal2 :as lp2]
   [io.pedestal.http :as http]
   [app-server.domain.graphql.schema]
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [io.pedestal.http.jetty]
   [app-server.util.log :refer [log]]
   [app-server.interface.gateway.memory.board-game])
  (:import [org.eclipse.jetty.server.handler StatisticsHandler]))

;; gke のデフォルトは 30秒
;; https://cloud.google.com/blog/products/containers-kubernetes/kubernetes-best-practices-terminating-with-grace
(def graceful-msec 20000)

(defn conf
  [server]
  (let [stats-handler (StatisticsHandler.)
        default-handler (.getHandler server)]
    (.setHandler stats-handler default-handler)
    (.setHandler server stats-handler)
    (.setStopTimeout  server graceful-msec)
    (.setStopAtShutdown server true)))

(defonce runner (atom nil))

(defrecord Server [graphql-resolver])

;; 多分間違っている (実装上 pedestal で jetty の configurator は書き換えるのが困難)
(defn wrap-conf [x]
  (doto x (fn [x] (conf (::http/server x)))))

(defn start-server! [^app_server.infrastructure.server.Server server]
  (let [runner (-> (:graphql-resolver server)
                   (lp2/default-service nil)
                   (assoc ::http/host "0.0.0.0")
                   (assoc ::http/port 8888)
                   http/create-server
                   wrap-conf
                   http/start)]
    runner))

(defn stop-server! [^app_server.infrastructure.server.Server server]
  (http/stop server)
  nil)

(defn start!
  ([]
   (start! (->Server (app-server.domain.graphql.schema/load-schema!
                      (app-server.interface.gateway.memory.board-game/->GraphqlResovler
                       (reduce #(assoc %1 (:id %2) %2) {} (-> "data.edn" io/resource slurp edn/read-string :games)))))))
  ([^app_server.infrastructure.server.Server server]
   (app-server.util.log/init)
   (reset! runner (start-server! server))))

(defn stop!
  []
  (try
    (stop-server! @runner)
    (catch Exception e
      (log :info "server error" {} e)))
  (reset! runner nil))

;; (start!)
;; (stop!)
