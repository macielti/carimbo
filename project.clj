(defproject carimbo "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.taoensso/timbre "6.3.1"]
                 [prismatic/plumbing "0.6.0"]
                 [camel-snake-kebab "0.4.3"]
                 [io.pedestal/pedestal.jetty "0.6.3"]
                 [io.pedestal/pedestal.route "0.6.3"]
                 [io.pedestal/pedestal.service "0.6.3"]
                 [io.pedestal/pedestal.interceptor "0.6.3"]
                 [com.stuartsierra/component "1.1.0"]
                 [siili/humanize "0.1.1"]
                 [prismatic/schema "1.4.1"]
                 [hashp "0.2.1"]
                 [clojure.java-time "1.4.2"]
                 [nubank/matcher-combinators "3.5.0"]
                 [org.postgresql/postgresql "42.7.1"]
                 [com.github.seancorfield/next.jdbc "1.3.909"]
                 [org.testcontainers/postgresql "1.17.6"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.4"]]

  :injections [(require 'hashp.core)]

  :repl-options {:init-ns carimbo.components}

  :test-paths ["test/unit" "test/integration" "test/helpers"]

  :main carimbo.components)
