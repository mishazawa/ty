(defproject d5 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-jetty-adapter "1.6.3"]

                 [environ "1.1.0"]
                 [compojure "1.6.1"]
                 [cheshire "5.3.1" :exclusions [com.fasterxml.jackson.core/jackson-core]]
                 [slugify "0.0.1"]
                 [org.clojure/java.jdbc "0.7.6"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [amazonica "0.3.121"]

                 [ring/ring-devel "1.6.3"]]
  :ring {:handler app.router/handler}
  :plugins [[lein-ring "0.12.1"]])

