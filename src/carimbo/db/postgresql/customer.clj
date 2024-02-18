(ns carimbo.db.postgresql.customer
  (:require [common-clj.error.core :as error]
            [carimbo.models.customer :as models.customer]
            [next.jdbc :as jdbc]
            [schema.core :as s]))

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
  (let [{:customer/keys [customer_id customer_balance customer_limit] :as customer} (jdbc/execute-one! db-connection ["SELECT customer_id, customer_limit, customer_balance FROM customer WHERE customer_id = ?"
                                                                                                                      customer-id])]
    (when customer
      {:customer/id      customer_id
       :customer/balance (biginteger customer_balance)
       :customer/limit   (biginteger customer_limit)})))

(s/defn update-balance!
  [customer-id :- s/Int
   amount :- BigInteger
   db-connection]
  (jdbc/execute! db-connection ["UPDATE customer
                                   SET customer_balance = (customer_balance + ?)
                                   WHERE customer_id = ?"
                                amount customer-id]))