(ns task2.primesWithNaturals)

(def naturals
  (lazy-seq (cons 1 (map inc naturals))))

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
  (if (<= n 1)
    []
    (reduce (fn [primes n] (if (< n 3)
                             primes
                             (sieve primes n)))
            [2]
            (take n naturals))))

(println (prime-numbers 100))