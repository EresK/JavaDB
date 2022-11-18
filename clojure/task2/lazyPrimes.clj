(ns task2.lazyPrimes)

; list of prime numbers
; 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, ...

(defn not-multiple? [n d] (not= 0 (mod n d)))

(defn primes-seq [s]
  (lazy-seq
    (cons (first s)
          (primes-seq (filter
                        (fn [x] (not-multiple? x (first s)))
                        (rest s))))))

(defn primes []
  (primes-seq (iterate inc 2)))

(println (time (take 1000 (primes))))
(println (time (take 1100 (primes))))
