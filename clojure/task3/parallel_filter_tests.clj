(ns task3.parallel-filter-tests
  (:use task3.parallel-filter)
  (:require [clojure.test :as test]))

(def bound 10)

(test/deftest unittest
  (test/testing "Test results"
    (test/is (= (p-filter odd? (range 1 bound)) '(1 3 5 7 9)))
    (test/is (= (p-filter even? (range 1 bound)) '(2 4 6 8)))
    ))

(test/deftest unittest
  (test/testing "Test with lazy seq"
    (test/is (= (nth (p-filter-lazy odd? (range)) 5) 11))
    (test/is (= (nth (p-filter-lazy even? (range)) 5) 10))
    ))
