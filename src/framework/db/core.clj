(ns framework.db.core
  (:require
   [next.jdbc :as jdbc]
   [framework.config.core :as config]))

;; database instance reference
(defonce db (atom {}))

(defn- make
  "Return database instance map."
  [spec]
  (let [datasource (jdbc/get-datasource spec)]
    {:datasource datasource
     :connection (try
                   (jdbc/get-connection datasource)
                   (catch Exception _ nil))}))

(defn start
  "Start database instance."
  [db-spec]
  (when-let [spec (or db-spec (config/get-spec :database))]
    (swap! db
           (fn [m]
             (merge m (make spec))))))

(defn connection
  "Get (or start) database connection."
  []
  ;; start the database instance (if necessary)
  (when (or
         ;; empty structure reference?
         (empty @db)
         ;; not connected?
         (not (:connection @db)))
    ;; tries to start the database connection
    (start))
  ;; return the connection or nil which means:
  ;; connection not established
  (:connection @db))
