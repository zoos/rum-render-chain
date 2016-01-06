(ns core
  (:require [rum.core :as rum]))

(defn get-time []
  (. (js/Date.) getTime))

(defn sleep [msec]
  (let [start-time (get-time)]
    (while (>= msec (- (get-time) start-time)))))

(def splitter (reduce str (repeat 40 "-")))

(defn debug-watch [state]
  (add-watch state :debug-watch
             (fn [key atom old new]
               (println splitter)
               (println (get-time))
               (println "old:")
               (println old)
               (println "new:")
               (println new)
               (println splitter)))
  state)

(enable-console-print!)

(def message-source (atom "hello"))

(debug-watch message-source)
(defn on-click []
  ;; not work!!!visible only last state (line 34)
  (reset! message-source "please wait...")
  (sleep 2000)
  (reset! message-source (str "result " (get-time))))

(rum/defc message < rum/cursored rum/cursored-watch [message-source]
  [:div
   [:a {:href "#" :on-click on-click} "click me"]
   [:p @message-source]])

(def cc (rum/mount (message message-source) js/document.body))

