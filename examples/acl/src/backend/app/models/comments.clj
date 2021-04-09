(ns models.comments
  (:require
    [honeysql.helpers :refer [select
                              from
                              where
                              insert-into
                              delete-from
                              columns
                              values
                              sset]
     :as helpers]
    [xiana.core :as xiana])
  (:import
    (java.util
      UUID)))

(defn fetch-query
  [{{{id :id} :query-params} :request
    :as                      state}]
  (xiana/ok (assoc state :query (cond-> (-> (select :*)
                                            (from :comments))
                                  id (where [:= :id (UUID/fromString id)])))))

(defn add-query
  [{{{user-id :id} :user}                              :session-data
    {{content :content post-id :post_id} :body-params} :request
    :as                                                state}]
  (xiana/ok (assoc state :query (-> (insert-into :comments)
                                    (columns :content :post_id :user_id)
                                    (values [[content (UUID/fromString post-id) user-id]])))))

(defn update-query
  [{{{id :id} :query-params}          :request
    {{content :content} :body-params} :request
    :as                               state}]
  (xiana/ok (assoc state :query (-> (helpers/update :comments)
                                    (where [:= :id (UUID/fromString id)])
                                    (sset {:content content})))))

(defn delete-query
  [{{{id :id} :query-params} :request
    :as                      state}]
  (xiana/ok (assoc state :query (cond-> (delete-from :comments)
                                  id (where [:= :id (UUID/fromString id)])))))
