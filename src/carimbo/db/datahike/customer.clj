(ns carimbo.db.datahike.customer
  (:require [datahike.http.client :as client]
            [carimbo.error :as error]
            [carimbo.models.customer :as models.customer]
            [schema.core :as s]))

(s/defn insert! :- models.customer/Customer
  [customer :- models.customer/Customer
   db-connection]
  (client/transact db-connection [customer])
  customer)

(s/defn lookup! :- models.customer/Customer
  [customer-id :- s/Int
   database]
  (let [customer (-> (client/q '[:find (pull ?customer [*])
                            :in $ ?customer-id
                            :where [?customer :customer/id ?customer-id]] database customer-id)
                     ffirst
                     (dissoc :db/id))]
    (when-not customer
      (error/http-friendly-exception 404
                                     "customer-not-found"
                                     "Customer not found"
                                     "Customer not exist on database"))
    customer))

(s/defn lookup :- (s/maybe models.customer/Customer)
  [customer-id :- s/Int
   database]
  (-> (client/q '[:find (pull ?customer [*])
             :in $ ?customer-id
             :where [?customer :customer/id ?customer-id]] database customer-id)
      ffirst
      (dissoc :db/id)))