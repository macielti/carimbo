(ns carimbo.diplomat.http-server
  (:require [carimbo.diplomat.http-server.transaction :as diplomat.http-server.transaction]
            [carimbo.diplomat.http-server.statement :as diplomat.http-server.statement]))

(def routes [["/clientes/:customer-id/transacoes"
              :post diplomat.http-server.transaction/create-transaction!
              :route-name :create-transaction]

             ["/clientes/:customer-id/extrato"
              :get diplomat.http-server.statement/fetch-statement
              :route-name :fetch-statement]])