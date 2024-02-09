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

(def transactions
  [{:transaction/amount       (biginteger 200)
    :transaction/customer-id  1
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2024)
    :transaction/type         :credit}
   {:transaction/amount       (biginteger 200)
    :transaction/customer-id  2
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2025)
    :transaction/type         :credit}
   {:transaction/amount       (biginteger 200)
    :transaction/customer-id  3
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2026)
    :transaction/type         :credit}
   {:transaction/amount       (biginteger 200)
    :transaction/customer-id  4
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2027)
    :transaction/type         :credit}
   {:transaction/amount       (biginteger 200)
    :transaction/customer-id  5
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2028)
    :transaction/type         :credit}
   {:transaction/amount       (biginteger 200)
    :transaction/customer-id  6
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2029)
    :transaction/type         :credit}
   {:transaction/amount       (biginteger 200)
    :transaction/customer-id  7
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2030)
    :transaction/type         :credit}
   {:transaction/amount       (biginteger 200)
    :transaction/customer-id  8
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2031)
    :transaction/type         :credit}
   {:transaction/amount       (biginteger 200)
    :transaction/customer-id  9
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2032)
    :transaction/type         :credit}
   {:transaction/amount       (biginteger 200)
    :transaction/customer-id  10
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2033)
    :transaction/type         :credit}
   {:transaction/amount       (biginteger 200)
    :transaction/customer-id  11
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2034)
    :transaction/type         :credit}
   {:transaction/amount       (biginteger 200)
    :transaction/customer-id  12
    :transaction/description  "2 reais ou um presente misterioso?"
    :transaction/requested-at (jt/local-date-time 2035)
    :transaction/type         :credit}])
