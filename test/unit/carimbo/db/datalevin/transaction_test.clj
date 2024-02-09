(ns carimbo.db.datalevin.transaction-test
  (:require [clojure.test :refer :all])
  (:require [carimbo.db.datalevin.config :as database.config]
            [carimbo.db.datalevin.customer :as database.customer]
            [carimbo.db.datalevin.transaction :as database.transaction]
            [fixtures.transaction]
            [datalevin.core :as d]
            [schema.test :as s]))

(s/deftest insert-with-account-balance-upsert!-test
  (testing "Given a customer entity we can persist it on the database (datalevin)"
    (let [database-uri (datalevin.util/tmp-dir (str "query-or-" (random-uuid)))
          db-connection (d/get-conn database-uri database.config/schema)]

      (database.customer/insert! fixtures.customer/customer db-connection)

      (is (= fixtures.transaction/credit-transaction
             (database.transaction/insert-with-account-balance-upsert! fixtures.transaction/credit-transaction
                                                                       (biginteger 0)
                                                                       (biginteger -200)
                                                                       db-connection))))))
