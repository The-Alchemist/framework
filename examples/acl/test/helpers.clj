(ns helpers
  (:require
    [clj-http.client :refer [request]]
    [clojure.data.json :refer [write-str]]))

(def test_member "611d7f8a-456d-4f3c-802d-4d869dcd89bf")
(def test_admin "b651939c-96e6-4fbb-88fb-299e728e21c8")
(def test_suspended_admin "b01fae53-d742-4990-ac01-edadeb4f2e8f")
(def test_staff "75c0d9b2-2c23-41a7-93a1-d1b716cdfa6c")

(defn delete
  ([uri]
   (-> {:url                  (format "http://localhost:3333/%s" (name uri))
        :headers              {"Authorization" test_admin}
        :unexceptional-status (constantly true)
        :method               :delete}
       request))
  ([uri user id]
   (-> {:url                  (format "http://localhost:3333/%s" (name uri))
        :headers              {"Authorization" user}
        :query-params         {:id id}
        :unexceptional-status (constantly true)
        :method               :delete}
       request)))

(defn fetch
  ([uri user id]
   (-> {:url                  (format "http://localhost:3333/%s" (name uri))
        :headers              {"Authorization" user}
        :unexceptional-status (constantly true)
        :query-params         {:id id}
        :method               :get}
       request))
  ([uri user]
   (-> {:url                  (format "http://localhost:3333/%s" (name uri))
        :headers              {"Authorization" user}
        :unexceptional-status (constantly true)
        :method               :get}
       request))
  ([uri]
   (-> {:url                  (format "http://localhost:3333/%s" (name uri))
        :headers              {"Authorization" test_admin}
        :unexceptional-status (constantly true)
        :method               :get}
       request)))

(defn put
  ([uri content]
   (-> {:url                  (format "http://localhost:3333/%s" (name uri))
        :headers              {"Authorization" test_admin
                               "Content-Type" "application/json;charset=utf-8"}
        :unexceptional-status (constantly true)
        :body                 (write-str content)
        :method               :put}
       request))
  ([uri user content]
   (-> {:url                  (format "http://localhost:3333/%s" (name uri))
        :headers              {"Authorization" user
                               "Content-Type" "application/json;charset=utf-8"}
        :unexceptional-status (constantly true)
        :body                 (write-str content)
        :method               :put}
       request)))

(defn post
  [uri user id content]
  (-> {:url                  (format "http://localhost:3333/%s" (name uri))
       :headers              {"Authorization" user
                              "Content-Type" "application/json;charset=utf-8"}
       :unexceptional-status (constantly true)
       :body                 (write-str content)
       :query-params         {:id id}
       :method               :post}
      request))

