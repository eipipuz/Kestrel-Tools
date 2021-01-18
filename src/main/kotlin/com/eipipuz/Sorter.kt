package com.eipipuz


object Sorter {
    private fun <T : Comparable<T>> findPivot(list: List<T>): T {
        val start = list.first()
        val end = list.last()
        val middleIndex: Int = list.size / 2
        val middle = list[middleIndex]

        return if (start <= end) {
            if (start <= middle) {
                if (middle <= end) middle else end
            } else {
                start
            }
        } else {
            if (end <= middle) {
                if (middle <= start) middle else start
            } else {
                end
            }
        }
    }

    fun <T : Comparable<T>> quickSort(collection: Collection<T>): List<T> {
        val input = collection.toList()
        if (collection.size <= 1) return input

        val pivot = findPivot(input)

        val lessThanList = mutableListOf<T>()
        val equalThanList = mutableListOf<T>()
        val moreThanList = mutableListOf<T>()

        input.forEach {
            when (it.compareTo(pivot)) {
                -1 -> lessThanList.add(it)
                0 -> equalThanList.add(it)
                1 -> moreThanList.add(it)
            }
        }

        val lessThanSorted = quickSort(lessThanList)
        val moreThanSorted = quickSort(moreThanList)

        return lessThanSorted + equalThanList + moreThanSorted
    }

    fun <T> mergeSort(collection: Collection<T>): List<T> = TODO()

    fun <T> heapSort(collection: Collection<T>): List<T> = TODO()

    fun <T : Comparable<T>> insertSort(collection: Collection<T>): List<T> {
        val mutableList = collection.toMutableList()
        if (collection.size <= 1) return mutableList

        var previousValue = mutableList.first()
        repeat(mutableList.size) { index ->
            val nextValue = mutableList[index]
            if (nextValue < previousValue) {
                val (shouldBeIndex, _) = Searcher.binarySearchIndex(mutableList.subList(0, index), nextValue)

                mutableList.removeAt(index)
                mutableList.add(shouldBeIndex, nextValue)
            }
            previousValue = nextValue
        }

        return mutableList
    }
}