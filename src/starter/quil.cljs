;; code from http://www.quil.info/

(ns starter.quil
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn setup []
  ;; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ;; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ;; setup function returns initial state. It contains
  ;; circle color and position.
  {:color 0
   :angle 0})

(defn update-state [state]
  ;; Update sketch state by changing circle color and position.
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)})

(defn draw-state [state]
  ;; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  ;; Set circle color.
  (q/fill (:color state) 255 255)
  ;; Calculate x and y coordinates of the circle.
  (let [angle (:angle state)
        x (* 150 (q/cos angle))
        y (* 150 (q/sin angle))]
    ;; Move origin point to the center of the sketch.
    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      ;; Draw the circle.
      (q/ellipse x y 100 100))))

;;;; this function is called in index.html
(defn run-sketch []
  (q/defsketch cljs-quil
    :host "app"
    :size [500 500]
    ;;;; setup function called only once, during sketch initialization.
    :setup setup
    ;;;; update-state is called on each iteration before draw-state.
    :update update-state
    :draw draw-state
    ;;;; This sketch uses functional-mode middleware.
    ;;;; Check quil wiki for more info about middlewares and particularly
    ;;;; fun-mode.
    :middleware [m/fun-mode]))

;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (js/console.log "start")
  )

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (run-sketch)
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))
