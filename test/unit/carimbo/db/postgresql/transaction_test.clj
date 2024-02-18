(ns carimbo.db.postgresql.transaction-test
  (:require [clojure.test :refer :all]
            [common-clj.component.postgresql :as component.postgresql]
            [fixtures.transaction]
            [carimbo.db.postgresql.transaction :as database.transaction]
            [java-time.api :as jt]
            [schema.test :as s]
            [matcher-combinators.test :refer [match?]]))

(s/deftest insert-test
  (testing "That we can update balance"
    (let [{:keys [database-connection]} (component.postgresql/posgresql-component-for-unit-tests "resources/schema.sql")]
      (database.transaction/insert! fixtures.transaction/credit-transaction database-connection))))

(s/deftest by-customer-test
  (testing "Query transactions by customer"
    (let [{:keys [database-connection]} (component.postgresql/posgresql-component-for-unit-tests "resources/schema.sql")]
      (database.transaction/insert! fixtures.transaction/credit-transaction database-connection)
      (database.transaction/insert! fixtures.transaction/debit-transaction database-connection)
      (is (match? [{:transaction/customer-id  1
                    :transaction/amount       200
                    :transaction/type         :credit
                    :transaction/description  "PIX chegou!"
                    :transaction/requested-at jt/local-date-time?}
                   {:transaction/customer-id  1
                    :transaction/amount       200
                    :transaction/type         :debit
                    :transaction/description  "tira"
                    :transaction/requested-at jt/local-date-time?}]
                  (database.transaction/by-customer 1 database-connection))))))
