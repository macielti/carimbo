(ns carimbo.controllers.statement
  (:require [carimbo.db.datalevin.customer :as database.customer]
            [carimbo.db.datalevin.transaction :as database.transaction]
            [carimbo.logic.transaction :as logic.transaction]
            [carimbo.models.statement :as models.statement]
            [datalevin.core :as d]
            [java-time.api :as jt]
            [schema.core :as s]
            [carimbo.logic.statement :as logic.statement]))

(s/defn fetch-statement :- models.statement/Statement
  [customer-id :- s/Int
   db-connection]
  (let [as-of (jt/local-date-time (jt/zone-id "UTC"))
        database (d/db db-connection)
        customer (database.customer/lookup! customer-id database)
        recent-transactions (->> (database.transaction/by-customer customer-id database)
                                 (filter #(jt/before? (:transaction/requested-at %) as-of))
                                 logic.transaction/recent-transactions)]
    (logic.statement/->statement customer recent-transactions as-of)))