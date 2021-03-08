(ns framework.components.acl.core
  (:require
    [xiana.core :as xiana]))

(defn has-access
  "Examine if the user is has access to a resource with the provided action.
  If it has, returns anything what is provided in 'permissions' corresponding :filter field.
  If isn't then returns \"false\"
  'permissions' is a map keyed by name of permissions.
  'user' is optional, but if it missing you must provide the 'role' field in action.
  'access' defines the role, resource and privilege what needs to be achieved.
  If user is provided, the role will be resolved from it."
  ([permissions user access]
   (cond
     (not (:is_active user)) (has-access permissions (assoc access :role :guest))
     (:is_superuser user) (has-access permissions (assoc access :role :superuser))
     (:is_staff user) (has-access permissions (assoc access :role :staff))
     (:is_active user) (has-access permissions (assoc access :role :member))))
  ([permissions {:keys [role resource privilege]}]
   (let [granted (->> (get permissions role)
                      (filter #(#{resource :all} (:resource %)))
                      first)]
     (if (some #{privilege :all} (:actions granted))
       (:filter granted)
       false))))

(def action-mapping
  {:get    :read
   :post   :update
   :put    :create
   :delete :delete})

(defn ->resource
  [uri]
  (re-find #"\w+" uri))

(defn is-allowed
  "Checks if the user is able to do an action on a resource.
  Returns xiana/ok when it is, and extends [:response-data :acl] with the restriction of ownership check.
  When the user has no access, returns xiana/error or executes ((:or-else access) state) if it's provided.
  If no 'access' has been provided, it's resolves:
  - resource from URI (/users/ -> \"users\")
  - and privilege from request method:

  |req:    | action: |
  |------- |---------|
  |:get    | :read   |
  |:post   | :update |
  |:put    | :create |
  |:delete | :delete |"
  ([{:keys [user] :as state} access]
   (let [permissions (:acl/permissions state)
         result (if (:role user)
                  (has-access permissions (assoc access :role (:role user)))
                  (has-access permissions user access))]
     (cond result (xiana/ok (assoc-in state [:response-data :acl] result))
           (:or-else access) ((:or-else access) state)
           :else (xiana/error (assoc state :response {:status 401 :body "Authorization error"})))))
  ([{:keys [user http-request] :as state}]
   (let [permissions (:acl/permissions state)
         resource (->resource (:uri http-request))
         privilege (action-mapping (:request-method http-request))
         result (has-access permissions user {:resource resource :privilege privilege})]
     (if result
       (xiana/ok (assoc-in state [:response-data :acl] result))
       (xiana/error (assoc state :response {:status 401 :body "Authorization error"}))))))
