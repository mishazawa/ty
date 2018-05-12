(ns app.processing)

(require '[environ.core :refer [env]])

(use '[clojure.java.shell :only [sh]])

(defn ffcommand [filename url]
  (format "AWS_SECRET_ACCESS_KEY=%s AWS_ACCESS_KEY_ID=%s t.sh %s %s" :aws-access-key ))

(defn get-and-process [url base-url segment-filename]
  (println (sh (ffcommand url base-url segment-filename))))


; (get-and-process "https://s3.us-east-2.amazonaws.com/kletka/01-%E6%9D%B1%E4%BA%ACmp3" "" "song-%05d.ts")
; (ffcommand "https://s3.us-east-2.amazonaws.com/kletka/01-%E6%9D%B1%E4%BA%ACmp3" "" "song-%05d.ts")
