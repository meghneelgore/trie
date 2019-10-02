package com.meghneelgore.trie.examples.jumbledwords

import com.google.common.collect.HashMultimap
import com.meghneelgore.trie.Trie
import net.sourceforge.tess4j.Tesseract
import java.awt.Rectangle
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class AutoJumbledWordsFinder {
    companion object {

        val centerRect = Rectangle(532, 1489, 549 - 532, 1507 - 1489)
        val charRects = arrayListOf<Rectangle>(
            Rectangle(474, 1179, 608 - 474, 1310 - 1179),
            Rectangle(664, 1275, 802 - 664, 1400 - 1275),
            Rectangle(720, 1486, 847 - 720, 1611 - 1486),
            Rectangle(587, 1651, 706 - 587, 1776 - 1651),
            Rectangle(373, 1654, 492 - 373, 1779 - 1654),
            Rectangle(239, 1487, 363 - 239, 1606 - 1487),
            Rectangle(296, 1279, 407 - 296, 1398 - 1297)

        )
        val mapOfChars: HashMultimap<Char, Rectangle> = HashMultimap.create()

        @JvmStatic
        fun main(args: Array<String>) {
            val tesseract = Tesseract()
            tesseract.setDatapath(".")
            val fileName = "englishwordswithfrequency.txt"
            val trie =
                Trie.trieWithFrequencyFromFilename(fileName, minLength = 3, maxLength = 10)


            Runtime.getRuntime().exec("adb shell screencap -p /sdcard/screencap.png")
            Runtime.getRuntime().exec("adb pull /sdcard/screencap.png").waitFor(1, TimeUnit.SECONDS)

            var jumble: String? = ""

            charRects.forEachIndexed { _, rect ->
                val character = tesseract.doOCR(File("screencap.png"), rect).replace(
                    Pattern.compile("[^A-Z]").toRegex(),
                    ""
                )
                jumble += character
                mapOfChars.put(character.toCharArray()[0], rect)
            }

            println(jumble)

            val foundWords = trie.findAllWordsFromCharactersWithFrequency(jumble!!.toLowerCase())
                .sortedBy { it.first }
                .sortedBy { it.first.length }
            JumbledWordsFinder.prettyPrintWithFrequencies(foundWords)
            inputLetters(jumble)
        }

        private fun inputLetters(jumble: String) {
            val copyOfMappings = HashMultimap.create<Char, Rectangle>()
            copyOfMappings.putAll(mapOfChars)
            for (char in jumble) {
                val rect = copyOfMappings.get(char).iterator().next()
                copyOfMappings.remove(char, rect)
//                Runtime.getRuntime()
//                    .exec("adb shell input swipe ${centerRect.centerX} ${centerRect.centerY} ${rect?.centerX} ${rect?.centerY}")
//                    .waitFor()
//                Runtime.getRuntime()
//                    .exec("adb shell input swipe ${rect?.centerX} ${rect?.centerY} ${centerRect.centerX} ${centerRect.centerY}")
//                    .waitFor()
                Runtime.getRuntime().exec("adb shell /dev/input/event2: 0001 ${0x014a} 1").waitFor()
                Runtime.getRuntime().exec("adb shell /dev/input/event2: 0003 ${0x0035} ${0x000001ee}").waitFor()
                Runtime.getRuntime().exec("adb shell /dev/input/event2: 0003 ${0x0036} ${0x000005af}").waitFor()
                Runtime.getRuntime().exec("adb shell /dev/input/event2: 0001 ${0x014a} 0").waitFor()
            }
        }
    }

}