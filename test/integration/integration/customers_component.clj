(ns integration.customers-component
  (:require [carimbo.db.datalevin.customer :as database.customer]
            [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [carimbo.components :as components]
            [common-clj.component.helper.core :as component.helper]
            [datalevin.core :as d]
            [schema.test :as s]))

(s/deftest customers-component
  (let [system (component/start components/system-test)
        db-connection (component.helper/get-component-content :datalevin system)
        database (d/db db-connection)]

    (testing "Performed registration of initial customers entities"
      (is (= {:customer/balance 0
              :customer/id      1
              :customer/limit   100000}
             (database.customer/lookup! 1 database)))

      (is (= {:customer/balance 0
              :customer/id      2
              :customer/limit   80000}
             (database.customer/lookup! 2 database)))

      (is (= {:customer/balance 0
              :customer/id      3
              :customer/limit   1000000}
             (database.customer/lookup! 3 database)))

      (is (= {:customer/balance 0
              :customer/id      4
              :customer/limit   10000000}
             (database.customer/lookup! 4 database)))

      (is (= {:customer/balance 0
              :customer/id      5
              :customer/limit   500000}
             (database.customer/lookup! 5 database))))

    (component/stop system)))