(ns fixtures.statement
  (:require [clojure.test :refer :all]
            [java-time.api :as jt]
            [fixtures.transaction]))

(def requested-at (jt/local-date-time))

(def statement
  {:statement/balance             (biginteger 100)
   :statement/requested-at        requested-at
   :statement/limit               (biginteger 1000)
   :statement/recent-transactions fixtures.transaction/transactions})