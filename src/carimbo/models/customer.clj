(ns carimbo.models.customer
  (:require [schema.core :as s]))

(def customer-skeleton
  {:customer/id      s/Int
   :customer/limit   BigInteger
   :customer/balance BigInteger})

(s/defschema Customer
  customer-skeleton)