(ns fixtures.customer)

(def customer-id 1)
(def customer-limit 100000)
(def customer-balance 0)

(def customer
  {:customer/id      customer-id
   :customer/limit   customer-limit
   :customer/balance customer-balance})

(def customer-database
  {:customer/customer_id      customer-id
   :customer/customer_balance customer-balance
   :customer/customer_limit   customer-limit})
