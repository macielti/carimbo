(ns fixtures.statement
  (:require [clojure.test :refer :all]
            [java-time.api :as jt]
            [fixtures.transaction]))

(def requested-at (jt/local-date-time))

(def statement
  {:statement/balance             100
   :statement/requested-at        requested-at
   :statement/limit               1000
   :statement/recent-transactions fixtures.transaction/transactions})
