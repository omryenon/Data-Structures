# Data-Structures
Implementation of 2 types of trees:

B-Tree:
    * In this implementation I used left before right. The meaning of that is in shift,
    try the left brother first and then the right one (if needed). In addition, call
    predecessor before the successor.
    * There are 2 types of insertions:
        1-pass: During the insertion any node in the path is splat if found full
        2-pass: Only nodes which should be splat are really splat (lazy insertion)
