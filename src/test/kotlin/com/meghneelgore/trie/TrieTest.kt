package com.meghneelgore.trie

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TrieTest {
    @Test
    fun testTraverseTrie() {
        val trie = Trie.trieOf(listOf("a", "b", "c", "aa", "bb", "cc", "aaa"))
        trie.traverseTrie(trie.root, mutableListOf('a', 'a', 'a'))
        assertTrue("Wrong nodes traversed", trie.root.next['a']?.isTraversed ?: false)
        assertTrue("Wrong nodes traversed", trie.root.next['a']?.next?.get('a')?.isTraversed ?: false)
        assertTrue("Wrong nodes traversed", trie.root.next['a']?.next?.get('a')?.next?.get('a')?.isTraversed ?: false)
        assertFalse("Wrong nodes traversed", trie.root.next['b']?.isTraversed ?: false)
    }

    @Test
    fun testResetTraversal() {
        val trie = Trie.trieOf(listOf("a", "b", "c", "aa", "bb", "cc", "aaa"))
        trie.traverseTrie(trie.root, mutableListOf('a', 'a', 'a'))
        trie.resetTraversal(trie.root)
        assertAllTraversalsAreFalse(trie.root)
    }

    private fun assertAllTraversalsAreFalse(root: TrieNode<Char, String>) {
        for (node in root.next.values) {
            assertFalse("Traversal not reversed", node.isTraversed)
            assertAllTraversalsAreFalse(node)
        }
    }
}