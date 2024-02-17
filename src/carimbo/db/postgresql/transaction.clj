(ns carimbo.db.postgresql.transaction
  (:require [carimbo.adapters.transaction :as adapters.transaction]
            [carimbo.models.transaction :as models.transaction]
            [next.jdbc :as jdbc]
            [schema.core :as s]))

(s/defn by-customer :- [models.transaction/Transaction]
  [customer-id :- s/Int
   db-connection]
  (->> (jdbc/execute! db-connection ["SELECT customer_id, amount, type, description, requested_at FROM transaction WHERE customer_id = ?"
                                     customer-id])
       (map adapters.transaction/database->internal)))

(s/defn recent-by-customer :- [models.transaction/Transaction]
  [customer-id :- s/Int
   db-connection]
  (->> (jdbc/execute! db-connection ["SELECT customer_id, amount, type, description, requested_at FROM transaction
                                      WHERE customer_id = ?
                                      ORDER BY requested_at desc limit 10"
                                     customer-id])
       (map adapters.transaction/database->internal)))

(s/defn insert!
  [{:transaction/keys [customer-id amount type description requested-at]} :- models.transaction/Transaction
   db-connection]
  (jdbc/execute! db-connection ["INSERT INTO transaction (customer_id, amount, type, description, requested_at) VALUES (?, ?, ?, ?, ?)"
                                customer-id amount (adapters.transaction/type->wire type) description requested-at]))