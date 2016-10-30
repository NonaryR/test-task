(ns test-task.handler
  (:require [test-task.db :as db]
            [test-task.views :as views]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [ring.adapter.jetty :as jetty])
  (:gen-class))


(defroutes app-routes
  (GET "/" [] (views/home-page))
  (GET "/api/items" [] (views/all-items-page))
  (GET "/add-item" [] (views/add-items-page))
  (POST "/add-item" {params :params} (views/add-items-results-page params))
  (GET "/api/items/:id" [id] (views/display-post id))
  (GET "/update-item" [] (views/add-update-page))
  (POST "/update-item" {params :params} (views/add-update-results-page params)))

(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))


(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty #'app {:port  port
                            :join? false})))
