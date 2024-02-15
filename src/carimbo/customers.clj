(ns carimbo.customers
  (:require [com.stuartsierra.component :as component]
            [carimbo.controllers.customer :as controllers.customer]
            [carimbo.postgresql :as postgresql]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(defrecord Customers [config postgresql]
  component/Lifecycle
  (start [component]
    (let [{:keys [customers]} (-> config :config)
          db-connection (postgresql/get-connection (:postgresql postgresql))]

      (doseq [{:customer/keys [limit balance] :as customer} customers
              :let [customer' (assoc customer :customer/limit (biginteger limit)
                                              :customer/balance (biginteger balance))]]
        (controllers.customer/create! customer' db-connection))

      component))

  (stop [component]
    component))

(defn new-customers []
  (->Customers {} {}))