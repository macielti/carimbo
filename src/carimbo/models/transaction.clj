(ns carimbo.models.transaction
  (:require [schema.core :as s])
  (:import (java.time LocalDateTime)))

(def types #{:credit :debit})
(def Type (apply s/enum types))

(def transaction-skeleton
  {:transaction/customer-id  s/Int
   :transaction/amount       s/Int
   :transaction/type         Type
   :transaction/description  s/Str
   :transaction/requested-at LocalDateTime})

(s/defschema Transaction
  transaction-skeleton)