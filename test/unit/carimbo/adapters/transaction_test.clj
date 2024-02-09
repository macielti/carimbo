(ns carimbo.adapters.transaction-test
  (:require [clojure.test :refer :all]
            [carimbo.adapters.transaction :as adapters.transaction]
            [fixtures.transaction]
            [java-time.api :as jt]
            [schema.test :as s]
            [matcher-combinators.test :refer [match?]]))

(s/deftest wire-type-to-internal-test
  (testing "Given a wire transaction type, we can adapt to internal representation"
    (is (= :credit
           (adapters.transaction/wire-type->internal "c")))
    (is (= :debit
           (adapters.transaction/wire-type->internal "d")))))

(s/deftest wire->internal-test
  (testing "Given a wire transaction, we can adapt to internal representation"
    (is (match? {:transaction/amount       200
                 :transaction/customer-id  1
                 :transaction/description  "2 reais ou um presente misterioso?"
                 :transaction/requested-at jt/local-date-time?
                 :transaction/type         :credit}
                (adapters.transaction/wire->internal fixtures.transaction/wire-credit-transaction 1)))))
