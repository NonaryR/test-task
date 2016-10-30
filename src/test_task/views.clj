(ns test-task.views
  (:require [test-task.db :as db]
            [clojure.string :as str]
            [hiccup.page :as hic-p]))

(defn gen-page-head
  [title]
  [:head
   [:title (str "Items: " title)]
   (hic-p/include-css "/css/styles.css")])


(def header-links
  [:div#header-links
   "[ "
   [:a {:href "/"} "Home"]
   " | "
   [:a {:href "/add-item"} "Add a Item"]
   " | "
   [:a {:href "/api/items"} "View All Items"]
   " | "
   [:a {:href "/update-item"} "Update item"]
   " ]"])


(defn home-page
  []
  (hic-p/html5
   (gen-page-head "Home")
   header-links
   [:h1 "Home"]
   [:p "Test-webapp"]))


(defn all-items-page
  []
  (let [all-locs (db/get-all)]
    (hic-p/html5
     (gen-page-head "All Items in the DB")
     header-links
     [:h1 "All Items"]
     [:table
      [:tr [:th "id"] [:th "text"] [:th "next"]]
      (for [loc all-locs]
        [:tr [:td (:id loc)] [:td (:text loc)] [:td (:next loc)]])])))


(defn add-items-page
  []
  (hic-p/html5
   (gen-page-head "Add new item")
   header-links
   [:h1 "Add a item"]
   [:form {:action "/add-item" :method "POST"}
    [:p "text: " [:input {:type "text" :name "x"}]]
    [:p "next: " [:input {:type boolean :name "y"}]]
    [:p [:input {:type "submit" :value "submit item"}]]]))


(defn add-items-results-page
  [{:keys [x y]}]
  (let [id (db/insert-new-item x y)]
  (hic-p/html5
    (gen-page-head "Added a item")
    header-links
    [:h1 "Added a item"]
    [:p "Added text: " x ", next: " y" to the db. "
      [:a {:href (str "/api/items/" id)} "Click for see"]
      "."])))


(defn display-post
  [id]
  (let [post (db/post-by-id id)]
  (hic-p/html5
    (gen-page-head "Item")
    header-links
    [:h1 "See item"]
    [:p "Item: " post])))


(defn add-update-page
  []
  (hic-p/html5
   (gen-page-head "Update item")
   header-links
   [:h1 "Update item"]
   [:form {:action "/update-item" :method "POST"}
    [:p "id: " [:input {:type int :name "id"}]]
    [:p "text: " [:input {:type "text" :name "text"}]]
    [:p [:input {:type "submit" :value "update item"}]]]))


(defn add-update-results-page
  [{:keys [id text]}]
  (db/update-item id text)
  (hic-p/html5
    (gen-page-head "Update a item")
    header-links
    [:h1 "Update a item"]
    [:p "Updated text for id: " id, " to the db. "
     [:a {:href (str "/api/items/" id)} "Click for see"]"."]))
