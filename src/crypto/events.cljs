(ns crypto.events
  (:require
   [re-frame.core :as re-frame]
   [crypto.db :as db]
   [ajax.core :as ajax]
   [ajax.protocols]
   [day8.re-frame.http-fx]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-fx 
 :http-request           
 (fn [{db :db} _] 
   {:http-xhrio {:uri "https://www.binance.com/api/v3/ticker/price"
                 :method :get
                 :timeout 10000
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::good]
                 :on-failure [::bad]}}))

(re-frame/reg-event-db
  ::good
  (fn [db [_ response]]
    (-> db
        (assoc :data (js->clj response)))))

(re-frame/reg-event-db
 ::bad           
 (fn [db [_ response]]             
       (assoc db :failure-http-response response)))

(re-frame/reg-event-fx
 :get-time
 [(re-frame/inject-cofx :now)]
 (fn [cofx _]
  (:now cofx)))

(re-frame/reg-cofx              
   :now                
   (fn [cofx _]   
      (assoc cofx :now (js/Date.now)))) 
