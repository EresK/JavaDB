(ns task2.primes-tests
  (:use task2.primesWithNaturals)
  (:require [clojure.test :as test]))

(test/deftest unittests
  (test/testing "Testing prime-numbers with correct n"
    (test/is (= (prime-numbers 10) [2 3 5 7]))
    (test/is (= (prime-numbers 50) [2 3 5 7 11 13 17 19 23 29 31 37 41 43 47]))
    (test/is (= (prime-numbers 100) [2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97]))
    ))

(test/deftest unittests
  (test/testing "Testing prime-numbers with incorrect n"
    (test/is (= (prime-numbers 1) []))
    (test/is (= (prime-numbers 0) []))
    (test/is (= (prime-numbers -1) []))
    ))

(test/deftest unittests
  (test/testing "Testing sieve with prime number"
    (test/is (= (sieve [2] 3) [2 3]))
    (test/is (= (sieve [2 3 5 7] 11) [2 3 5 7 11]))
    ))

(test/deftest unittests
  (test/testing "Testing sieve with non-prime number"
    (test/is (= (sieve [2] 4) [2]))
    (test/is (= (sieve [2 3 5 7] 21) [2 3 5 7]))
    ))

(test/deftest unittests
  (test/testing "Testing multiple?"
    (test/is (= (multiple? 2 2) true))
    (test/is (= (multiple? 9 3) true))
    (test/is (= (multiple? 2 4) false))
    (test/is (= (multiple? 9 10) false))
    ))
