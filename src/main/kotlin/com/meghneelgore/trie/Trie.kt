package com.meghneelgore.trie

import java.io.File

/**
 * This class creates a Trie from:
 * 1) A List<String> of a word list
 * 2) A file containing a word list. Each word must start on the first column of a new line. Words starting with a `#`
 * will be ignored.
 *
 * A Trie is a prefix tree. Each node in the trie holds a value, and a next set of nodes. Traversing down a trie to a
 * 'terminal' node gives a valid word from the word list loaded into the trie.
 */
class Trie private constructor() {
    internal lateinit var root: TrieNode<Char, String>
    var nWords: Long = 0
        private set

    /**
     * Returns whether a specific word is in the dictionary.
     */
    fun isWordInDictionary(word: String): Boolean {
        var tempRoot = root
        for (character in word) {
            if (tempRoot.contains(character)) tempRoot = tempRoot.get(character) else return false
        }
        return tempRoot.isTerminal
    }

    /**
     * Finds all the words in the dictionary that can complete a given start sequence. For example:
     * Given a dictionary: {"a, aa, ab, abc, abb, acc, aab"} and a sequence "ab"
     * the completions list will contain ["ab", "abc", "abb"]
     */
    fun findCompletionsInDictionary(word: String): List<String> {
        val t = System.currentTimeMillis()
        val completions = mutableListOf<String>()
        // Traverse the word
        var tempRoot = root
        for (character in word) {
            if (tempRoot.contains(character)) tempRoot = tempRoot.get(character) else return emptyList()
        }
        // If word exists until now, traverse to leaves recursively
        traverseToLeaves(tempRoot, completions)
        println("Found ${completions.size} completions in ${System.currentTimeMillis() - t} milliseconds")
        return completions
    }

    /**
     * Finds all the words that can be produced using a sequence of given characters.
     * Given a dictionary: {"a, aa, ab, abc, abb, acc, aab, abd"} and a sequence ['a','b', 'c']
     * The output list will contain ["a", "ab", "abc"]
     */
    fun findAllWordsFromCharacters(characterString: String): List<String> {
        val t = System.currentTimeMillis()
        val foundWords = mutableListOf<String>()
        traverseTrie(root, characterString.toMutableList())
        getFoundWords(root, foundWords)
        resetTraversal(root)
        println("Found ${foundWords.size} words in ${System.currentTimeMillis() - t} milliseconds")
        return foundWords
    }

    fun findAllWordsFromCharactersWithFrequency(characterString: String): List<Pair<String, Int>> {
        val t = System.currentTimeMillis()
        val foundWords = mutableListOf<Pair<String, Int>>()
        traverseTrie(root, characterString.toMutableList())
        getFoundWordsWithFrequency(root, foundWords)
        resetTraversal(root)
        println("Found ${foundWords.size} words in ${System.currentTimeMillis() - t} milliseconds")
        return foundWords
    }

    fun getAllWords(root: TrieNode<Char, String>): List<Pair<String, Int>> {
        val list = mutableListOf<Pair<String, Int>>()
        getAllWordsInTrie(root, list)
        return list
    }

    private fun getAllWordsInTrie(root: TrieNode<Char, String>?, wordList: MutableList<Pair<String, Int>>) {
        if (root == null) return
        if (root.isTerminal) wordList.add(root.value!! to root.frequency)
        for (currentRoot in root.next.values) {
            getAllWordsInTrie(currentRoot, wordList)
        }
    }

    internal fun resetTraversal(root: TrieNode<Char, String>) {
        for (trieNode in root.next.values) {
            trieNode.isTraversed = false
            resetTraversal(trieNode)
        }
    }

    internal fun traverseTrie(root: TrieNode<Char, String>, listOfCharacters: MutableList<Char>) {
        for (trieNode in root.next.values) {
            val value: String? = trieNode.value
            value ?: return
            if (listOfCharacters.contains(value.last())) {
                trieNode.isTraversed = true
                traverseTrie(trieNode, listOfCharacters.apply { listOfCharacters.remove(value.last()) })
                listOfCharacters.add(value.last())
            }
        }
    }

    private fun getFoundWords(root: TrieNode<Char, String>, foundWords: MutableList<String>) {
        for (trieNode in root.next.values) {
            if (trieNode.isTraversed && trieNode.isTerminal) {
                foundWords.add(trieNode.value.toString())
            }
            getFoundWords(trieNode, foundWords)
        }
    }

    private fun getFoundWordsWithFrequency(root: TrieNode<Char, String>, foundWords: MutableList<Pair<String, Int>>) {
        for (trieNode in root.next.values) {
            if (trieNode.isTraversed && trieNode.isTerminal) {
                foundWords.add(trieNode.value.toString() to trieNode.frequency)
            }
            getFoundWordsWithFrequency(trieNode, foundWords)
        }
    }

