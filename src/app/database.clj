(ns app.database)

(require '[monger.core :as mg]
         '[monger.json]
         '[environ.core :refer [env]])

(def ^:const mongo-url (str "mongodb://" (env :client) ":" (env :pass) "@" (env :database-url)))

(def connection (mg/connect-via-uri mongo-url))

(defn db-query [f coll]
  (let [db (:db connection)]
    (f db coll)))
