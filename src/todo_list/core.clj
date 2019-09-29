(ns todo-list.core
 (:require [ring.adapter.jetty :as jetty]
          [ring.middleware.reload :refer [wrap-reload]]
          [compojure.core :refer [defroutes GET]]
          [compojure.route :refer [not-found]]))

(defn welcome
  "A ring handler to process all requests sent to the webapp"
  [request]
 (if (= "/" (:uri request))
    { :status 200
      :body
          "<h1>Hello, Clojure World</h1>
            <p>Welcome to your first Clojure app, I now update automatically<p>"
      :headers {}}
    {:status 404
      :body "<h1>This is not the page you are looking for</h1>
            <p>Sorry, the page you requested was not found!</p>"
      :headers {}}))

(defroutes myapp
    (GET "/" [] "Show something"))

(defn -dev-main
  "A very simple web server using Ring and Jetty  that reloads code changes via the development profile of Leiningen"
  [port-number]
  (jetty/run-jetty (wrap-reload #'welcome)
                   {:port (Integer. port-number)}))()

(defn -main
  "A very simple web server using Ring and Jetty"
  [port-number]
  (jetty/run-jetty welcome
    {:port (Integer. port-number)}))
