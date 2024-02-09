(ns carimbo.controllers.transaction
  (:require [carimbo.db.datalevin.customer :as database.customer]
            [carimbo.models.customer :as models.customer]
            [carimbo.models.transaction :as models.transaction]
            [common-clj.error.core :as common-error]
            [datalevin.core :as d]
            [schema.core :as s]
            [carimbo.db.datalevin.transaction :as database.transaction]))

(s/defn create-transaction! :- models.customer/Customer
  [{:transaction/keys [amount customer-id type] :as transaction} :- models.transaction/Transaction
   db-connection]
  (d/with-transaction [conn db-connection]
    (let [{current-balance :customer/balance
           limit           :customer/limit :as customer} (database.customer/lookup! customer-id (d/db conn))
          balance-after (case type
                          :credit (+ current-balance amount)
                          :debit (- current-balance amount))]
      (when (and (= type :debit) (> (abs balance-after) limit))
        (common-error/http-friendly-exception 422
                                              "inconsistent-balance"
                                              "Balance debit over limit"
                                              "Customer is trying to spend above the limit"))
      (database.transaction/insert-with-account-balance-upsert! transaction current-balance (biginteger balance-after) conn)
      (assoc customer :customer/balance balance-after))))