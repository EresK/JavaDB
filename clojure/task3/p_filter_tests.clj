(ns task3.p-filter-tests
  (:use task3.parallel-filter)
  (:require [clojure.test :as test]))

(def bound 10)

(test/deftest unittest
  (test/testing "Test results"
    (test/is (= (p-filter-2 odd? (range 1 bound)) '(1 3 5 7 9)))
    (test/is (= (p-filter-2 even? (range 1 bound)) '(2 4 6 8)))
    ))
