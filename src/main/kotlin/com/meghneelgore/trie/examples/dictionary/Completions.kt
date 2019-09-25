package com.meghneelgore.trie.examples.dictionary

import com.meghneelgore.trie.Trie


/**
 * Example class for showing the capability of a Trie to find completions. Enter an empty string to stop.
 */
class Completions {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val trie = Trie.trieFromFilename("wordlist.txt")
            while (true) {
                print("Enter word to complete: ")
                val enteredString = readLine()
                enteredString ?: break
                if (enteredString == "") break
                val completions =
                    trie.findCompletionsInDictionary(enteredString).sorted().sortedBy { it.length }
                completions.forEachIndexed { index, element -> println("$index: $element") }
            }
        }
    }
}