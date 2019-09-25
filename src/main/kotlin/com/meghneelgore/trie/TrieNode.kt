package com.meghneelgore.trie

/**
 * This class encapsulates the node in a Trie.
 */
data class TrieNode<K, V>(
    internal val value: V? = null,
    internal val next: MutableMap<K, TrieNode<K, V>> = mutableMapOf(),
    internal var isTerminal: Boolean = false,
    internal var isTraversed: Boolean = false
) {
    internal fun contains(key: K): Boolean {
        return next.contains(key)
    }

    internal fun get(key: K): TrieNode<K, V> {
        return next[key]!! // next[key] should not be accessed if null
    }

    internal fun put(key: K, nextValue: TrieNode<K, V>): TrieNode<K, V>? {
        return next.put(key, nextValue)
    }

    override fun toString(): String {
        return "TrieNode(value=$value, next=$next, isTerminal=$isTerminal, isTraversed=$isTraversed)"
    }
}