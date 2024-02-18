(ns carimbo.components
  (:require [com.stuartsierra.component :as component]
            [carimbo.customers :as customers]
            [carimbo.diplomat.http-server :as diplomat.http-server]
            [common-clj.component.service :as component.service]
            [common-clj.component.postgresql :as component.postgresql]
            [common-clj.component.routes :as component.routes]
            [common-clj.component.config :as component.config]
            [schema.core :as s])
  (:gen-class))

(s/defn get-component-content
  [component :- s/Keyword
   system]
  (get-in system [component component]))

(def system
  (component/system-map
    :config (component.config/new-config "resources/config.edn" :prod :edn)
    :postgresql (component/using (component.postgresql/new-postgresql) [:config])
    :customers (component/using (customers/new-customers) [:config :postgresql])
    :routes (component.routes/new-routes diplomat.http-server/routes)
    :service (component/using (component.service/new-service) [:routes :postgresql :config])))

(defn start-system! []
  (component/start system))

(def system-test
  (component/system-map
    :config (component.config/new-config "resources/config.edn" :test :edn)
    :postgresql (component/using (component.postgresql/new-mock-postgresql) [:config])
    :customers (component/using (customers/new-customers) [:config :postgresql])
    :routes (component.routes/new-routes diplomat.http-server/routes)
    :service (component/using (component.service/new-service) [:routes :postgresql :config])))

(def -main start-system!)