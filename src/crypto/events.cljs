(ns crypto.events
  (:require
   [re-frame.core :as re-frame]
   [project0.db :as db]
   [ajax.core :as ajax]
   [ajax.protocols]
   [day8.re-frame.http-fx]
   [cljs-time.core :as time]
   [cljs-time.format :as tf]
   [cljs-time.coerce :as tc]))


(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-fx
 :http-request
 [(re-frame/inject-cofx :now)]
 (fn [{:keys [db now]} _]
   {:http-xhrio {:uri "https://www.binance.com/api/v3/ticker/price"
                 :method :get
                 :timeout 10000
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::good now]
                 :on-failure [::bad]}}))

(re-frame/reg-event-db
 ::good
 (fn [db [_ now response]]
   (-> db
       (assoc :data (js->clj response))
       (assoc :time now))))

(re-frame/reg-event-db
 ::bad
 (fn [db [_ response]]
   (assoc db :failure-http-response response)))

(re-frame/reg-cofx              
   :now                
   (fn [cofx _]   
      (assoc cofx :now (tf/unparse (tf/formatter "HH:mm:ss | dd.MM.yyyy") (time/to-default-time-zone (js/Date.)))))) 

