(ns carimbo.wire.datalevin.customer)

(def customer-skeleton
  {:customer/id      {:db/valueType :db.type/long
                      :db/unique    :db.unique/identity
                      :db/doc       "Customer id"}
   :customer/limit   {:db/valueType :db.type/bigint
                      :db/doc       "Credit limit"}
   :customer/balance {:db/valueType :db.type/bigint
                      :db/doc       "Account balance"}})