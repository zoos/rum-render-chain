(require 'boot.repl)

;; For emacs cider
(swap! boot.repl/*default-dependencies* conj
       '[refactor-nrepl "1.1.0"]
       '[cider/cider-nrepl "0.9.1"])

(swap! boot.repl/*default-middleware* conj
       conj 'cider.nrepl/cider-middleware
       'refactor-nrepl.middleware/wrap-refactor)

(set-env!
 :source-paths   #{"src"}
 :resource-paths #{"html"}
 :dependencies '[
                 [org.clojure/clojure       "1.7.0" :scope "provided"]
                 [org.clojure/clojurescript "1.7.170" :scope "provided"]
                 
                 [adzerk/boot-cljs   "1.7.170-3" :scope "test"]
                 [adzerk/boot-reload "0.4.2" :scope "test"]
                 [adzerk/boot-cljs-repl "0.2.0"]
                 [pandeiro/boot-http "0.7.1-SNAPSHOT" :scope "test"]

                 [rum "0.5.0" :scope "test"]])

(require
 '[adzerk.boot-cljs   :refer [cljs]]
 '[adzerk.boot-reload :refer [reload]]
 '[pandeiro.boot-http :refer [serve]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]])

(def dev-cljs-opts
  {:optimizations    :none
   :source-map       true
   :compiler-options {:warnings {:single-segment-namespace false}}})

(deftask dev [] 
  (comp (do (task-options! cljs dev-cljs-opts) identity)
        (serve :dir "target/")
        (watch)
        (speak)
        (cljs-repl)
        (cljs)
        (reload)))
