(ns carimbo.controllers.statement
  (:require [carimbo.db.datalevin.customer :as database.customer]
            [carimbo.db.datalevin.transaction :as database.transaction]
            [carimbo.logic.transaction :as logic.transaction]
            [carimbo.models.statement :as models.statement]
            [datalevin.core :as d]
            [diehard.core :as dh]
            [java-time.api :as jt]
            [schema.core :as s]
            [carimbo.logic.statement :as logic.statement]))

(s/defn fetch-statement :- models.statement/Statement
  [customer-id :- s/Int
   db-connection]
  (let [as-of (jt/local-date-time (jt/zone-id "UTC"))
        database (dh/with-retry {:retry-on    [NullPointerException ClassCastException]
                                 :max-retries 3}
                   (d/db db-connection))
        customer (dh/with-retry {:retry-on    NullPointerException
                                 :max-retries 3}
                   (database.customer/lookup! customer-id database))
        recent-transactions (->> (dh/with-retry {:retry-on    NullPointerException
                                                 :max-retries 3}
                                   (database.transaction/by-customer customer-id database))
                                 (filter #(jt/before? (:transaction/requested-at %) as-of))
                                 logic.transaction/recent-transactions)]
    (logic.statement/->statement customer recent-transactions as-of)))