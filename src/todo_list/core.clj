(ns todo-list.core
 (:require [ring.adapter.jetty :as jetty]
          [ring.middleware.reload :refer [wrap-reload]]
          [compojure.core :refer [defroutes GET]]
          [compojure.route :refer [not-found]]
          [compojure.coercions :refer [as-int]]
          [ring.handler.dump :refer [handle-dump]]
          [clojure.core.strint :refer [<<]]))

(defn welcome
  "A ring handler to respond with a simple welcome message"
  [request]
  { :status 200
    :body
        "<h1>Hello, Clojure World</h1>
        <p>Welcome to your first Clojure app, I now update automatically<p>
        <p>I now use defroutes to manage incoming requests</p>"
    :headers {}})

(defn goodbye
  "A song to wish you goodbye"
  [request]
  {:status 200
   :body "<h1>Walking back to happiness</h1>
          <p>Walking back to happiness with you</p>
          <p>Said, Farewell to loneliness I knew</p>
          <p>Laid aside foolish pride</p>
          <p>Learnt the truth from tears I cried</p>"
   :headers {}})

(defn about
  "this is a page about the website"
  [request]
  {:status 200
    :body "<h1>My way if you wanna take it there</h1>"
    :headers {}})

(defn hello
  "A customized hello message"
  [name]
  {:status 200
    :body (<< "<h1>Hello ~{name}. I got your name from the web URL</h1>")
    :headers {}})

(def operands {"+" + "-" - "*" * ":" /})

(defn calculator
  "A very simple calculator that can add, divide, subtract and multiply.  This is done through the magic of variable path elements."
  [a b op]
  (let [f  (get operands op)]
    (if f
      {:status 200
       :body (str "Calculated result: " (f a b))
       :headers {}}
      {:status 404
       :body "Sorry, unknown operator.  I only recognise + - * : (: is for division)"
       :headers {}})))

(defroutes app
    (GET "/" [] welcome)
    (GET "/goodbye" [] goodbye)
    (GET "/about" [] about)
    (GET "/hello/:name" [name] (hello name))
    (GET "/calculator/:a/:op/:b" [a :<< as-int b :<< as-int op] (calculator a b op))
    (GET "/request-info" [] handle-dump)
    (not-found "<h1>This is not the page you are looking for</h1>
                  <p>Sorry, the page you requested was not found!</p>"))

(defn -dev-main
  "A very simple web server using Ring and Jetty  that reloads code changes via the development profile of Leiningen"
  [port-number]
  (jetty/run-jetty (wrap-reload #'app)
                   {:port (Integer. port-number)}))()

(defn -main
  "A very simple web server using Ring and Jetty"
  [port-number]
  (jetty/run-jetty app
    {:port (Integer. port-number)}))
