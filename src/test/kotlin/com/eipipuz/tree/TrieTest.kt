package com.eipipuz.tree

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class TrieTest {
    private val sentDictionary = listOf("romane", "romanus", "romulus", "rubens", "rubicon", "rubicundus")

    @Test
    fun testCreate() {
        val trie = Trie.createFromDictionary(sentDictionary)
        val gotDictionary = trie.toDictionary()

        assertEquals(sentDictionary, gotDictionary)
    }

    @Test
    fun testInsert() {
        val trie = Trie.createEmpty<Char>()

        sentDictionary.forEach {
            trie.insert(it)
        }

        assertEquals(sentDictionary, trie.toDictionary())
    }

    @Test
    fun testFind() {
        val trie = Trie.createFromDictionary(sentDictionary)

        assertTrue(trie.find("romulus"))
        assertFalse(trie.find("romulu"))
        assertFalse(trie.find("roma"))
        assertFalse(trie.find("romanes"))
        assertFalse(trie.find("Romane"))
    }

    private fun assertUnknownWordThrown(trie: Trie<Char>, string: String) {
        val gotException = try {
            trie.remove(string)

            false
        } catch (unknownWordException: Trie.UnknownWordException) {
            true
        }

        assertTrue(gotException)
    }

    @Test
    fun testDelete() {
        val trie = Trie.createFromDictionary(sentDictionary)

        assertUnknownWordThrown(trie, "")
        assertUnknownWordThrown(trie, "does not exist")

        trie.remove("romulus")
        assertFalse(trie.find("romulus"))
    }
}