(ns carimbo.db.datalevin.customer-test
  (:require [clojure.test :refer :all]
            [carimbo.db.datalevin.customer :as database.customer]
            [datalevin.core :as d]
            [carimbo.db.datalevin.config :as database.config]
            [fixtures.customer]))

(deftest insert-test
  (testing "Given a document entity we can persist it on the database (datalevin)"
    (let [database-uri (datalevin.util/tmp-dir (str "query-or-" (random-uuid)))
          db-connection (d/get-conn database-uri database.config/schema)]

      (is (= fixtures.customer/customer
             (database.customer/insert! fixtures.customer/customer db-connection))))))
