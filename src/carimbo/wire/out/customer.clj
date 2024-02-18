(ns carimbo.wire.out.customer
  (:require [schema.core :as s]))

(s/defschema Customer
  {:limite s/Int
   :saldo  s/Int})