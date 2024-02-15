(ns carimbo.controllers.transaction
  (:require [carimbo.db.postgresql.customer :as database.customer]
            [carimbo.db.postgresql.transaction :as database.transaction]
            [carimbo.models.customer :as models.customer]
            [carimbo.models.transaction :as models.transaction]
            [next.jdbc :as jdbc]
            [schema.core :as s]))

(s/defn create-transaction! :- models.customer/Customer
  [{:transaction/keys [customer-id] :as transaction} :- models.transaction/Transaction
   db-connection]
  (jdbc/with-transaction [tx db-connection]
    (database.customer/lookup! customer-id tx)
    (database.customer/update-balance! customer-id transaction tx)
    (database.transaction/insert! transaction tx)
    (database.customer/lookup! customer-id tx)))