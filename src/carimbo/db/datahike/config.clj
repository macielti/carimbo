(ns carimbo.db.datahike.config
  (:require [carimbo.wire.datahike.customer :as wire.datahike.customer]
            [carimbo.wire.datahike.transaction :as wire.datahike.transaction]))

(def schemas (concat []
                     wire.datahike.customer/customer-skeleton
                     wire.datahike.transaction/transaction-skeleton))