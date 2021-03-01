package com.eipipuz


class DisjointSet<T> private constructor(val exemplar: T, private var rank: Int) {
    companion object {
        fun <T> create(value: T): DisjointSet<T> {
            val disjointSet = DisjointSet(value, 0)
            disjointSet.parent = disjointSet

            return disjointSet
        }
    }

    private lateinit var parent: DisjointSet<T>

    fun findParent(): DisjointSet<T> {
        var root = this
        while (root.parent != root) {
            root.parent = root.parent.parent
            root = root.parent
        }

        return root
    }

    fun addValue(value: T): DisjointSet<T> {
        val child = create(value)

        return union(child)
    }

    fun union(otherDisjointSet: DisjointSet<T>): DisjointSet<T> {
        val root = findParent()
        val otherRoot = otherDisjointSet.findParent()

        if (root == otherRoot) return root

        val (smallerRoot, biggerRoot) = Swapper.findSmallAndBig(root, otherRoot) { ds1, ds2 -> ds1.rank < ds2.rank }

        smallerRoot.parent = biggerRoot // Keeps the trees short.
        if (biggerRoot.rank == smallerRoot.rank) {
            biggerRoot.rank++
        }

        return biggerRoot
    }
}