(ns framework.acl.builder.roles
  (:require
    [framework.acl.builder.builder-functions :as b]
    [xiana.core :as xiana]))

(defn init
  ([state]
   (if (:acl/roles state)
     (xiana/ok state)
     (init state {})))
  ([state roles-map]
   (xiana/ok (assoc state :acl/roles roles-map))))

(defn allow
  [{ap :acl/available-permissions ar :acl/roles :as state} permission]
  (xiana/ok (assoc state :acl/roles (if ap
                                      (b/allow ar ap permission)
                                      (b/allow ar permission)))))

(defn deny
  [{ap :acl/available-permissions ar :acl/roles :as state} permission]
  (xiana/ok (assoc state :acl/roles (if ap
                                      (b/deny ar ap permission)
                                      (b/deny ar {} permission)))))
