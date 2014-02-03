(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [guestbook.views.layout :as layout]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [noir.validation :refer [rule errors? has-value? on-error]]
            [hiccup.form :refer [text-field password-field]]))

(defn registration-page []
  (layout/form "/register" "Create Account"
               [text-field :username "Username"]
               [password-field :pass "Password"]
               [password-field :pass1 "Retype Password"]))

(defn handle-registration [username pass pass1]
  (rule (has-value? username) [:username "Username is required"])
  (rule (has-value? pass) [:pass "Password is required"])
  (rule (= pass pass1) [:pass1 "Passwords must match"])
  (if (errors? :username :pass :pass1)
    (registration-page)
    (do (session/put! :user username)
        (redirect "/"))))

(defn login-page []
  (layout/form "/login" "Login"
               [text-field :username "Username"]
               [password-field :pass "Password"]))

(defn handle-login [username pass]
  (rule (has-value? username) [:username "Username is required"])
  (rule (= username "foo") [:username "Unknown user"])
  (rule (has-value? pass) [:pass "Password is required"])
  (rule (= pass "bar") [:pass "Invalid Password"])
  (if (errors? :username :pass)
    (login-page)
    (do (session/put! :user username)
        (redirect "/"))))

(defroutes auth-routes
  (GET "/register" [_] (registration-page))
  (POST "/register" [username pass pass1]
        (handle-registration username pass pass1))
  (GET "/login" [] (login-page))
  (POST "/login" [username pass] (handle-login username pass))
  (GET "/logout" [] (layout/form "/logout" "Logout"))
  (POST "/logout" [] (session/clear!) (redirect "/")))
