(ns carimbo.controllers.customer
  (:require [carimbo.db.datahike.customer :as database.customer]
            [carimbo.models.customer :as models.customer]
            [schema.core :as s]))

(s/defn create! :- models.customer/Customer
  [{:customer/keys [id] :as customer} :- models.customer/Customer
   db-connection]
  (when-not (database.customer/lookup id @db-connection)
    (database.customer/insert! customer db-connection)))
