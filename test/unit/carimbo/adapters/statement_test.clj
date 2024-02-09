(ns carimbo.adapters.statement-test
  (:require [clojure.test :refer :all]
            [carimbo.adapters.statement :as adapters.statement]
            [matcher-combinators.test :refer [match?]]
            [fixtures.statement]
            [schema.test :as s]))

(s/deftest to-wire-test
  (testing "Given a internal statement model, we can externalise it"
    (is (match? {:saldo              {:total        100
                                      :data_extrato string?
                                      :limite       1000}
                 :ultimas_transacoes [{:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2024-01-01T00:00"}
                                      {:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2025-01-01T00:00"}
                                      {:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2026-01-01T00:00"}
                                      {:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2027-01-01T00:00"}
                                      {:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2028-01-01T00:00"}
                                      {:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2029-01-01T00:00"}
                                      {:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2030-01-01T00:00"}
                                      {:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2031-01-01T00:00"}
                                      {:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2032-01-01T00:00"}
                                      {:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2033-01-01T00:00"}
                                      {:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2034-01-01T00:00"}
                                      {:valor        200
                                       :tipo         "c"
                                       :descricao    "2 reais ou um presente misterioso?"
                                       :realizada_em "2035-01-01T00:00"}]}
                (adapters.statement/->wire fixtures.statement/statement)))))
