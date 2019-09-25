package com.meghneelgore.trie.examples.dictionary

import com.meghneelgore.trie.Trie

/**
 * Example class to show the word finding capabilities of a Trie. Enter an empty string to stop.
 */
class FindWord {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val trie = Trie.trieFromFilename("wordlist.txt")
            while (true) {
                print("Enter word to find: ")
                val enteredString = readLine()
                enteredString ?: break
                if (enteredString == "") break
                val found = trie.isWordInDictionary(enteredString)
                val existenceString = if (found) "exists" else "does not exist"
                println("The word $enteredString $existenceString")
            }
        }
    }
}
