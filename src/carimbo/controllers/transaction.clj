(ns carimbo.controllers.transaction
  (:require [carimbo.db.postgresql.customer :as database.customer]
            [carimbo.db.postgresql.transaction :as database.transaction]
            [carimbo.error :as error]
            [carimbo.models.customer :as models.customer]
            [carimbo.models.transaction :as models.transaction]
            [next.jdbc :as jdbc]
            [schema.core :as s]))

(s/defn create-transaction! :- models.customer/Customer
  [{:transaction/keys [customer-id] :as transaction} :- models.transaction/Transaction
   db-connection]
  (jdbc/with-transaction [tx db-connection]
    (let [update-balance-result (-> (database.customer/lookup! customer-id tx)
                                    (database.customer/update-balance! transaction tx))]
      (when (not= (:next.jdbc/update-count (first update-balance-result)) 1)
        (error/http-friendly-exception 422
                                       "inconsistent-balance"
                                       "Balance debit over limit"
                                       "Customer is trying to spend above the limit")))
    (database.transaction/insert! transaction tx)
    (database.customer/lookup! customer-id tx)))