(ns task3.parallel-filter)

(defn heavy-odd? [n]
  (Thread/sleep 1)
  (odd? n))

(defn p-filter-2 [pred coll]
  (let [chunk-size (int (Math/ceil (Math/sqrt (count coll)))),
        parts (partition-all chunk-size coll)]
    (->> parts
         (map (fn [p] (future (filter pred p))))
         (doall)
         (map deref)
         (reduce concat))))

(def upper-bound 1000)

(println "odd?")
(time (nth (filter odd? (range 1 upper-bound)) (- (/ upper-bound 2) 1)))
(time (p-filter-2 odd? (range 1 upper-bound)))

(println "heavy-odd?")
(time (nth (filter heavy-odd? (range 1 upper-bound)) (- (/ upper-bound 2) 1)))
(time (p-filter-2 heavy-odd? (range 1 upper-bound)))
