(ns task3.parallel-filter)

(defn heavy-odd? [n]
  (Thread/sleep 1)
  (odd? n))

(defn p-filter [pred coll]
  (let [chunk-size (int (Math/ceil (Math/sqrt (count coll)))),
        parts (partition-all chunk-size coll)]
    (->> parts
         (map (fn [p] (future (doall (filter pred p)))))
         (doall)
         (map deref)
         (reduce concat))))

(defn p-filter-lazy [pred coll]
  (lazy-seq
    (concat (p-filter pred (take 100 coll))
            (p-filter-lazy pred (nthrest coll 100)))))

(def upper-bound 1000)

(def last-elem (- (/ upper-bound 2) 1))

;(println "EAGER odd?")
;(time (nth (filter odd? (range 1 upper-bound)) last-elem))
;(time (nth (p-filter odd? (range 1 upper-bound)) last-elem))
;
;(println "EAGER heavy-odd?")
;(time (nth (filter heavy-odd? (range 1 upper-bound)) last-elem))
;(time (nth (p-filter heavy-odd? (range 1 upper-bound)) last-elem))
;
;(println "LAZY")
;(time (nth (filter heavy-odd? (range)) last-elem))
;(time (nth (p-filter-lazy heavy-odd? (range)) last-elem))
