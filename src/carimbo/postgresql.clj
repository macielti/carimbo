(ns carimbo.postgresql
  (:require [com.stuartsierra.component :as component]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [schema.core :as s]
            [jdbc.pool.c3p0 :as pool])
  (:import (org.testcontainers.containers GenericContainer PostgreSQLContainer)))

(defn get-connection
  [database-uri]
  (-> (jdbc/get-connection {:jdbcUrl database-uri})
      (jdbc/with-options {:builder-fn rs/as-unqualified-maps})))

(defrecord PostgreSQL [config]
  component/Lifecycle
  (start [component]
    (let [{{:keys [username password host port database]} :postgresql} (:config config)
          db-connection (pool/make-datasource-spec
                          {:classname         "org.postgresql.Driver"
                           :subprotocol       "postgresql"
                           :user              username
                           :password          password
                           :subname           (str "//" host ":" port "/" database)
                           :initial-pool-size 3
                           :max-pool-size     14})
          schema-sql (slurp "resources/schema.sql")]

      (jdbc/execute! db-connection [schema-sql])

      (assoc component :postgresql db-connection)))

  (stop [component]
    (assoc component :postgresql nil)))

(defn new-postgresql []
  (->PostgreSQL {}))

(s/defn postgresql-for-unit-tests
  [schema-sql-path :- s/Str]
  (let [postgresql-container (doto (PostgreSQLContainer. "postgres:15-alpine")
                               .start)
        connection (-> (jdbc/get-connection {:dbtype  "postgresql"
                                             :jdbcUrl (str (.getJdbcUrl postgresql-container) "&user=test&password=test")})
                       (jdbc/with-options {:builder-fn rs/as-unqualified-maps}))]

    (jdbc/execute! connection [(slurp schema-sql-path)])

    {:database-connection  connection
     :postgresql-container postgresql-container}))

(defrecord MockPostgreSQL [config]
  component/Lifecycle
  (start [component]
    (let [postgresql-container (doto (PostgreSQLContainer. "postgres:15-alpine")
                                 .start)
          postgresql-uri (str (.getJdbcUrl postgresql-container)
                              "&user=test&password=test")
          schema-sql (slurp "resources/schema.sql")]

      (jdbc/execute! (get-connection postgresql-uri) [schema-sql])

      (assoc component :postgresql postgresql-uri
                       :postgresql-container postgresql-container)))

  (stop [component]
    (.stop ^GenericContainer (:postgresql-container component))
    (assoc component :postgresql nil)))

(defn new-mock-postgresql []
  (->MockPostgreSQL {}))
