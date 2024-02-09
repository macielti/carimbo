(ns carimbo.wire.out.statement
  (:require [carimbo.wire.out.transaction :as wire.out.transaction]
            [schema.core :as s]))

(def balance-skeleton
  {:total        BigInteger
   :data_extrato s/Str
   :limite       BigInteger})

(s/defschema Balance
  balance-skeleton)

(def statement-skeleton
  {:saldo              Balance
   :ultimas_transacoes [wire.out.transaction/Transaction]})

(s/defschema Statement
  statement-skeleton)