(ns fixtures.transaction
  (:require [java-time.api :as jt])
  (:import (java.util Date)))

(def requested-at (jt/local-date-time (jt/zone-id "UTC")))

(def wire-credit-transaction
  {:valor     200
   :tipo      "c"
   :descricao "2 reais ou um presente misterioso?"})

(def credit-transaction
  {:transaction/amount       (biginteger 200)
   :transaction/customer-id  1
   :transaction/description  "2 reais ou um presente misterioso?"
   :transaction/requested-at requested-at
   :transaction/type         :credit})

(def database-credit-transaction
  (assoc credit-transaction :transaction/requested-at (Date.)))
