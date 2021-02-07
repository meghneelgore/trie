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
            val fileName = "englishwordswithfrequency.txt"
            val trie =
                Trie.trieWithFrequencyFromFilename(fileName, minLength = 3, maxLength = 9)
            while (true) {

                print("Enter jumble: ")
                val enteredString = readLine()
                enteredString ?: continue
                if (enteredString == "") break

                val foundWords = trie.findAllWordsFromCharactersWithFrequency(enteredString)
                    .sortedBy { it.first }
                    .sortedBy { it.first.length }
                prettyPrintWithFrequencies(foundWords)
                var wordSayer: WordSayer? = null
                wordSayer = WordSayer(foundWords.filter { it.second > 0 }.map { it.first })
                wordSayer.start()
                while (true) {
                    val input = readLine()
                    if (input.isNullOrBlank()) {
                        wordSayer.stop()
                        // Clear screen. Will not work in IntelliJ's Run console. Will only work on linux and mac
                        // and (maybe) cygwin.
                        print("\u001b[H\u001b[2J")
                        Trie.saveTrieWithFrequenciesToFile(trie, fileName)
                        break
                    } else {
                        input.split(" ").listIterator().forEach { trie.setEncountered(it.trim()) }
                        val foundWords = trie.findAllWordsFromCharactersWithFrequency(enteredString)
                            .sortedBy { it.first }
                            .sortedBy { it.first.length }
                        prettyPrintWithFrequencies(foundWords)
                    }
                }
            }
        }

        internal fun prettyPrintWithFrequencies(foundWords: List<Pair<String, Int>>) {
            val ansiReset = "\u001B[0m"
            val ansiRed = "\u001B[91m"
            for (word in foundWords) if (word.second > 0) {
                println(ansiRed + "${word.first.length}: ${word.first}" + ansiReset)
            } else {
                println("${word.first.length}: ${word.first}")
            }
        }

        private class WordSayer(val listOfWords: List<String>) : Thread() {
            override fun run() {
                if (true) return
                for (word in listOfWords) {
                    Runtime.getRuntime().exec("say $word")
                    sleep(2000)
                }
            }
        }
    }
}