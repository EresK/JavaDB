(ns task2.lazyPrimes-tests
  (:use task2.lazyPrimes)
  (:require [clojure.test :as test]))

(test/deftest unittest
  (test/testing "Test with take"
    (test/is (= (take 10 primes) '(2 3 5 7 11 13 17 19 23 29)))
    (test/is (= (take 20 primes) '(2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71)))
    (test/is (= (take 30 primes) '(2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97 101 103 107 109 113)))
    ))

(test/deftest unittest
  (test/testing "Test with nth"
    (test/is (= (nth primes 0) 2))
    (test/is (= (nth primes 10) 31))
    ))
