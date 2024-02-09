(ns carimbo.logic.transaction-test
  (:require [clojure.test :refer :all]
            [carimbo.logic.transaction :as logic.transaction]
            [fixtures.transaction]
            [schema.test :as s]
            [matcher-combinators.test :refer [match?]]))

(s/deftest recent-transactions-test
  (testing "Given a list of transactions, we can take the 10 most recent ones in descending order"
    (is (match? [#:transaction{:customer-id 12}
                 #:transaction{:customer-id 11}
                 #:transaction{:customer-id 10}
                 #:transaction{:customer-id 9}
                 #:transaction{:customer-id 8}
                 #:transaction{:customer-id 7}
                 #:transaction{:customer-id 6}
                 #:transaction{:customer-id 5}
                 #:transaction{:customer-id 4}
                 #:transaction{:customer-id 3}]
                (logic.transaction/recent-transactions fixtures.transaction/transactions)))))
