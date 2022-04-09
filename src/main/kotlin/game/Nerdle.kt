package game

class Nerdle(private val solutionSequence: Sequence, private val gameMode: GameMode) {
    private var remainingAttempts = 6
    private var gameSolved = false

    fun canPlay(): Boolean {
        return remainingAttempts > 0 && !gameSolved
    }

    fun submitUserAnswer(guess: Sequence): OutputSequence {
        if (remainingAttempts > 0) {
            remainingAttempts -= 1
            val outputSequence = validateGuess(guess)
            if (outputSequence.isCorrect()) {
                gameSolved = true
            }
            return outputSequence
        } else {
            throw NerdleException("Cannot submit answer. No more attempts")
        }
    }

    private fun validateGuess(guess: Sequence): OutputSequence {
        if (gameMode == GameMode.MANUAL) {
            val outputSymbols: List<OutputSymbol> = guess.symbols.mapIndexed { i, answer: Symbol ->
                val proximity = when {
                    answer == solutionSequence.symbols[i] -> Proximity.CORRECT
                    solutionSequence.symbols.contains(answer) -> Proximity.MISPLACED
                    else -> Proximity.EXCLUDED
                }
                OutputSymbol(
                    answer,
                    proximity
                )
            }

            return OutputSequence(outputSymbols)
        } else {

            println("Write the result from Nerdle")
            println(
                "Legend: ${Proximity.CORRECT.name}=${Proximity.CORRECT.name.first()}, " +
                        "${Proximity.MISPLACED.name}=${Proximity.MISPLACED.name.first()}, " +
                        "${Proximity.EXCLUDED.name}=${Proximity.EXCLUDED.name.first()}"
            )
            val result = readLine()
            val proximities: List<Proximity> = result?.map {
                when (it) {
                    'M' -> Proximity.MISPLACED
                    'C' -> Proximity.CORRECT
                    'E' -> Proximity.EXCLUDED
                    else -> throw RuntimeException("Unknown symbol $it")
                }
            }
                ?: throw RuntimeException("Could not parse input")
            val outputSymbols: List<OutputSymbol> = guess.symbols.mapIndexed { index, answer ->
                OutputSymbol(answer, proximities[index])
            }

            return OutputSequence(outputSymbols)

        }
    }
}

private class NerdleException(s: String) : RuntimeException(s)
