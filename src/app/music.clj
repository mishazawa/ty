(ns app.music)

(require '[app.database :as db]
         '[monger.collection :as mc]
         '[monger.conversion :refer [from-db-object]]
         '[monger.result :refer [acknowledged?]]

         '[amazonica.core :refer [with-credential defcredential]]
         '[amazonica.aws.s3 :as s3]
         '[environ.core :refer [env]])

(import  'org.bson.types.ObjectId)

(defcredential (env :aws-access-key) (env :aws-secret-key) (env :region))

(def coll "music")

(defn get-music-list []
  (db/db-query #(mc/find-maps %1 %2) coll))

(defn get-music-info [id]
  (db/db-query #(mc/find-one-as-map %1 %2 { :_id id }) coll))

(defn music-item-blank [data]
  (conj data { :guid (str (java.util.UUID/randomUUID)) }))

(defn create-music-item [data]
  (db/db-query #(mc/insert-and-return %1 %2 (music-item-blank (get data :body))) coll))

(defn update-music-item [id data]
  (acknowledged? (db/db-query #(mc/update-by-id %1 %2 (ObjectId. id) { :$set data }) coll)))


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
