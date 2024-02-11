(ns carimbo.wire.in.transaction
  (:require [schema.core :as s]))

(def types #{"c" "d"})
(def Type (apply s/enum types))

(s/defschema Transaction
  {:valor     s/Int
   :tipo      Type
   :descricao (s/pred #(and (string? %) (<= 1 (count %) 10)))})