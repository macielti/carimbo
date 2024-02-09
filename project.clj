(defproject carimbo "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [prismatic/schema "1.4.1"]
                 [datalevin "0.8.25"]
                 [net.clojars.macielti/common-clj "24.49.48"]
                 [hashp "0.2.1"]
                 [clojure.java-time "1.4.2"]
                 [nubank/matcher-combinators "3.5.0"]
                 [ch.qos.logback/logback-classic "1.2.10"]]

  :injections [(require 'hashp.core)]

  :repl-options {:init-ns carimbo.components}

  :test-paths ["test/unit" "test/integration" "test/helpers"]

  :jvm-opts ^:replace ["--add-opens=java.base/java.nio=ALL-UNNAMED"
                       "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED"]

  :main carimbo.components)
