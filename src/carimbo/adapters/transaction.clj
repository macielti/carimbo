(ns carimbo.adapters.transaction
  (:require [carimbo.models.transaction :as models.transaction]
            [carimbo.wire.in.transaction :as wire.in.transaction]
            [carimbo.wire.datalevin.transaction :as wire.database.transaction]
            [carimbo.wire.out.transaction :as wire.out.transaction]
            [java-time.api :as jt]
            [schema.core :as s]))

(s/defn wire-type->internal :- models.transaction/Type
  [type :- wire.in.transaction/Type]
  (case type
    "c" :credit
    "d" :debit))

(s/defn type->wire :- wire.in.transaction/Type
  [type :- models.transaction/Type]
  (case type
    :credit "c"
    :debit "d"))

(s/defn wire->internal :- models.transaction/Transaction
  [{:keys [tipo descricao valor]} :- wire.in.transaction/Transaction
   customer-id :- s/Int]
  {:transaction/customer-id  customer-id
   :transaction/amount       (biginteger valor)
   :transaction/type         (wire-type->internal tipo)
   :transaction/description  descricao
   :transaction/requested-at (jt/local-date-time (jt/zone-id "UTC"))})

(s/defn internal->database :- wire.database.transaction/Transaction
  [{:transaction/keys [requested-at] :as transaction} :- models.transaction/Transaction]
  (assoc transaction :transaction/requested-at (-> (jt/zoned-date-time requested-at (jt/zone-id "UTC"))
                                                   jt/java-date)))

(s/defn database->internal :- models.transaction/Transaction
  [{:transaction/keys [requested-at] :as transaction} :- wire.database.transaction/Transaction]
  (assoc transaction :transaction/requested-at (jt/local-date-time requested-at (jt/zone-id "UTC"))))

(s/defn ->wire :- wire.out.transaction/Transaction
  [{:transaction/keys [amount type description requested-at]} :- models.transaction/Transaction]
  {:valor        amount
   :tipo         (type->wire type)
   :descricao    description
   :realizada_em (str requested-at)})
