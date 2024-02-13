(ns carimbo.datahike
  (:require [datahike.api :as d]
            [datahike-jdbc.core]
            [com.stuartsierra.component :as component]
            [taoensso.timbre :as log])
  (:import (clojure.lang ExceptionInfo)
           (org.testcontainers.containers GenericContainer PostgreSQLContainer)))

(defrecord Datahike [config schema]
  component/Lifecycle
  (start [component]
    (let [{{:keys [host port username password]} :datahike
           service-name                          :service-name} (:config config)
          db-connection-config {:store {:backend  :jdbc
                                        :dbtype   "postgresql"
                                        :host     host
                                        :port     port
                                        :user     username
                                        :password password
                                        :dbname   service-name}}
          _ (try (d/create-database db-connection-config)
                 (catch ExceptionInfo e
                   (if (= (:type (ex-data e) :db-already-exists))
                     (log/info :db-already-exists)
                     (throw e))))
          db-connection (d/connect db-connection-config)]

      (d/transact db-connection schema)

      (merge component {:datahike db-connection})))

  (stop [component]
    (assoc component :config nil)))

(defn new-datahike [schema]
  (map->Datahike {:schema schema}))

(defrecord MockDatahike [config schema]
  component/Lifecycle
  (start [component]
    (let [postgresql-container (doto (PostgreSQLContainer. "postgres:alpine")
                                 .start)
          db-connection-config {:store {:backend  :jdbc
                                        :dbtype   "postgresql"
                                        :host     (.getHost postgresql-container)
                                        :port     (.getMappedPort postgresql-container PostgreSQLContainer/POSTGRESQL_PORT)
                                        :user     (.getUsername postgresql-container)
                                        :password (.getPassword postgresql-container)
                                        :dbname   (.getDatabaseName postgresql-container)}}
          _ (try (d/create-database db-connection-config)
                 (catch ExceptionInfo e
                   (if (= (:type (ex-data e) :db-already-exists))
                     (log/info :db-already-exists)
                     (throw e))))
          db-connection (d/connect db-connection-config)]

      (d/transact db-connection schema)

      (merge component {:datahike             db-connection
                        :postgresql-container postgresql-container})))

  (stop [component]
    (.stop ^GenericContainer (:postgresql-container component))))

(defn new-mock-datahike [schema]
  (map->MockDatahike {:schema schema}))
