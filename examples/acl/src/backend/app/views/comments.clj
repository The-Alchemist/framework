(ns views.comments
  (:require
    [views.common :as common]
    [xiana.core :as xiana]))

(defn ->comment-view
  [m]
  (select-keys m [:comments/id
                  :comments/post_id
                  :comments/user_id
                  :comments/content
                  :comments/creation_time]))

(defn comments
  [{response-data :response-data :as state}]
  (xiana/ok (common/response state {:view-type "comments"
                                    :data      {:comments (->> response-data
                                                               :db-data
                                                               (map ->comment-view))}})))
