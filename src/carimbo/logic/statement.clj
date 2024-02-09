(ns carimbo.logic.statement
  (:require [carimbo.models.customer :as models.customer]
            [carimbo.models.transaction :as models.transaction]
            [schema.core :as s]
            [carimbo.models.statement :as models.statement])
  (:import (java.time LocalDateTime)))

(s/defn ->statement :- models.statement/Statement
  [{:customer/keys [balance limit]} :- models.customer/Customer
   recent-transactions :- [models.transaction/Transaction]
   as-of :- LocalDateTime]
  {:statement/balance             balance
   :statement/requested-at        as-of
   :statement/limit               limit
   :statement/recent-transactions recent-transactions})