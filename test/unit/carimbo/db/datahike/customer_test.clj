(ns carimbo.db.datahike.customer-test
  (:require [carimbo.components :as components]
            [clojure.test :refer :all]
            [carimbo.db.datahike.customer :as database.customer]
            [com.stuartsierra.component :as component]
            [fixtures.customer]
            [schema.test :as s])
  (:import (clojure.lang ExceptionInfo)))

(s/deftest insert-and-lookup-test
  (let [system (component/start components/system-test)
        db-connection (components/get-component-content :datahike system)]
    (testing "Given a internal customer entity, we should be able to persist on the database"
      (is (= fixtures.customer/customer
             (database.customer/insert! fixtures.customer/customer db-connection))))

    (testing "We should be able to query the customer by id"
      (is (= fixtures.customer/customer
             (database.customer/lookup! 1 @db-connection)))

      (is (thrown? ExceptionInfo (database.customer/lookup! 2 @db-connection))))

    (component/stop system)))
