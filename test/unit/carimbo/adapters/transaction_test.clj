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

(s/deftest internal->database-test
  (testing "Given a internal transaction entity, we can adapt to datomic"
    (is (match? {:transaction/amount       200
                 :transaction/customer-id  1
                 :transaction/description  "2 reais ou um presente misterioso?"
                 :transaction/requested-at inst?
                 :transaction/type         :credit}
                (adapters.transaction/internal->database fixtures.transaction/credit-transaction)))))

(s/deftest database->internal-test
  (testing "Given a database transaction entity, we can adapt to internal model"
    (is (match? {:transaction/amount       200
                 :transaction/customer-id  1
                 :transaction/description  "2 reais ou um presente misterioso?"
                 :transaction/requested-at jt/local-date-time?
                 :transaction/type         :credit}
                (adapters.transaction/database->internal fixtures.transaction/database-credit-transaction)))))

(s/deftest type->wire-test
  (testing "Given a type transaction internal representation, we can adapt to external representation"
    (is (= "d"
           (adapters.transaction/type->wire :debit)))

    (is (= "c"
           (adapters.transaction/type->wire :credit)))))

(s/deftest ->wire-test
  (testing "Giving a Internal transaction entity, we should be able to externalize"
    (is (match? {:descricao    "2 reais ou um presente misterioso?"
                 :realizada_em string?
                 :tipo         "c"
                 :valor        200}
                (adapters.transaction/->wire fixtures.transaction/credit-transaction)))))
