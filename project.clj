(defproject carimbo "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [prismatic/schema "1.4.1"]
                 [datalevin "0.8.25"]]

  :test-paths ["test/unit" "test/integration" "test/helpers"]

  :jvm-opts ^:replace ["--add-opens=java.base/java.nio=ALL-UNNAMED"
                       "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED"]


  :repl-options {:init-ns carimbo.core})
