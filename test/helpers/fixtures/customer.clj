(ns fixtures.customer)

(def customer-id 1)

(def customer
  {:customer/id      1
   :customer/limit   (biginteger 100000)
   :customer/balance (biginteger 0)})
