(ns app-server.main
  (:gen-class)
  (:require [app-server.infrastructure.server]))

(defn -main [& args]
  (app-server.infrastructure.server/start!))
