(ns app.database)

(require '[clojure.java.jdbc :as j]
         '[environ.core :refer [env]])

(def ^:private db-spec
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     (env :database)})

(defn create-db []
  (try (j/db-do-commands db-spec
          (j/create-table-ddl :music
            [[:album :text]
             [:url :text]
             [:processed_url :text]
             [:talb :text]
             [:trck :text]
             [:tit2 :text]
             [:tyer :text]
             [:tlen :text]
             [:genre :text]
             [:title :text]
             [:type :text]
             [:version :text]
             [:artist :text]
             [:guid :text]]))
       (catch Exception e (println e))))

(defn db-query [que]
  (j/query db-spec que))

(defn db-insert! [data]
  (j/insert! db-spec :music data))

(defn db-update! [id data]
  (j/update! db-spec :music data ["guid = ?" id]))
