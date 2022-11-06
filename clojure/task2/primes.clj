(ns task2.primes)

; list of prime numbers
; 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, ...

(defn multiple? [n d] (= 0 (mod n d)))

(defn sieve [primes n]
  (if (reduce (fn [f d] (or f (multiple? n d)))
        false
        primes)
    primes
    (conj primes n)))

(defn prime-numbers
  "return prime numbers which are in range from 2 to n.
  n should be greater than 1 else it will return []"
  [n]
  (cond
    (<= n 1) []
    :default (reduce sieve [2] (take (- n 2) (iterate inc 3)))))

(println (prime-numbers 100))
