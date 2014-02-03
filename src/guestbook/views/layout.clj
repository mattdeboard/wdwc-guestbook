(ns guestbook.views.layout
  (:require [hiccup.form :refer [form-to label submit-button]]
            [hiccup.page :refer [html5 include-css]]
            [noir.validation :refer [on-error]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to guestbook"]
     (include-css "/css/screen.css")]
    [:body body]))

(defn format-error [[error]]
  [:p.error error])

(defn control [field name text]
  (list (on-error name format-error)
        (label name text)
        (field name)
        [:br]))

(defn form [action button-text & controls]
  (common
   (form-to
    [:post action]
    (map #(apply control %) controls)
    (if button-text (submit-button button-text)))))
