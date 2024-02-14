(ns carimbo.datahike
  (:require [datahike.http.client :as client]
            [datahike-jdbc.core]
            [com.stuartsierra.component :as component]
            [taoensso.timbre :as log])
  (:import (clojure.lang ExceptionInfo)
           (org.testcontainers.containers GenericContainer PostgreSQLContainer)))

(defrecord Datahike [config schema]
  component/Lifecycle
  (start [component]
    (let [{{:keys [uri token]} :datahike
           service-name        :service-name} (:config config)
          db-connection-config {:store              {:backend :mem
                                                     :id      service-name}
                                :keep-history?      false
                                :schema-flexibility :read
                                :remote-peer        {:backend :datahike-server
                                                     :url     uri
                                                     :token   token}}
          _ (try (client/create-database db-connection-config)
                 (catch ExceptionInfo e
                   (if (= (:type (ex-data e) :db-already-exists))
                     (log/info :db-already-exists)
                     (throw e))))
          db-connection (client/connect db-connection-config)]

      (client/transact db-connection schema)

      (merge component {:datahike db-connection})))

  (stop [component]
    (assoc component :config nil)))

(defn new-datahike [schema]
  (map->Datahike {:schema schema}))

(defrecord MockDatahike [config schema]
  component/Lifecycle
  (start [component]
    (let [{{:keys [uri token]} :datahike
           service-name        :service-name} (:config config)
          postgresql-container (doto (PostgreSQLContainer. "postgres:alpine")
                                 .start)
          db-connection-config {:store              {:backend :mem
                                                     :id      service-name}
                                :keep-history?      false
                                :schema-flexibility :read
                                :remote-peer        {:backend :datahike-server
                                                     :url     uri
                                                     :token   token}}
          _ (try (client/create-database db-connection-config)
                 (catch ExceptionInfo e
                   (if (= (:type (ex-data e) :db-already-exists))
                     (log/info :db-already-exists)
                     (throw e))))
          db-connection (client/connect db-connection-config)]

      (client/transact db-connection schema)

      (merge component {:datahike             db-connection
                        :postgresql-container postgresql-container})))

  (stop [component]
    (.stop ^GenericContainer (:postgresql-container component))))

(defn new-mock-datahike [schema]
  (map->MockDatahike {:schema schema}))
