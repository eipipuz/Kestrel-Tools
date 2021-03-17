package com.eipipuz.tree


data class Trie<T>(val children: MutableMap<T, Trie<T>>, val value: MutableList<T>, var isTerminal: Boolean) {
    companion object {
        fun createFromDictionary(dictionary: List<String>): Trie<Char> {
            val list = dictionary.map { it.toList() }

            return create(list)
        }

        fun <T> createEmpty(isTerminal: Boolean = false): Trie<T> = Trie(mutableMapOf(), mutableListOf(), isTerminal)

        @Suppress("MemberVisibilityCanBePrivate")
        fun <T> create(list: List<List<T>>): Trie<T> {
            val root = createEmpty<T>(isTerminal = list.isEmpty())
            for (word in list) {
                root.insert(word)
            }

            return root
        }
    }

    fun insert(word: List<T>) {
        if (word.isEmpty()) {
            isTerminal = true
            return
        }

        val firstElement = word.first()
        val nextTrie = children.getOrPut(firstElement) { createEmpty(isTerminal = false) }
        nextTrie.insert(word.drop(1))
    }

    fun expand(): List<List<T>> {
        val result = mutableListOf<List<T>>()

        if (isTerminal) {
            result.add(emptyList())
        }

        children.forEach { (element, trie) ->
            val commonList = listOf(element)
            val subWords = trie.expand().map {
                commonList + it
            }

            result.addAll(subWords)
        }

        return result
    }

    fun find(word: List<T>): Boolean {
        var currentTrie = this
        for (element in word) {
            currentTrie = currentTrie.children[element] ?: return false
        }

        return currentTrie.isTerminal
    }

    /**
     * @return whether the Trie should be deleted
     */
    fun remove(word: List<T>): Boolean {
        if (word.isEmpty()) {
            return if (isTerminal) {
                isTerminal = false

                children.isEmpty() // Can be deleted
            } else {
                throw UnknownWordException
            }
        }
        // Loop through the word
        val element = word.first()
        val nextTrie = children[element] ?: throw UnknownWordException

        val tail = word.subList(1, word.size)
        val canDelete = nextTrie.remove(tail)

        return if (canDelete) {
            children.remove(element)

            children.isEmpty() // Can be deleted
        } else {
            false
        }
    }

    object UnknownWordException : IllegalStateException("Trying to delete a word that doesn't exist")
}

fun Trie<Char>.toDictionary(): List<String> = expand().map { it.joinToString("") }

fun Trie<Char>.find(string: String): Boolean = find(string.toList())

fun Trie<Char>.insert(string: String) {
    insert(string.toList())
}

fun Trie<Char>.remove(string: String) {
    remove(string.toList())
}

