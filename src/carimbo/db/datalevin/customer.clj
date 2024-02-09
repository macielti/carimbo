(ns carimbo.db.datalevin.customer
  (:require [datalevin.core :as d]
            [schema.core :as s]
            [carimbo.models.customer :as models.customer]))

(s/defn insert! :- models.customer/Customer
  [customer :- models.customer/Customer
   db-connection]
  (d/transact! db-connection [customer])
  customer)
