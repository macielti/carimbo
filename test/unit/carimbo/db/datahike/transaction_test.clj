(ns carimbo.db.datahike.transaction-test
  (:require [carimbo.db.datahike.customer :as database.customer]
            [clojure.test :refer :all]
            [carimbo.components :as components]
            [carimbo.db.datahike.transaction :as database.transaction]
            [com.stuartsierra.component :as component]
            [fixtures.transaction]
            [fixtures.customer]
            [java-time.api :as jt]
            [schema.test :as s]
            [matcher-combinators.test :refer [match?]]))

(s/deftest insert-and-lookup-test
  (let [system (component/start components/system-test)
        db-connection (components/get-component-content :datahike system)]
    (testing "Given a internal transaction entity, we should be able to persist on the database"
      (is (= fixtures.transaction/credit-transaction
             (database.transaction/insert! fixtures.transaction/credit-transaction db-connection))))

    (testing "We should be able to query the customer by id"
      (is (match? [#:transaction{:transaction/amount       200
                                 :transaction/customer-id  1
                                 :transaction/description  "2 reais ou um presente misterioso?"
                                 :transaction/requested-at jt/local-date-time?
                                 :transaction/type         :credit}]
                  (database.transaction/by-customer 1 @db-connection)))

      (is (= []
             (database.transaction/by-customer 2 @db-connection))))

    (component/stop system)))

(s/deftest insert-with-account-balance-upsert-test
  (let [system (component/start components/system-test)
        db-connection (components/get-component-content :datahike system)]
    (testing "We should be able to persist a new Transaction entity upsert the customer balance"

      (is (= fixtures.customer/customer
             (database.customer/insert! fixtures.customer/customer db-connection)))

      (is (= fixtures.transaction/credit-transaction
             (database.transaction/insert-with-account-balance-upsert! fixtures.transaction/credit-transaction
                                                                       (biginteger 0)
                                                                       (biginteger 200)
                                                                       db-connection))))

    (testing "We should be able to query the customer by id"
      (is (match? [#:transaction{:transaction/amount       200
                                 :transaction/customer-id  1
                                 :transaction/description  "2 reais ou um presente misterioso?"
                                 :transaction/requested-at jt/local-date-time?
                                 :transaction/type         :credit}]
                  (database.transaction/by-customer 1 @db-connection)))

      (is (= {:customer/balance 200
              :customer/id      1
              :customer/limit   100000}
             (database.customer/lookup! 1 @db-connection))))

    (component/stop system)))
