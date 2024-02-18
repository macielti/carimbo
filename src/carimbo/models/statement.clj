(ns carimbo.models.statement
  (:require [carimbo.models.transaction :as models.transaction]
            [schema.core :as s])
  (:import (java.time LocalDateTime)))

(def statement-skeleton
  {:statement/balance             s/Int
   :statement/requested-at        LocalDateTime
   :statement/limit               s/Int
   :statement/recent-transactions [models.transaction/Transaction]})

(s/defschema Statement
  statement-skeleton)
