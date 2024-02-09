(ns carimbo.customers
  (:require [com.stuartsierra.component :as component]
            [carimbo.controllers.customer :as controllers.customer]))

(defrecord Customers [config datalevin]
  component/Lifecycle
  (start [component]
    (let [{:keys [customers]} (-> config :config)
          db-connection (-> datalevin :datalevin)]

      (doseq [{:customer/keys [limit balance] :as customer} customers
              :let [customer' (assoc customer :customer/limit (biginteger limit)
                                              :customer/balance (biginteger balance))]]
        (controllers.customer/create! customer' db-connection))

      component))

  (stop [component]
    component))

(defn new-customers []
  (->Customers {} {}))