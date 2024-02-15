(ns carimbo.db.postgresql.customer-test
  (:require [carimbo.postgresql :as postgresql]
            [clojure.test :refer :all]
            [schema.test :as s]
            [fixtures.transaction]
            [carimbo.db.postgresql.customer :as database.customer])
  (:import (clojure.lang ExceptionInfo)))

(s/deftest insert-and-lookup-test
  (testing "Given a customer entity we can persist it on the database (datalevin)"
    (let [{:keys [database-connection]} (postgresql/postgresql-for-unit-tests "resources/schema.sql")]

      (is (= fixtures.customer/customer
             (database.customer/insert! fixtures.customer/customer database-connection)))

      (is (= {:customer/id      1
              :customer/balance 0
              :customer/limit   100000}
             (database.customer/lookup! 1 database-connection)))

      (is (thrown? ExceptionInfo (database.customer/lookup! 2 database-connection)))

      (is (= {:customer/id      1
              :customer/balance 0
              :customer/limit   100000}
             (database.customer/lookup 1 database-connection)))

      (is (nil? (database.customer/lookup 2 database-connection))))))

(s/deftest update-balance-test
  (testing "That we can update balance"
    (let [{:keys [database-connection]} (postgresql/postgresql-for-unit-tests "resources/schema.sql")]

      (is (= fixtures.customer/customer
             (database.customer/insert! fixtures.customer/customer database-connection)))

      (database.customer/update-balance! 1 fixtures.transaction/credit-transaction database-connection)

      (is (thrown? ExceptionInfo (database.customer/update-balance! 1 (assoc fixtures.transaction/debit-transaction
                                                                                                 :transaction/amount (biginteger 1000000)) database-connection)))

      (is (= {:customer/id      1
              :customer/balance 200
              :customer/limit   100000}
             (database.customer/lookup! 1 database-connection))))))
