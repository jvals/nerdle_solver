import game.Symbol.*
import game.GameMode
import game.Nerdle
import game.Proximity
import game.Sequence
import game.Symbol

fun main(args: Array<String>) {
    println("Welcome to game.Nerdle!")
    println("Legend: ${Proximity.CORRECT.name}=${Proximity.CORRECT.name.first()}, " +
            "${Proximity.MISPLACED.name}=${Proximity.MISPLACED.name.first()}, " +
            "${Proximity.EXCLUDED.name}=${Proximity.EXCLUDED.name.first()}")

    val game = Nerdle(
        Sequence(
            listOf( NINE, TIMES, TWO, ZERO, EQUALS, ONE, EIGHT, ZERO )
        ),
        GameMode.MANUAL
    )

    while (game.canPlay()) {
        try {
            val inputSequence: Sequence = getInput()
            val outputSequence = game.submitUserAnswer(inputSequence)
            outputSequence.outputSymbols.forEach {
                print("${it.proximity.name.first()}")
            }
            println()
            if (outputSequence.isCorrect()) {
                println("You won!")
            }

        } catch (ex: IllegalArgumentException) {
            println("Illegal input sequence")
        } catch (ex: IllegalStateException) {
            println("Illegal input sequence")
        }
    }

}

fun getInput(): Sequence {
    print("Write your guess: ")
    val inputString: String? = readLine()
    if (inputString != null) {
        return Sequence(inputString.map { Symbol.from(it.toString()) })
    } else {
        throw IllegalArgumentException("Input cannot be empty")
    }
}
