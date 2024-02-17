(ns carimbo.controllers.statement
  (:require [carimbo.db.postgresql.customer :as database.customer]
            [carimbo.db.postgresql.transaction :as database.transaction]
            [carimbo.models.statement :as models.statement]
            [java-time.api :as jt]
            [schema.core :as s]
            [carimbo.logic.statement :as logic.statement]))

(s/defn fetch-statement :- models.statement/Statement
  [customer-id :- s/Int
   db-connection]
  (let [customer (database.customer/lookup! customer-id db-connection)
        recent-transactions (database.transaction/recent-by-customer customer-id db-connection)]
    (logic.statement/->statement customer recent-transactions (jt/local-date-time (jt/zone-id "UTC")))))