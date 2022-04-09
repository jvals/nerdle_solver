package solver

import game.GameMode
import game.Nerdle
import game.OutputSequence
import game.Proximity
import game.Sequence
import game.Symbol
import java.io.File

//fun main() {
//    val nerdleSolver = NerdleSolver(
//        Nerdle(
//            Sequence(
//                Symbol.fromExpr("3*45=135")
//            ), GameMode.MANUAL
//        )
//    )
//    nerdleSolver.start()
//}

class NerdleSolver(private val game: Nerdle) {
    private val allKnownExpressions = File("sequences/sequences.txt").readLines()
    private val knowledge: SymbolKnowledge = SymbolKnowledge()

    fun start() {
        println("Starting a game")

        println("12/3-4=0")
        val firstResult = game.submitUserAnswer(Sequence(Symbol.fromExpr("12/3-4=0")))
        if (firstResult.isCorrect()) {
            println("You won! The correct sequence was $firstResult")
            return
        }
        addToKnowledge(firstResult)

        println("9+8*7=65")
        val secondResult = game.submitUserAnswer(Sequence(Symbol.fromExpr("9+8*7=65")))
        if (secondResult.isCorrect()) {
            println("You won! The correct sequence was $secondResult")
            return
        }
        addToKnowledge(secondResult)

        while (game.canPlay()) {
            val attempt: Sequence = generateAnswer()
            println(attempt)
            val result = game.submitUserAnswer(attempt)
            addToKnowledge(result)
            if (result.isCorrect()) {
                println("You won! The correct sequence was ${result.outputSymbols.map { it.value }}")
                return
            }
        }
        println("Game finished")
    }

    private fun addToKnowledge(outputSequence: OutputSequence) {
        outputSequence.outputSymbols.forEachIndexed { i, symbol ->
            if (symbol.proximity == Proximity.MISPLACED) {
//                knowledge[i] = knowledge[i] - symbol.value
                knowledge.removeMisplacedSymbol(i, symbol.value)
            }
            if (symbol.proximity == Proximity.EXCLUDED) {
//                for (j in knowledge.keys) {
//                    knowledge[j] = knowledge[j] - symbol.value
                knowledge.removeExcludedSymbol(i, symbol.value)
//                }
            }
            if (symbol.proximity == Proximity.CORRECT) {
                knowledge.setCorrectSymbol(i, symbol.value)
            }
        }
    }

    private fun generateAnswer(): Sequence {
        val misplacedSymbols: Set<Symbol> = knowledge.values.flatMap { knowledgeSymbols -> knowledgeSymbols.filter { it.proximity == Proximity.MISPLACED } }.map { it.value }.toSet()
        val possibleExpressions = allKnownExpressions.filter { s ->
            s.withIndex().all {
                val symbol = Symbol.from(it.value.toString())
                knowledge[it.index].any { knowledge -> knowledge.value == symbol }
            }
        }.map { Symbol.fromExpr(it) }.filter { symbols ->
            symbols.any { misplacedSymbols.contains(it) }
        }
        println("${possibleExpressions.size} possible answers")
        return Sequence(possibleExpressions.random())
//        return Sequence(orderByKnowledge(possibleExpressions).random())
    }

//    private fun orderByKnowledge(expressions: List<List<Symbol>>): List<List<Symbol>> {
//        return expressions.sortedBy { symbols ->
//            symbols.first()
//        }
//    }
}
