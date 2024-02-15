(ns carimbo.diplomat.http-server.transaction
  (:require [carimbo.adapters.transaction :as adapters.transaction]
            [carimbo.postgresql :as component.postgresql]
            [schema.core :as s]
            [carimbo.controllers.transaction :as controllers.transaction]
            [carimbo.adapters.customer :as adapters.customer]))

(s/defn create-transaction!
  [{transaction           :json-params
    {:keys [customer-id]} :path-params
    {:keys [postgresql]}    :components}]
  {:status 200
   :body   (-> (adapters.transaction/wire->internal transaction (Integer/parseInt customer-id))
               (controllers.transaction/create-transaction! (component.postgresql/get-connection postgresql))
               adapters.customer/internal->wire)})