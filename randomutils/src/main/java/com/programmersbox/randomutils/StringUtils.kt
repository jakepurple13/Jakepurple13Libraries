package com.programmersbox.randomutils

infix fun String.isAnagramOf(word: String): Boolean = isAnagram(word, this)
infix fun String.hasAnagram(anagram: String): Boolean = isAnagram(this, anagram)

fun isAnagram(word: String, anagram: String): Boolean {
    val c = word.groupBy(Char::lowercaseChar).mapValues { it.value.size }
    val a = anagram.groupBy(Char::lowercaseChar).mapValues { it.value.size }

    for (i in a) {
        c[i.key]?.let { if (it < i.value) return false } ?: return false
    }

    return true
}