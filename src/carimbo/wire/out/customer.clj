(ns carimbo.wire.out.customer
  (:require [schema.core :as s]))

(s/defschema Customer
  {:limite BigInteger
   :saldo  BigInteger})