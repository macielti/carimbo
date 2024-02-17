(ns carimbo.interceptors
  (:require [carimbo.error :as error]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.interceptor :as pedestal.interceptor]
            [io.pedestal.interceptor.error :as interceptor.error]
            [schema.core :as s]
            [taoensso.timbre :as log]
            [humanize.schema :as h])
  (:import (clojure.lang ExceptionInfo)))

(def error-handler-interceptor
  (interceptor.error/error-dispatch [ctx ex]
                        [{:exception-type :clojure.lang.ExceptionInfo}]
                        (let [{:keys [status error message detail]} (ex-data ex)]
                          (assoc ctx :response {:status status
                                                :body   {:error   error
                                                         :message message
                                                         :detail  detail}}))

                        :else
                        (do (log/error ex)
                            (assoc ctx :response {:status 500 :body {:error   "unexpected-server-error"
                                                                     :message "Internal Server Error"
                                                                     :detail  "Internal Server Error"}}))))

(defn schema-body-in-interceptor [schema]
  (pedestal.interceptor/interceptor {:name  ::schema-body-in-interceptor
                                     :enter (fn [{{:keys [json-params]} :request :as context}]
                                              (try (s/validate schema json-params)
                                                   (catch ExceptionInfo e
                                                     (when (= (:type (ex-data e)) :schema.core/error)
                                                       (error/http-friendly-exception 422
                                                                                      "invalid-schema-in"
                                                                                      "The system detected that the received data is invalid"
                                                                                      (get-in (h/ex->err e) [:unknown :error])))))
                                              context)}))


(defn components-interceptor [system-components]
  (pedestal.interceptor/interceptor {:name  ::components-interceptor
                                     :enter (fn [context]
                                              (assoc-in context [:request :components] system-components))}))

(defn common-interceptors [components]
  [(body-params/body-params)
   (components-interceptor components)
   http/json-body
   error-handler-interceptor])
