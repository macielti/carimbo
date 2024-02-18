(ns carimbo.models.customer
  (:require [schema.core :as s]))

(def customer-skeleton
  {:customer/id      s/Int
   :customer/limit   s/Int
   :customer/balance s/Int})

(s/defschema Customer
  customer-skeleton)