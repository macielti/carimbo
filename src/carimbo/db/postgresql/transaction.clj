(ns carimbo.db.postgresql.transaction
  (:require [carimbo.adapters.transaction :as adapters.transaction]
            [carimbo.models.transaction :as models.transaction]
            [java-time.api :as jt]
            [next.jdbc :as jdbc]
            [schema.core :as s]))

(s/defn by-customer :- [models.transaction/Transaction]
  [customer-id :- s/Int
   db-connection]
  (->> (jdbc/execute! db-connection ["SELECT customer_id, amount, type, description, requested_at FROM transaction WHERE customer_id = ?"
                                     customer-id])
       (map (fn [{:keys [customer_id amount type description requested_at]}]
              {:transaction/customer-id  customer_id
               :transaction/amount       (biginteger amount)
               :transaction/type         (adapters.transaction/wire-type->internal type)
               :transaction/description  description
               :transaction/requested-at (jt/local-date-time requested_at (jt/zone-id "UTC"))}))))

(s/defn recent-by-customer :- [models.transaction/Transaction]
  [customer-id :- s/Int
   db-connection]
  (->> (jdbc/execute! db-connection ["SELECT customer_id, amount, type, description, requested_at FROM transaction
                                      WHERE customer_id = ?
                                      ORDER BY requested_at desc limit 10"
                                     customer-id])
       (map (fn [{:transaction/keys [customer_id amount type description requested_at]}]
              {:transaction/customer-id  customer_id
               :transaction/amount       (biginteger amount)
               :transaction/type         (adapters.transaction/wire-type->internal type)
               :transaction/description  description
               :transaction/requested-at (jt/local-date-time requested_at (jt/zone-id "UTC"))}))))

(s/defn insert!
  [{:transaction/keys [customer-id amount type description requested-at]} :- models.transaction/Transaction
   db-connection]
  (jdbc/execute! db-connection ["INSERT INTO transaction (customer_id, amount, type, description, requested_at) VALUES (?, ?, ?, ?, ?)"
                                customer-id amount (adapters.transaction/type->wire type) description requested-at]))