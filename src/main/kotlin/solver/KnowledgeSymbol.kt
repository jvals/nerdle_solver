package solver

import game.OutputSymbol
import game.Proximity
import game.Symbol

data class KnowledgeSymbol(val value: Symbol, val proximity: Proximity = Proximity.UNKNOWN) {
    companion object {
        fun from(outputSymbol: OutputSymbol): KnowledgeSymbol {
            return KnowledgeSymbol(outputSymbol.value, outputSymbol.proximity)
        }
    }
}
