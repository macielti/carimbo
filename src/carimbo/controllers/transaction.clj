(ns carimbo.controllers.transaction
  (:require [carimbo.db.postgresql.customer :as database.customer]
            [carimbo.db.postgresql.transaction :as database.transaction]
            [carimbo.models.customer :as models.customer]
            [carimbo.models.transaction :as models.transaction]
            [common-clj.error.core :as error]
            [next.jdbc :as jdbc]
            [schema.core :as s])
  (:import (org.postgresql.util PSQLException)))

(s/defn create-transaction! :- models.customer/Customer
  [{:transaction/keys [customer-id amount type] :as transaction} :- models.transaction/Transaction
   db-connection]
  (database.customer/lookup! customer-id db-connection)
  (jdbc/with-transaction [tx db-connection]
    (let [amount' (case type
                    :credit (+ amount)
                    :debit (- amount))]
      (jdbc/execute! tx ["select pg_advisory_xact_lock(?)" customer-id])
      (try (database.customer/update-balance! customer-id (biginteger amount') tx)
           (catch PSQLException _
             (error/http-friendly-exception 422
                                            "inconsistent-balance"
                                            "Balance debit over limit"
                                            "Customer is trying to spend above the limit")))
      (database.transaction/insert! transaction tx)
      (database.customer/lookup! customer-id tx))))