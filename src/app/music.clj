(ns app.music)

(require '[app.database :as db]
         '[clojure.java.jdbc :as sql]

         '[amazonica.core :refer [with-credential defcredential]]
         '[amazonica.aws.s3 :as s3]

         '[environ.core :refer [env]])

(defcredential (env :aws-access-key) (env :aws-secret-key) (env :region))

(defn get-music-list []
  (db/db-query ["select * from music"]))

(defn get-music-info [id]
  (db/db-query ["select * from music where guid = ?" id]))

(defn music-item-blank [data]
  (conj data { :guid (str (java.util.UUID/randomUUID)) }))

(defn create-music-item [data]
  (db/db-insert! (music-item-blank (get data :body))))

(defn update-music-item [id data]
  (db/db-update! id data))


(defn sign-s3 [body]
  (let [file-name (:file-name body) file-type (:file-type body)]
    (let [signed-url (s3/generate-presigned-url
                        {:bucket-name (env :bucket-name)
                         :access-control-list {:grant-permission ["AllUsers" "Read"]}
                         :key file-name
                         :content-type file-type
                         :expires 60
                         :method "PUT"})
          url (str "https://" (env :bucket-name) ".s3.amazonaws.com/" file-name)]
    {:signedRequest (str signed-url) :url url})))
