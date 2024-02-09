(ns carimbo.logic.transaction
  (:require [java-time.api :as jt]
            [schema.core :as s]
            [carimbo.models.transaction :as models.transaction]))

(s/defn recent-transactions :- [models.transaction/Transaction]
  [transactions :- [models.transaction/Transaction]]
  (->> transactions
       (sort-by :transaction/requested-at jt/after?)
       (take 10)))