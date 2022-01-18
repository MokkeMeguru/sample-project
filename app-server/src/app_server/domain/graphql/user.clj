(ns app-server.domain.graphql.user
  (:require
   [app-server.domain.graphql.schema :as schema]
   [com.walmartlabs.lacinia :as lacinia]
   [clojure.java.io :as io]
   [clojure.edn :as edn]))

;;  (defn query!
;;   [schema query-string]
;;   (lacinia/execute schema query-string nil nil))

;; (comment
;;   (def memory-db
;;     (app-server.interface.gateway.momory.board-game/->GraphqlResovler
;;      (->> "data.edn" io/resource slurp edn/read-string
;;           :games
;;           (reduce #(assoc %1 (:id %2) %2) {}))))

;;   (query! (schema/load-schema! memory-db)
;;           "{ game_by_id(id: \"1236\") { id name summary }}"))
