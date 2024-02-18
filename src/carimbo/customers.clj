(ns carimbo.customers
  (:require [carimbo.db.postgresql.customer :as database.customer]
            [com.stuartsierra.component :as component]
            [carimbo.controllers.customer :as controllers.customer]))

(defrecord Customers [config postgresql]
  component/Lifecycle
  (start [component]
    (let [{:keys [customers]} (-> config :config)
          db-connection (:postgresql postgresql)]

      (doseq [customer customers
              :when (nil? (database.customer/lookup (:customer/id customer) db-connection))]
        (controllers.customer/create! customer db-connection))

      component))

  (stop [component]
    component))

(defn new-customers []
  (->Customers {} {}))