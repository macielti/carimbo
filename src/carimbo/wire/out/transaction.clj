(ns carimbo.wire.out.transaction
  (:require [carimbo.wire.in.transaction :as wire.in.transaction]
            [schema.core :as s]))

(def transaction-skeleton
  {:valor        BigInteger
   :tipo         wire.in.transaction/Type
   :descricao    s/Str
   :realizada_em s/Str})

(s/defschema Transaction
  transaction-skeleton)