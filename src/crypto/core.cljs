(ns crypto.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [crypto.events :as events]
   [crypto.views :as views]
   [crypto.config :as config]
   [clojure.core.async :refer [go-loop <! timeout]]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root)
  (go-loop [seconds 0]
        (<! (timeout 3000))
        (re-frame/dispatch [:http-request])
        (recur (+ seconds 3))))
