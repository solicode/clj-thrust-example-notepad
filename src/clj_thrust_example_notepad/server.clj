(ns clj-thrust-example-notepad.server
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer :all]
            [org.httpkit.server :refer :all]
            [ring.middleware.defaults :refer :all]
            [clojure.java.io :as io])
  (:import [java.awt FileDialog Frame]))

(def port 7171)

(defroutes site-routes
  (GET "/" []
    (slurp (io/resource "public/index.html")))

  (GET "/save-dialog" []
    (let [dialog (FileDialog. (cast Frame nil) "Save" FileDialog/SAVE)]
      (.setVisible dialog true)
      (.toFront dialog)
      (.requestFocus dialog)

      ; Note: `getFiles` is a blocking call. It waits until the user selects
      ; a file before returning.
      (str (first (.getFiles dialog)))))

  (POST "/save" {{:keys [path text]} :params :as request}
    (when-not (empty? path)
      (spit path text :encoding "UTF-8")
      (println "Saved file to:" path))
    "OK")

  (route/resources "/")
  (route/not-found "Not found (404)"))

(defn wrap-strip-trailing-slash
  "If the requested url has a trailing slash, remove it."
  [handler]
  (fn [request]
    (handler (update-in request [:uri] clojure.string/replace #"(?<=.)/$" ""))))

(def site
  (-> site-routes
    (wrap-defaults api-defaults)
    (wrap-strip-trailing-slash)))

(defn start []
  (println "Starting server at port" port)
  (run-server #'site {:port port}))

(defn stop [server]
  (println "Stopping server")
  (server))