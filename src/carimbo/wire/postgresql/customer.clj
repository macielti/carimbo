(ns carimbo.wire.postgresql.customer
  (:require [schema.core :as s]))

(s/defschema Customer
  {:customer/customer_id      s/Int
   :customer/customer_balance s/Int
   :customer/customer_limit   s/Int})