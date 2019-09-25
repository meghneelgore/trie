package com.meghneelgore.trie.examples.jumbledwords

import com.meghneelgore.trie.Trie


/**
 * An example class to leverage the capability of the Trie class to find all words in a provided word list that can
 * be made using a provided list of characters. Enter an empty string to stop.
 */
class JumbledWordsFinder {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val trie = Trie.trieFromFilename("wordlist.txt", minLength = 3, maxLength = 6)
            while (true) {
                print("Enter jumble: ")
                val enteredString = readLine()
                enteredString ?: continue
                if (enteredString == "") break
                val foundWords = trie.findAllWordsFromCharacters(enteredString).sorted().sortedBy { it.length }
                for (word in foundWords) println("${word.length}: $word")
            }
        }
    }
}