(ns carimbo.db.postgresql.customer
  (:require [carimbo.error :as error]
            [carimbo.models.customer :as models.customer]
            [carimbo.models.transaction :as models.transaction]
            [next.jdbc :as jdbc]
            [fixtures.customer]
            [schema.core :as s])
  (:import (clojure.lang ExceptionInfo)))

;TODO: Check if the jdbc can transact and return the entity
(s/defn insert! :- models.customer/Customer
  [{:customer/keys [id limit balance] :as customer} :- models.customer/Customer
   db-connection]
  (jdbc/execute! db-connection ["INSERT INTO customer (customer_id, customer_limit, customer_balance) VALUES (?, ?, ?)"
                                id limit balance])
  customer)

(s/defn lookup! :- models.customer/Customer
  [customer-id :- s/Int
   db-connection]
  (let [{:customer/keys [customer_id customer_balance customer_limit] :as customer} (jdbc/execute-one! db-connection ["SELECT customer_id, customer_limit, customer_balance FROM customer WHERE customer_id = ?"
                                                                                                                      customer-id])]
    (if customer
      {:customer/id      (or customer_id (:customer_id customer))
       :customer/balance (biginteger (or customer_balance (:customer_balance customer)))
       :customer/limit   (biginteger (or customer_limit (:customer_limit customer)))}
      (error/http-friendly-exception 404
                                     "customer-not-found"
                                     "Customer not found"
                                     "Customer not exist on database"))))

(s/defn lookup :- (s/maybe models.customer/Customer)
  [customer-id :- s/Int
   db-connection]
  (let [{:keys [customer_id customer_balance customer_limit] :as customer} (jdbc/execute-one! db-connection ["SELECT customer_id, customer_limit, customer_balance FROM customer WHERE customer_id = ?"
                                                                                                             customer-id])]
    (when customer
      {:customer/id      customer_id
       :customer/balance (biginteger customer_balance)
       :customer/limit   (biginteger customer_limit)})))

(s/defn update-balance!
  [{:customer/keys [balance id]} :- models.customer/Customer
   {:transaction/keys [type amount]} :- models.transaction/Transaction
   db-connection]
  (if (= type :debit)
    (jdbc/execute! db-connection ["UPDATE customer
                                   SET customer_balance = CASE
                                                            WHEN (customer_balance - ?) > -customer_limit THEN (customer_balance - ?)
                                                          END
                                   WHERE customer_id = ? AND customer_balance = ?"
                                  amount amount id balance])
    (jdbc/execute! db-connection ["UPDATE customer
                                   SET customer_balance = (customer_balance + ?)
                                   WHERE customer_id = ? AND customer_balance = ?"
                                  amount id balance])))