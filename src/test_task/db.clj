(ns test-task.db
  (:require [korma.db :refer [defdb]]
            [korma.core :refer :all]
            [clojure.string :as str]
            [clojure.java.jdbc :as sql]))


(def data
  [{:id "0" :text "first"   :next false}
   {:id "1" :text "second"  :next true}
   {:id "2" :text "third"   :next false}
   {:id "3" :text "fourth"  :next false}
   {:id "4" :text "fifth"   :next true}
   {:id "5" :text "sixth"   :next false}
   {:id "6" :text "seventh" :next false}
   {:id "7" :text "eighth"  :next true}])


(def dbspec {:classname "org.h2.Driver"
             :subprotocol "h2"
             :subname "test.db;DATABASE_TO_UPPER=false;"})


(defdb mydb dbspec)

(exec-raw (str "CREATE TABLE IF NOT EXISTS items ("
               "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
               "text VARCHAR NOT NULL,"
               "next BOOLEAN NOT NULL"
               ");"))

(defentity items)

;; (insert items (values data))

(defn get-all
  []
  (into [] (select items)))

(defn post-by-id
  [id]
  (first (select items
            (where (= :id id)))))

;; (post-by-id 1)


(defn update-item
  [id body]
  (if (post-by-id id)
    (update items
      (set-fields {:text body})
      (where (= :id id)))))

; ;(update-item 4 "lolll")

(defn insert-new-item
  [x y]
  (first (vals (insert items (values {:text x :next y})))))
