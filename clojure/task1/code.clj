(ns task1.code)

(defn cat-seq-alpha [sq alpha]
  (->> alpha
       (filter (fn [letter] (false? (.contains sq letter))))
       (map (fn [letter] (str sq letter)))))

(defn cat-apply [sq alpha]
  (reduce concat (map (fn [s] (cat-seq-alpha s alpha)) sq)))

(defn cat-n' [n sq alpha]
  (if (<= n 1)
    sq
    (cat-n' (dec n) (cat-apply sq alpha) alpha)))

(defn cat-n [n alpha]
  (if (<= n 1)
    alpha
    (cat-n' n alpha alpha)))

(def alphabet '("a" "b" "c" "d"))

(println (cat-apply alphabet alphabet))
(println (cat-apply (cat-apply alphabet alphabet) alphabet))
(println (cat-apply (cat-apply (cat-apply alphabet alphabet) alphabet) alphabet))
(println (cat-n 1 alphabet))