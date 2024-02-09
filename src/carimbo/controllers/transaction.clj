(ns carimbo.controllers.transaction
  (:require [carimbo.models.transaction :as models.transaction]
            [schema.core :as s]))

(s/defn create-transaction
  [transaction :- models.transaction/Transaction
   db-connection])