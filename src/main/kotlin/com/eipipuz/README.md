# Goal

This is a place for me to _learn_ Data Structures and Algorithms.

## Priorities

### High
1. Correct — Everything should be highly tested.
1. Readable — We should be able to understand the logic even if years pass since we read it.
1. Repeatable — We should be able to read the code once and then re-implement from scratch.

### Medium
A fast implementation is more important than a lean one.

1. Generic — If X can use _generics_ do so.
1. Optimal in time
1. Optimal in memory

### Low priority
1. Complete — If variants are interesting enough, we should add them.
1. Dog-food — If we have a version we should use it.

# Data Structures

## Heap
There's both MaxHeap and MinHeap. Decided against having only 1 class with a way to pick modes
because that would interfere with __Readable__ for now.

## Stack

## Queue

## MultiSet

## DisjointSet
* Find halves path. This means it isn't eager to move everyone.
* Union is done per rank. This means not tracking exact size.

## Graph

# Algorithms

## Sort

### InsertSort

### QuickSort

### MergeSort

### HeapSort

### Topological
At this time we are using a MaxHeap instead of a general Queue.
Using a Queue returns values in an almost arbitrary way.
Also, this way we could have a job schedule thing with priorities.

## Search

### Quick Select

### Breath-First

### Depth-First

### Longest Common Sublist

### Minimum Spanning Tree
At this point, we only have Prim's algorithm. Picked over Kruskal's because it seems more natural.

## Back Trace

# Problems

## Eight Queens