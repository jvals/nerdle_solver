package expressionGenerator

import simpleCalculator.calculate
import java.io.File
import kotlin.concurrent.thread

private const val alphabet = "0123456789*/+-="


fun main() {
    generateSequence()
    sequences.forEach { (threadName, expressions) ->
        expressions.forEach {
            File("sequences/sequences_$threadName.txt").appendText(it + "\n")
        }
    }
}

private val sequences = mutableMapOf<String, MutableList<String>>()

private fun generateSequence(sequence: String = "") {
    alphabet.forEach {
        if (sequence.isEmpty()) {
            thread(name = "thread_$it") {
                val mutatedSequence = sequence + it
                if (mutatedSequence.length == 8) {
                    try {
                        testSequence(mutatedSequence)
                        writeSequence(mutatedSequence, Thread.currentThread().name)
                    } catch (_: Exception) {

                    }
                } else {
                    generateSequence(mutatedSequence)
                }
            }
        } else {
            val mutatedSequence = sequence + it
            if (mutatedSequence.length == 8) {
                try {
                    testSequence(mutatedSequence)
                    writeSequence(mutatedSequence, Thread.currentThread().name)
                } catch (_: Exception) {

                }
            } else {
                generateSequence(mutatedSequence)
            }
        }
    }
}

fun writeSequence(mutatedSequence: String, name: String) {
//    File("sequences/sequences_$name.txt").appendText(mutatedSequence + "\n")
    if (sequences.contains(name)) {
        sequences[name]!!.add(mutatedSequence)
    } else {
        sequences[name] = mutableListOf(name)
    }
}

// Method throws if sequence is not valid, else it does nothing
private fun testSequence(sequence: String) {
    if (sequence.count { it == '=' } > 1) {
        throw RuntimeException()
    }
    val (lhs, rhs) = sequence.split('=')
    if ("*/+-".any { rhs.contains(it) }) {
        throw RuntimeException()
    }
    if (calculate(lhs) != calculate(rhs)) {
        throw RuntimeException()
    }
    if (anyLeadingZero(sequence)) {
        throw RuntimeException()
    }
    if (loneZeros(sequence.split('=').first())) {
        throw RuntimeException()
    }
}

private fun anyLeadingZero(sequence: String): Boolean {
    val pattern = Regex("(?<!\\d)0+\\d+")
    return sequence.contains(pattern)
}

private fun loneZeros(sequence: String): Boolean {
    val pattern = Regex("(?<=[\\+\\\\\\-\\*\\=])0(?=[\\+\\\\\\-\\*\\=])")
    val lastZeroPattern = Regex("[\\+\\\\\\-\\*]0\$")
    return sequence.first() == '0' || sequence.contains(pattern) || sequence.contains(lastZeroPattern)
}
