(ns carimbo.components
  (:require [com.stuartsierra.component :as component]
            [carimbo.config :as config]
            [carimbo.service :as service]
            [carimbo.customers :as customers]
            [carimbo.diplomat.http-server :as diplomat.http-server]
            [carimbo.routes :as routes]
            [carimbo.datahike :as datahike]
            [carimbo.db.datahike.config :as database.config]
            [schema.core :as s])
  (:gen-class))

(s/defn get-component-content
  [component :- s/Keyword
   system]
  (get-in system [component component]))

(def system
  (component/system-map
    :config (config/new-config "resources/config.edn" :prod :edn)
    :datahike (component/using (datahike/new-datahike database.config/schemas) [:config])
    :customers (component/using (customers/new-customers) [:config :datahike])
    :routes (routes/new-routes diplomat.http-server/routes)
    :service (component/using (service/new-service) [:routes :datahike :config])))

(defn start-system! []
  (component/start system))

(def system-test
  (component/system-map
    :config (config/new-config "resources/config.example.edn" :test :edn)
    :datahike (component/using (datahike/new-mock-datahike database.config/schemas) [:config])
    :customers (component/using (customers/new-customers) [:config :datahike])
    :routes (routes/new-routes diplomat.http-server/routes)
    :service (component/using (service/new-service) [:routes :datahike :config])))

(def -main start-system!)