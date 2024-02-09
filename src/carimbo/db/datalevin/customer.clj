(ns carimbo.db.datalevin.customer
  (:require [datalevin.core :as d]
            [schema.core :as s]
            [carimbo.models.customer :as models.customer]))

(s/defn insert! :- models.customer/Customer
  [customer :- models.customer/Customer
   db-connection]
  (d/transact! db-connection [customer])
  customer)

(s/defn lookup :- (s/maybe models.customer/Customer)
  [customer-id :- s/Int
   database]
  (-> (d/q '[:find (pull ?customer [*])
             :in $ ?customer-id
             :where [?customer :customer/id ?customer-id]] database customer-id)
      ffirst
      (dissoc :db/id)))

