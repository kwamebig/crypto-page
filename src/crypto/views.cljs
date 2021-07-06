(ns crypto.views
  (:require
   [re-frame.core :as re-frame]
   [crypto.subs :as subs]))

(defn main-panel []
  (let [data (re-frame/subscribe [::subs/data])
        current-time (re-frame/subscribe [::subs/current-time])]
    (fn []
      [:div
       [:h1 "Cryptocurrency page ₿"]
       [:ul @data]
       [:sub "As of " @current-time]
       ])))
