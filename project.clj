(defproject carimbo "0.1.0-SNAPSHOT"

  :description "My submission for the second edition of the 'Rinha de Backend' (Q1 2024)"
  :url "https://github.com/macielti/carimbo"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clojure.java-time "1.4.2"]
                 [net.clojars.macielti/common-clj "25.49.48"]]

  :profiles {:test {:injections [(require 'hashp.core)]}}

  :repl-options {:init-ns carimbo.components}

  :src-dirs ["src"]

  :aot :all

  :test-paths ["test/unit" "test/integration" "test/helpers"]

  :main carimbo.components)
