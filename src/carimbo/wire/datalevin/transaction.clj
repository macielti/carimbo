(ns carimbo.wire.datalevin.transaction
  (:require [schema.core :as s]
            [carimbo.models.transaction :as models.transaction])
  (:import (java.util Date)))

(def transaction-skeleton
  {:transaction/customer-id  {:db/valueType :db.type/long
                              :db/doc       "Transaction customer id"}
   :transaction/amount       {:db/valueType :db.type/bigint
                              :db/doc       "Transaction amount"}
   :transaction/type         {:db/valueType :db.type/keyword
                              :db/doc       "Transaction type (:credit, :debit)"}
   :transaction/description  {:db/valueType :db.type/string
                              :db/doc       "Transaction description"}
   :transaction/requested-at {:db/valueType :db.type/instant
                              :db/doc       "Moment when the Transaction was requested"}})

(s/defschema Transaction
  (assoc models.transaction/transaction-skeleton
    :transaction/requested-at Date))