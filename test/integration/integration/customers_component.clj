(ns integration.customers-component
  (:require [carimbo.db.postgresql.customer :as database.customer]
            [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [common-clj.component.helper.core :as component.helper]
            [carimbo.components :as components]
            [schema.test :as s]))

(s/deftest customers-component
  (let [system (component/start components/system-test)
        db-connection (component.helper/get-component-content :postgresql system)]

    (testing "Performed registration of initial customers entities"
      (is (= {:customer/balance 0
              :customer/id      1
              :customer/limit   100000}
             (database.customer/lookup! 1 db-connection)))

      (is (= {:customer/balance 0
              :customer/id      2
              :customer/limit   80000}
             (database.customer/lookup! 2 db-connection)))

      (is (= {:customer/balance 0
              :customer/id      3
              :customer/limit   1000000}
             (database.customer/lookup! 3 db-connection)))

      (is (= {:customer/balance 0
              :customer/id      4
              :customer/limit   10000000}
             (database.customer/lookup! 4 db-connection)))

      (is (= {:customer/balance 0
              :customer/id      5
              :customer/limit   500000}
             (database.customer/lookup! 5 db-connection))))

    (component/stop system)))