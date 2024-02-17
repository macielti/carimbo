(ns carimbo.wire.datahike.customer)

(def customer-skeleton
  [{:db/ident       :customer/id
    :db/valueType   :db.type/long
    :db/unique      :db.unique/identity
    :db/cardinality :db.cardinality/one
    :db/doc         "Customer id"}
   {:db/ident       :customer/limit
    :db/valueType   :db.type/bigint
    :db/cardinality :db.cardinality/one
    :db/doc         "Credit limit"}
   {:db/ident       :customer/balance
    :db/valueType   :db.type/bigint
    :db/cardinality :db.cardinality/one
    :db/doc         "Account balance"}])