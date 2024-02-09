(ns carimbo.adapters.statement
  (:require [carimbo.adapters.transaction :as adapters.transaction]
            [carimbo.models.statement :as models.statement]
            [carimbo.wire.out.statement :as wire.out.statement]
            [schema.core :as s]))

(s/defn ->wire :- wire.out.statement/Statement
  [{:statement/keys [balance limit recent-transactions requested-at]} :- models.statement/Statement]
  {:saldo              {:total        balance
                        :data_extrato (str requested-at)
                        :limite       limit}
   :ultimas_transacoes (map adapters.transaction/->wire recent-transactions)})