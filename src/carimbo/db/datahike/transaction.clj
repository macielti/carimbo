(ns carimbo.db.datahike.transaction
  (:require [carimbo.adapters.transaction :as adapters.transaction]
            [carimbo.db.datahike.customer :as database.customer]
            [carimbo.error :as error]
            [schema.core :as s]
            [datahike.api :as d]
            [carimbo.models.transaction :as models.transaction]))

(s/defn insert! :- models.transaction/Transaction
  [transaction :- models.transaction/Transaction
   db-connection]
  (d/transact db-connection [(adapters.transaction/internal->database transaction)])
  transaction)

(s/defn by-customer :- [models.transaction/Transaction]
  [customer-id :- s/Int
   database]
  (->> (d/q '[:find (pull ?transaction [*])
              :in $ ?customer-id
              :where [?transaction :transaction/customer-id ?customer-id]] database customer-id)
       (mapv #(-> (first %)
                  (dissoc :db/id)
                  adapters.transaction/database->internal))))

(defn update-customer-balance [db incoming-transaction]
  (let [{:transaction/keys [customer-id type amount]} incoming-transaction
        {:customer/keys [limit balance]} (database.customer/lookup! customer-id db)
        new-balance (case type
                      :debit (- balance amount)
                      :credit (+ balance amount))]

    (when (and (< new-balance 0) (= type :debit) (> (abs new-balance) limit))
      (error/http-friendly-exception 422
                                     "inconsistent-balance"
                                     "Balance debit over limit"
                                     "Customer is trying to spend above the limit"))

    [{:customer/id      customer-id
      :customer/balance new-balance}]))

(s/defn insert-with-account-balance-upsert! :- models.transaction/Transaction
  [transaction :- models.transaction/Transaction
   db-connection]
  (try
    (d/transact db-connection [[:db.fn/call update-customer-balance (adapters.transaction/internal->database transaction)]
                               (adapters.transaction/internal->database transaction)])
    (catch Exception _e
      (error/http-friendly-exception 422
                                     "inconsistent-balance"
                                     "Balance debit over limit"
                                     "Customer is trying to spend above the limit"))))