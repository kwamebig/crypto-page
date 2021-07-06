(ns crypto.subs
  (:require
   [re-frame.core :as re-frame]))


(re-frame/reg-sub
 ::data
 (fn [db]
   (map (fn [item]
          [:li
           {:key (:symbol item)}
           (str (:symbol item) " — " (:price item))])
        (filter (comp #{"BTCUSDT" "ETHUSDT" "LTCUSDT" "XMRUSDT" "DOGEUSDT"} :symbol) (:data db)))))

(re-frame/reg-sub
 ::current-time
 (fn [db _]
   (:time db)))
