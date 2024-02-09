(ns carimbo.components
  (:require [com.stuartsierra.component :as component]
            [common-clj.component.config :as component.config]
            [common-clj.component.service :as component.service]
            [common-clj.component.datalevin :as component.datalevin]
            [carimbo.db.datalevin.config :as database.config]
            [carimbo.customers :as customers])
  (:gen-class))

(def system
  (component/system-map
    :config (component.config/new-config "resources/config.edn" :prod :edn)
    :datalevin (component/using (component.datalevin/new-datalevin database.config/schema) [:config])
    :customers (component/using (customers/new-customers) [:config :datalevin])
    #_:routes #_(component/using (component.routes/new-routes diplomat.http-server/routes) [:config])
    #_:service #_(component/using (component.service/new-service) [:routes :config])))

(defn start-system! []
  (component/start system))

(def system-test
  (component/system-map
    :config (component.config/new-config "resources/config.example.edn" :test :edn)
    :datalevin (component/using (component.datalevin/new-datalevin database.config/schema) [:config])
    :customers (component/using (customers/new-customers) [:config :datalevin])
    #_:routes #_(component/using (component.routes/new-routes diplomat.http-server/routes) [:config])
    #_:service #_(component/using (component.service/new-service) [:routes :config])))

(def -main start-system!)