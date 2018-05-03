(ns app.router)

(require '[compojure.core :refer :all]
         '[compojure.route :as route]
         '[ring.middleware.params :refer [wrap-params]]
         '[ring.middleware.json :refer [wrap-json-response wrap-json-body]]
         '[ring.util.response :refer [response]]
         '[app.music :as music]
         '[app.database :refer [create-db]])

(defn wrap-handler [f]
  (-> f wrap-params wrap-json-body wrap-json-response))

(defn get-list [request]
  (let [data (music/get-music-list)]
  (response { :data data })))

(defn get-info [params]
  (let [data (music/get-music-info (:id params))]
  (response { :data data })))

(defn create-item [request]
  (let [data (music/create-music-item request)]
  (response { :data data })))

(defn update-item [request]
  (let [data (music/update-music-item (:id (:params request)) (:body request))]
  (response { :data data })))

;; rwr to middleware

(defn to-keyw [m]
  (into {} (for [[k v] m] [(keyword k) v])))

(defn sign-url [request]
  (response (music/sign-s3 (to-keyw (:body request)))))

(create-db)

(defroutes handler
  (context "/music" []
      (GET  "/"     [] (wrap-handler get-list))
      (POST "/sign" [request] #((wrap-handler sign-url) %))
      (POST "/"     [request] #((wrap-handler create-item) %))
      (PUT  "/:id"  [request] #((wrap-handler update-item) %))
      (GET  "/:id"  [request] #((wrap-handler get-info) (:params %))))
  (route/not-found "Nothing is real"))

