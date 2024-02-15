(ns carimbo.db.datalevin.customer
  (:require [common-clj.error.core :as common-error]
            [datalevin.core :as d]
            [schema.core :as s]
            [carimbo.models.customer :as models.customer]))

(s/defn insert! :- models.customer/Customer
  [customer :- models.customer/Customer
   db-connection]
  (d/transact! db-connection [customer])
  customer)

(s/defn lookup! :- models.customer/Customer
  [customer-id :- s/Int
   database]
  (let [customer (-> (d/q '[:find (pull ?customer [*])
                            :in $ ?customer-id
                            :where [?customer :customer/id ?customer-id]] database customer-id)
                     ffirst
                     (dissoc :db/id))]
    (when-not customer
      (common-error/http-friendly-exception 404
                                            "customer-not-found"
                                            "Customer not found"
                                            "Customer not exist on database"))
    customer))

(s/defn lookup :- (s/maybe models.customer/Customer)
  [customer-id :- s/Int
   database]
  (some-> (d/q '[:find (pull ?customer [*])
                 :in $ ?customer-id
                 :where [?customer :customer/id ?customer-id]] database customer-id)
          ffirst
          (dissoc :db/id)))
