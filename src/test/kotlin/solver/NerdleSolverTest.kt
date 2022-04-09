package solver

import game.GameMode
import game.Nerdle
import game.Symbol
import org.junit.jupiter.api.Test

internal class NerdleSolverTest {
    @Test
    fun `1*14-9=5`() {
        val nerdleSolver = NerdleSolver(
            Nerdle(
              solutionSequence = game.Sequence(Symbol.fromExpr("1*14-9=5")),
                gameMode = GameMode.MANUAL
            )
        )
        nerdleSolver.start()
    }

    @Test
    fun `10+45=55`() {
        val nerdleSolver = NerdleSolver(
            Nerdle(
                solutionSequence = game.Sequence(Symbol.fromExpr("10+45=55")), // 5 tries
                gameMode = GameMode.MANUAL
            )
        )
        nerdleSolver.start()
    }

    @Test
    fun `8*5-37=3`() {
        val nerdleSolver = NerdleSolver(
            Nerdle(
                solutionSequence = game.Sequence(Symbol.fromExpr("8*5-37=3")), // 4 tries
                gameMode = GameMode.MANUAL
            )
        )
        nerdleSolver.start()
    }

    @Test
    fun `268 div 4 = 67`() {
        val nerdleSolver = NerdleSolver(
            Nerdle(
                solutionSequence = game.Sequence(Symbol.fromExpr("268/4=67")), // 3 tries
                gameMode = GameMode.MANUAL
            )
        )
        nerdleSolver.start()
    }
}