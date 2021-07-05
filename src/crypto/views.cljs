(ns crypto.views
  (:require
   [re-frame.core :as re-frame]
   [crypto.subs :as subs]
   [cljs-time.format :as tf]
   [cljs-time.core :as time]
   [cljs-time.coerce :as tc]
   [clojure.core.async :refer [go-loop]]
   [clojure.core.async :refer [<!]]
   [clojure.core.async :refer [timeout]]
   ))

(defn dispatch-request
  []
  (let [now (js/Date.)]
    (re-frame/dispatch [:http-request now])
    )) 

(defonce do-update (js/setInterval dispatch-request 5000))


(defn main-panel []
  (let [data (re-frame/subscribe [::subs/data])]
    (fn []
      [:div
      ;;  [:button {:on-click #(re-frame/dispatch [:http-request])} "Update"]
       [:h1 "Cryptocurrency page ₿ "]
       [:ul  (map #(vector :li {:key (:symbol %)} (:symbol %) " — " (:price %)  
                           
                          " (" (tf/unparse (tf/formatter "HH:mm:ss | dd.MM.yyyy") (tc/from-long (js/setInterval (re-frame/dispatch [:get-time]) 5000))) ")" 
                          " (" (tf/unparse (tf/formatter "HH:mm:ss | dd.MM.yyyy") (tc/from-long (js/Date.now))) ")" 
                           
                           )
                  (filter (comp #{"BTCUSDT" "ETHUSDT" "LTCUSDT" "XMRUSDT" "DOGEUSDT"} :symbol) @data))]])))

