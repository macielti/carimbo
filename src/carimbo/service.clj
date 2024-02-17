(ns carimbo.service
  (:require [com.stuartsierra.component :as component]
            [carimbo.interceptors :as interceptors]
            [io.pedestal.http :as http]
            [plumbing.core :as plumbing]))

(defrecord Service [routes config datomic datalevin postgresql rabbitmq-producer producer http-client prometheus rate-limiter telegram-producer datahike]
  component/Lifecycle
  (start [component]
    (let [{{{:keys [host port]} :service} :config} config
          service-map {::http/routes          (:routes routes)
                       ::http/allowed-origins (constantly true)
                       ::http/host            host
                       ::http/port            port
                       ::http/type            :jetty
                       ::http/join?           false}
          components (plumbing/assoc-when {:config (:config config)}
                                          :datahike (:datahike datahike)
                                          :producer (:producer producer)
                                          :rabbitmq-producer (:rabbitmq-producer rabbitmq-producer)
                                          :datomic (:datomic datomic)
                                          :datalevin (:datalevin datalevin)
                                          :postgresql (:postgresql postgresql)
                                          :http-client (:http-client http-client)
                                          :prometheus (:prometheus prometheus)
                                          :rate-limiter (:rate-limiter rate-limiter)
                                          :telegram-producer (:telegram-producer telegram-producer))]
      (assoc component :service (http/start (-> service-map
                                                http/default-interceptors
                                                (update ::http/interceptors concat (interceptors/common-interceptors
                                                                                     components))
                                                http/create-server)))))
  (stop [component]
    (http/stop (:service component))
    (assoc component :service nil)))

(defn new-service []
  (->Service {} {} {} {} {} {} {} {} {} {} {} {}))