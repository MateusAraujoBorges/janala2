#!/bin/bash

BENCHMARKS=(
"29 tests.QSort"
"29 tests.QSortLong"
"25 tests.InsertionSort"
"121 tests.MergeSortJDK15"
"76 tests.BinaryTreeSearch"
"13 tests.HeapInsertJDK15"
"25 tests.QuickSortJDK15"
"217 tests.RedBlackTreeSearch"
"121 tests.SortedListInsert"
"206 tests.BellmanFord"
"157 tests.Dijkstra"
"20 tests.QSort"
"20 tests.QSortLong"
"20 tests.InsertionSort"
"30 tests.MergeSortJDK15"
"30 tests.BinaryTreeSearch"
"10 tests.HeapInsertJDK15"
"20 tests.QuickSortJDK15"
"30 tests.RedBlackTreeSearch"
"30 tests.SortedListInsert"
"30 tests.BellmanFord"
"30 tests.Dijkstra"
)

TARGET_DIR="results-bench"

rm -r "$TARGET_DIR"
mkdir -p "$TARGET_DIR"

for BENCH in "${BENCHMARKS[@]}"
do
	BENCH_DIR=$(echo "$BENCH" | tr ' ' '-')
	# not quoting $BENCH because we want it to be split
	# http://www.tldp.org/LDP/abs/html/quotingvar.html
	/usr/bin/time --output=time python concolic.py -c $BENCH

	mv catg_tmp/coverage/ "${TARGET_DIR}/${BENCH_DIR}/"
	mv time "${TARGET_DIR}/${BENCH_DIR}/time"
done
