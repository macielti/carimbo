(ns carimbo.customers
  (:require [carimbo.db.datalevin.customer :as database.customer]
            [com.stuartsierra.component :as component]
            [carimbo.controllers.customer :as controllers.customer]))

(defrecord Customers [config datalevin]
  component/Lifecycle
  (start [component]
    (let [{:keys [customers]} (-> config :config)
          db-connection (-> datalevin :datalevin)]

      (doseq [{:customer/keys [id limit balance] :as customer} customers
              :let [customer' (assoc customer :customer/limit (biginteger limit)
                                              :customer/balance (biginteger balance))]
              :when (nil? (database.customer/lookup id @db-connection))]
        (controllers.customer/create! customer' db-connection))

      component))

  (stop [component]
    component))

(defn new-customers []
  (->Customers {} {}))