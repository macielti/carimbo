(ns carimbo.components
  (:require [com.stuartsierra.component :as component]
            [carimbo.config :as config]
            [carimbo.service :as service]
            [carimbo.customers :as customers]
            [carimbo.diplomat.http-server :as diplomat.http-server]
            [carimbo.routes :as routes]
            [carimbo.postgresql :as postgresql]
            [schema.core :as s])
  (:gen-class))

(s/defn get-component-content
  [component :- s/Keyword
   system]
  (get-in system [component component]))

(def system
  (component/system-map
    :config (config/new-config "resources/config.edn" :prod :edn)
    :postgresql (component/using (postgresql/new-postgresql) [:config])
    :customers (component/using (customers/new-customers) [:config :postgresql])
    :routes (routes/new-routes diplomat.http-server/routes)
    :service (component/using (service/new-service) [:routes :postgresql :config])))

(defn start-system! []
  (component/start system))

(def system-test
  (component/system-map
    :config (config/new-config "resources/config.edn" :test :edn)
    :postgresql (component/using (postgresql/new-mock-postgresql) [:config])
    :customers (component/using (customers/new-customers) [:config :postgresql])
    :routes (routes/new-routes diplomat.http-server/routes)
    :service (component/using (service/new-service) [:routes :postgresql :config])))

(def -main start-system!)