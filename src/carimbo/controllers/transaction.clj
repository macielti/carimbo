(ns carimbo.controllers.transaction
  (:require [carimbo.db.datahike.customer :as database.customer]
            [carimbo.models.customer :as models.customer]
            [carimbo.models.transaction :as models.transaction]
            [schema.core :as s]
            [carimbo.db.datahike.transaction :as database.transaction]))

(s/defn create-transaction! :- models.customer/Customer
  [{:transaction/keys [customer-id] :as transaction} :- models.transaction/Transaction
   db-connection]
  (->> (database.transaction/insert-with-account-balance-upsert! transaction db-connection)
       :db-after
       (database.customer/lookup! customer-id)))