# Coursera-Algorithm-II

Java solutions to the five programming assignments of **[Algorithms, Part II](https://www.coursera.org/learn/algorithms-part2)** (Princeton University, Sedgewick & Wayne, on Coursera).

## Assignments

| # | Assignment | Topic | Files |
| --- | --- | --- | --- |
| 1 | **WordNet** | Digraphs: shortest ancestral paths in the WordNet hypernym DAG, semantic distance, and outcast detection | `WordNet.java`, `SAP.java`, `Outcast.java` |
| 2 | **Seam Carving** | Shortest paths in edge-weighted DAGs: content-aware image resizing via minimum-energy seam removal | `SeamCarver.java` |
| 3 | **Baseball Elimination** | Maxflow/mincut: deciding which teams are mathematically eliminated via a flow network over remaining games | `BaseballElimination.java` |
| 4 | **Boggle** | Tries: finding all valid words on a Boggle board with a prefix-pruned trie over the dictionary | `BoggleSolver.java` |
| 5 | **Burrows–Wheeler** | String sorting: full data-compression pipeline — circular suffix array, Burrows–Wheeler transform and inverse, and move-to-front coding | `CircularSuffixArray.java`, `BurrowsWheeler.java`, `MoveToFront.java` |

Assignment 5 includes small test inputs (`abra.txt` and a sample image) with their move-to-front-encoded outputs.

## Notes

All assignments depend on the course's [algs4](https://github.com/kevin-wayne/algs4) library (`edu.princeton.cs.algs4`); add `algs4.jar` to the classpath to compile and run. Each assignment follows the official API specification, so the solutions can be checked against the course autograder.

If you are currently taking the course, please attempt the assignments yourself before reading the solutions, per the Coursera honor code.
