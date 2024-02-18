(ns carimbo.adapters.customer
  (:require [carimbo.models.customer :as models.customer]
            [carimbo.wire.out.customer :as wire.out.customer]
            [schema.core :as s]))

(s/defn internal->wire :- wire.out.customer/Customer
  [{:customer/keys [limit balance]} :- models.customer/Customer]
  {:limite limit
   :saldo  balance})

(s/defn database->internal :- models.customer/Customer
  [{:customer/keys [customer_id customer_balance customer_limit]}]
  {:customer/id      customer_id
   :customer/limit   customer_limit
   :customer/balance customer_balance})