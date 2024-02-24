(ns carimbo.adapters.customer-test
  (:require [clojure.test :refer :all]
            [carimbo.adapters.customer :as adapters.customer]
            [fixtures.customer]
            [schema.test :as s]))

(s/deftest internal-to-wire-test
  (testing "Given an internal representation for Customer entity, we should be able to externalize it"
    (is (= {:limite 100000
            :saldo  0}
           (adapters.customer/internal->wire fixtures.customer/customer)))))

(s/deftest database-to-internal-test
  (testing "Giving an Database customer entity we can internalize it"
    (is (= fixtures.customer/customer
           (adapters.customer/database->internal fixtures.customer/customer-database)))))
