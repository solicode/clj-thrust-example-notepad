(ns clj-thrust-example-notepad.core
  (:require [dommy.core :as d :refer-macros [sel sel1]]
            [ajax.core :refer [GET POST]]))

; Let's make FileList ISeqable so we can use the standard Clojure core
; functions on it.
(extend-type js/FileList
  ISeqable
  (-seq [array] (array-seq array 0)))

; Here we're handling the reading of the file on the client side. This
; could easily be done on the server side as well. We're just showing
; both techniques in this sample project for learning purposes.
(defn read-file [file on-load]
  (let [reader (js/FileReader.)]
    (set! (.-onload reader) on-load)
    (.readAsText reader file)))

(defn set-char-count! [char-count]
  (d/set-text! (sel1 :#characterCount) char-count))

(let [inputTextArea (sel1 :#inputTextArea)]
  (d/listen! (sel1 :#open) :click
    (fn [e] (.click (sel1 :#openFile))))

  (d/listen! (sel1 :#openFile) :change
    (fn [e]
      (let [file (first (aget e "target" "files"))]
        (read-file file
          (fn [e]
            (let [content (aget e "target" "result")]
              (d/set-value! inputTextArea content)
              (set-char-count! (.-length content))))))))

  ; I would consider learning how to use core.async as it can be
  ; used in cases like this. You can avoid having to nest callbacks like
  ; this, which can make reasoning about your code easier.
  (d/listen! (sel1 :#save) :click
    (fn [e]
      (GET "/save-dialog" :handler
        (fn [path]
          (when-not (empty? path)
            ; Contrast this to the way we're reading files (which is being done on the client side),
            ; here we are savings files on the server side.
            (POST "/save" {:params {:path path
                                    :text (d/value (sel1 :#inputTextArea))}
                           :format :url}))))))

  (doseq [eventType [:keyup :change :paste]]
    (d/listen! inputTextArea eventType
      (fn [e]
        (set-char-count! (aget e "target" "textLength"))))))
