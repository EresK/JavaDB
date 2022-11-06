(ns task1.code)

(defn get-last-char [s]
  (cond
    (= (count s) 0) ""
    :default (subs s (- (count s) 1))))

(defn cat-seq-alpha' [sq ch alpha]
  (->> alpha
       (filter (fn [letter] (not= ch letter)))
       (map (fn [letter] (str sq letter)))))

(defn cat-seq-alpha [sq alpha]
  (cat-seq-alpha' sq (get-last-char sq) alpha))

(defn cat-apply [sq alpha]
  (reduce concat (map (fn [s] (cat-seq-alpha s alpha)) sq)))

(defn cat-n [n alpha]
  (cond
    (<= n 1) alpha
    :default  (reduce (fn [sq _] (cat-apply sq alpha)) alpha (range (dec n)))))

(def alphabet '("a" "b" "c" "d"))

(println (cat-n 3 alphabet))
(println (count (cat-n 3 alphabet)))