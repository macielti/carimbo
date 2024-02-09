(ns carimbo.adapters.customer
  (:require [carimbo.models.customer :as models.customer]
            [carimbo.wire.out.customer :as wire.out.customer]
            [schema.core :as s]))

(s/defn internal->wire :- wire.out.customer/Customer
  [{:customer/keys [limit balance]} :- models.customer/Customer]
  {:limite limit
   :saldo  balance})