(ns carimbo.controllers.customer
  (:require [carimbo.models.customer :as models.customer]
            [schema.core :as s]
            [carimbo.db.datalevin.customer :as database.customer]))

(s/defn create! :- models.customer/Customer
  [customer :- models.customer/Customer
   db-connection]
  (database.customer/insert! customer db-connection))
