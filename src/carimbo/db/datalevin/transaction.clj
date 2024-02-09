(ns carimbo.db.datalevin.transaction
  (:require [carimbo.models.transaction :as models.transaction]
            [carimbo.adapters.transaction :as adapters.transaction]
            [datalevin.core :as d]
            [schema.core :as s]))

(s/defn by-customer :- [models.transaction/Transaction]
  [customer-id :- s/Int
   database]
  (->> (d/q '[:find (pull ?transaction [*])
              :in $ ?customer-id
              :where [?transaction :transaction/customer-id ?customer-id]] database customer-id)
       (mapv #(-> (first %)
                  (dissoc :db/id)
                  adapters.transaction/database->internal))))

(s/defn insert-with-account-balance-upsert! :- models.transaction/Transaction
  [{:transaction/keys [customer-id] :as transaction} :- models.transaction/Transaction
   current-balance :- BigInteger
   balance-after :- BigInteger
   db-connection]
  (d/transact! db-connection [[:db/cas [:customer/id customer-id] :customer/balance current-balance balance-after]
                              (adapters.transaction/internal->database transaction)])
  transaction)