    private fun traverseToLeaves(tempRoot: TrieNode<Char, String>, completions: MutableList<String>) {
        if (tempRoot.isTerminal) {
            tempRoot.value?.let { completions.add(it) }
        }
        for (trieNode in tempRoot.next.values) traverseToLeaves(trieNode, completions)
    }

    fun setEncountered(input: String) {
        setEncountered(root, input.toMutableList())
    }

    private fun setEncountered(root: TrieNode<Char, String>?, word: MutableList<Char>) {
        if (root == null) return
        if (word.isEmpty() && root.isTerminal) {
            root.frequency = 1
            return
        }
        val character = word.removeAt(0)
        if (root.contains(character)) {
            val nextRoot = root.get(character)
            setEncountered(nextRoot, word)
        }
    }

    companion object {
        /**
         * Helper function for creating a Trie from a newline delimited word list file. Each word must start on the
         * first column of a new line. Lines beginning with a # will be ignored
         *
         * @param fileName Name of the file to load words from.
         * @param minLength Minimum length of the words to load. Any words in the file that are shorter than minLength
         *                  will be ignored.
         * @param maxLength Maximum length of the words to load. Any words in the file that are longer than maxLength
         *                  will be ignored.
         */
        @JvmOverloads
        @JvmStatic
        fun trieFromFilename(fileName: String, minLength: Int = 1, maxLength: Int = 100): Trie =
            trieOf(File(fileName).readLines(), minLength, maxLength)

        @JvmOverloads
        @JvmStatic
        fun trieWithFrequencyFromFilename(fileName: String, minLength: Int = 1, maxLength: Int = 100): Trie =
            trieWithFrequencyOf(File(fileName).readLines(), minLength, maxLength)

        @JvmOverloads
        @JvmStatic
        fun trieWithFrequencyOf(wordsWithFrequency: List<String>, minLength: Int = 1, maxLength: Int = 100): Trie {
            val t = System.currentTimeMillis()
            val trie = Trie()
            trie.root = TrieNode()
            if (wordsWithFrequency.isNullOrEmpty()) return trie
            for (wordWithFreq in wordsWithFrequency) {
                val (word, freq) = wordWithFreq.split(",")
                if (word.length < minLength || word.length > maxLength) continue
                if (word.first() == '#') continue
                trie.nWords++
                var tempRoot = trie.root
                word.forEachIndexed { index, character ->
                    val isTerminal = index == word.lastIndex
                    if (tempRoot.contains(character)) {
                        tempRoot = tempRoot.get(character)
                        if (isTerminal) tempRoot.isTerminal = true
                    } else {
                        val newTrieNode = TrieNode<Char, String>(
                            value = if (tempRoot.value != null) tempRoot.value + (character) else character.toString(),
                            isTerminal = isTerminal,
                            frequency = if (isTerminal) freq.trim().toInt() else 0
                        )
                        tempRoot.put(character, newTrieNode)
                        tempRoot = newTrieNode
                    }
                }
            }
            println("${trie.nWords} words loaded in ${System.currentTimeMillis() - t} milliseconds")
            return trie
        }

        /**
         * Helper function for creating a Trie from a list of words.
         *
         * @param minLength Minimum length of the words to load. Any words in the list that are shorter than minLength
         *                  will be ignored.
         * @param maxLength Maximum length of the words to load. Any words in the list that are longer than maxLength
         *                  will be ignored.
         */
        @JvmOverloads
        @JvmStatic
        fun trieOf(words: List<String>?, minLength: Int = 1, maxLength: Int = 100): Trie {
            val t = System.currentTimeMillis()
            val trie = Trie()
            trie.root = TrieNode()
            if (words.isNullOrEmpty()) return trie
            for (word in words) {
                if (word.length < minLength || word.length > maxLength) continue
                if (word.first() == '#') continue
                trie.nWords++
                var tempRoot = trie.root
                word.forEachIndexed { index, character ->
                    if (tempRoot.contains(character)) {
                        tempRoot = tempRoot.get(character)
                        if (index == word.lastIndex) tempRoot.isTerminal = true
                    } else {
                        val newTrieNode = TrieNode<Char, String>(
                            value = if (tempRoot.value != null) tempRoot.value + (character) else character.toString(),
                            isTerminal = index == word.lastIndex
                        )
                        tempRoot.put(character, newTrieNode)
                        tempRoot = newTrieNode
                    }
                }
            }
            println("${trie.nWords} words loaded in ${System.currentTimeMillis() - t} milliseconds")
            return trie
        }

        @JvmStatic
        fun saveTrieWithFrequenciesToFile(trie: Trie, fileName: String) {
            val file = File(fileName)
            val words = trie.getAllWords(trie.root)
            file.printWriter().use { out ->
                for (word in words) {
                    out.println("${word.first}, ${word.second}")
                }
            }
        }
    }
}