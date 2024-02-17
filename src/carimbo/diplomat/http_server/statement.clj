(ns carimbo.diplomat.http-server.statement
  (:require [carimbo.controllers.statement :as controllers.statement]
            [carimbo.adapters.statement :as adapters.statement]))

(defn fetch-statement
  [{{:keys [customer-id]} :path-params
    {:keys [postgresql]}  :components}]
  {:status 200
   :body   (-> (controllers.statement/fetch-statement (Integer/parseInt customer-id) postgresql)
               adapters.statement/->wire)})