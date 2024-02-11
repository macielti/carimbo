(ns carimbo.diplomat.http-server
  (:require [carimbo.diplomat.http-server.transaction :as diplomat.http-server.transaction]
            [carimbo.diplomat.http-server.statement :as diplomat.http-server.statement]
            [carimbo.wire.in.transaction :as wire.in.transaction]
            [common-clj.io.interceptors :as io.interceptors]))

(def routes [["/clientes/:customer-id/transacoes"
              :post [(io.interceptors/schema-body-in-interceptor wire.in.transaction/Transaction)
                     diplomat.http-server.transaction/create-transaction!]
              :route-name :create-transaction]

             ["/clientes/:customer-id/extrato"
              :get diplomat.http-server.statement/fetch-statement
              :route-name :fetch-statement]])