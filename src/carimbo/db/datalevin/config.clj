(ns carimbo.db.datalevin.config
  (:require [carimbo.wire.datalevin.customer :as wire.datalevin.customer]))

(def schema (merge {}
                   wire.datalevin.customer/customer-skeleton))