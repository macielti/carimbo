(ns carimbo.db.postgresql.customer
  (:require [carimbo.adapters.customer :as adapters.customer]
            [common-clj.error.core :as error]
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
  (let [customer (jdbc/execute-one! db-connection ["SELECT customer_id, customer_limit, customer_balance FROM customer WHERE customer_id = ?"
                                                   customer-id])]
    (if customer
      (adapters.customer/database->internal customer)
      (error/http-friendly-exception 404
                                     "customer-not-found"
                                     "Customer not found"
                                     "Customer not exist on database"))))

(s/defn lookup :- (s/maybe models.customer/Customer)
  [customer-id :- s/Int
   db-connection]
  (some-> (jdbc/execute-one! db-connection ["SELECT customer_id, customer_limit, customer_balance
                                                    FROM customer
                                                    WHERE customer_id = ?"
                                            customer-id])
          adapters.customer/database->internal))

(s/defn update-balance!
  [customer-id :- s/Int
   amount :- s/Int
   db-connection]
  (jdbc/execute! db-connection ["UPDATE customer
                                   SET customer_balance = (customer_balance + ?)
                                   WHERE customer_id = ?"
                                amount customer-id]))