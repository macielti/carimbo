(ns carimbo.wire.datahike.transaction)

(def transaction-skeleton
  [{:db/ident       :transaction/customer-id
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc         "Transaction customer id"}
   {:db/ident       :transaction/amount
    :db/valueType   :db.type/bigint
    :db/cardinality :db.cardinality/one
    :db/doc         "Transaction amount"}
   {:db/ident       :transaction/type
    :db/valueType   :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc         "Transaction type (:credit, :debit)"}
   {:db/ident       :transaction/description
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc         "Transaction description"}
   {:db/ident       :transaction/requested-at
    :db/valueType   :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc         "Moment when the Transaction was requested"}])