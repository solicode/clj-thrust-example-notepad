(ns clj-thrust-example-notepad.core
  (:require [clj-thrust-example-notepad.server :as server]
            [clj-thrust.core :refer [create-process destroy-process]]
            [clj-thrust.window :as w])
  (:gen-class))

(defonce server nil)

(defn start-server []
  "Starts the server. If one is already running, it will be restarted."
  (alter-var-root #'server
    (fn [prev]
      (when prev (server/stop server))
      (server/start))))

(defn open-window []
  (let [process (create-process)
        window (w/create-window process
                 :root-url "http://localhost:7171"
                 :size {:width 500 :height 400}
                 :title "Notepad"
                 :has-frame true)]
    (w/listen-closed window
      (fn [e]
        (destroy-process process)
        ; Note: You can call `(System/exit 0)` here if you'd like closing the window to exit
        ; the program entirely.
        ))

    (w/listen-focus window
      (fn [e]
        (println "Window gained focus")))

    (w/show window)
    (w/focus window true)))

(defn -main [& [command]]
  (start-server)

  (when (not= command "headless")
    (open-window)))