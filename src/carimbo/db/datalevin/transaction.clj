(ns carimbo.db.datalevin.transaction
  (:require [carimbo.models.transaction :as models.transaction]
            [carimbo.adapters.transaction :as adapters.transaction]
            [datalevin.core :as d]
            [schema.core :as s]))

(s/defn insert-with-account-balance-upsert! :- models.transaction/Transaction
  [{:transaction/keys [customer-id] :as transaction} :- models.transaction/Transaction
   balance-before :- BigInteger
   balance-after :- BigInteger
   db-connection]
  (d/transact! db-connection [[:db.fn/cas [:customer/id customer-id] :customer/balance balance-before balance-after]
                              (adapters.transaction/internal->database transaction)])
  transaction)