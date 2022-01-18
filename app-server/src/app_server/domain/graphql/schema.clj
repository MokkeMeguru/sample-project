(ns app-server.domain.graphql.schema
  (:require
   [clojure.java.io :as io]
   [com.walmartlabs.lacinia.util :as util]
   [com.walmartlabs.lacinia.schema :as schema]
   [clojure.edn :as edn]))

(defprotocol IGraphqlResovler
  (get-by-id [this context args value]))

(defn resolve-map
  [record]
  {:query/game-by-id (partial get-by-id record)})

(defn load-schema!
  ([^app_server.domain.graphql.schema.IGraphqlResovler record]
   (load-schema! (-> "schema.edn" io/resource slurp edn/read-string) record))
  ([source ^app_server.domain.graphql.schema.IGraphqlResovler record]
   (-> source (util/attach-resolvers (resolve-map record)) schema/compile)))

(comment
  (load-schema! (app-server.interface.gateway.momory.board-game/->GraphqlResovler {})))
