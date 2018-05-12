(ns app.processing)

(require '[environ.core :refer [env]]
         '[clojure.string :as string]
         '[slugify.core :refer [slugify]])

(use '[clojure.java.shell :only [sh]])

(defn get-filename [url]
  (let [filename (last (string/split url #"/"))]
    {:filename filename :url url :slug (slugify filename)}))

(defn ffcommand [{:keys [filename url slug]}]
  (apply sh (string/split (format "bash src/app/scripts/process.sh %s %s %s %s"
          slug url (env :aws-secret-key) (env :aws-access-key)) #" "))
  {:slug slug})

(defn get-and-process [url]
  (-> url get-filename ffcommand))
