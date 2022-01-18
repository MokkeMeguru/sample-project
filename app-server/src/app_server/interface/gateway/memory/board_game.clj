(ns app-server.interface.gateway.memory.board-game
  (:require [app-server.domain.graphql.schema :as schema]))

(defrecord GraphqlResovler [memory-table]
  schema/IGraphqlResovler
  (schema/get-by-id [this context args value]
    (let [{:keys [id]} args]
      (get (:memory-table this) id))))
