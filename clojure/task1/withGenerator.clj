(ns task1.withGenerator)

(defn cat-seq-alpha [sq alpha]
  (->> alpha
       (filter (fn [letter] (false? (.contains sq letter))))
       (map (fn [letter] (str sq letter)))))

(defn cat-apply [sq alpha]
  (reduce concat (map (fn [s] (cat-seq-alpha s alpha)) sq)))

(defn cat-n [n alpha]
  (cond
    (<= n 1) alpha
    (> n (count alpha)) '()
    :else  (reduce (fn [sq _] (cat-apply sq alpha)) alpha (range (dec n)))))

(def alphabet '("a" "b" "c" "d"))

(println (cat-n 2 alphabet))
