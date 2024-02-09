(ns carimbo.diplomat.http-server
  (:require [carimbo.diplomat.http-server.transaction :as diplomat.http-server.transaction]))

(def routes [["/clientes/:customer-id/transacoes"
              :post diplomat.http-server.transaction/create-transaction!
              :route-name :create-transaction]])