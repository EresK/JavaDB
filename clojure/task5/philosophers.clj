(ns task5.philosophers)

(defn philosopher [id num forks thinking dining restarts]
  (while (= 1 1)
    (do (dosync (swap! restarts inc)
                (if (= id 0)
                  ; first philosopher
                  (do (alter (nth forks id) inc)
                      (alter (nth forks (- num 1)) inc))
                  ; others
                  (do (alter (nth forks (- id 1)) inc)
                      (alter (nth forks id) inc)))
                (printf "%d: is dining%n   restarts: %d%n" id @restarts)
                (reset! restarts 0)
                (Thread/sleep dining))
        (printf "%d: is thinking%n" id)
        (Thread/sleep thinking))))

(defn simulate [num forks thinking dining restarts-atoms]
  (do (let [ids (range 0 num)]
        (->> ids
             (map (fn [id] (future (philosopher id num forks thinking dining (nth restarts-atoms id)))))
             (doall)))
      (while (= 1 1)
        (do (run! println (map deref forks))
            (Thread/sleep 2000)))))

; philosophers number
(def p-num 5)
(def forks (map ref (take p-num (iterate identity 0))))
(def restarts-atoms (map atom (take p-num (iterate identity 0))))
(def thinking (rand-int 500))
(def dining (rand-int 500))

(simulate p-num forks thinking dining restarts-atoms)

