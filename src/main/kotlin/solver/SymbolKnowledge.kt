package solver

import game.Proximity
import game.Symbol

private typealias Position = Int
class SymbolKnowledge: HashMap<Position, Set<KnowledgeSymbol>>() {
    init {
        val allSymbols = Symbol.values().map { KnowledgeSymbol(it) }.toSet()
        put(0, allSymbols - Symbol.operators().map { KnowledgeSymbol(it) }.toSet())
        put(1, allSymbols - KnowledgeSymbol(Symbol.EQUALS))
        put(2, allSymbols)
        put(3, allSymbols)
        put(4, allSymbols)
        put(5, allSymbols)
        put(6, allSymbols)
        put(7, allSymbols - Symbol.operators().map { KnowledgeSymbol(it) }.toSet())
    }
    @Override
    override fun get(key: Position): Set<KnowledgeSymbol> {
        return super.getOrDefault(key, emptySet())
    }

    fun removeMisplacedSymbol(position: Position, symbol: Symbol) {
        // Set all other instances of this symbol to misplaced
        keys.forEach {
            this[it] = this[it]
                .filter { oldSymbol -> symbol == oldSymbol.value }
                .map { oldSymbol -> KnowledgeSymbol(oldSymbol.value, Proximity.MISPLACED) }
                .toSet().plus(this[it].filter { oldSymbol -> oldSymbol.value != symbol })

        }
        // Remove the symbol from the guessed position
        this[position] = this[position].filter { it.value != symbol }.toSet()
    }

    fun removeExcludedSymbol(position: Position, symbol: Symbol) {
        // If any other positions has this symbol as misplaced or correct, we only remove it from here
        if (anyMisplacedOrCorrect(symbol)) {
            this[position] = this[position].filter { it.value != symbol }.toSet()
        } else {
            // Remove symbol from all positions
            keys.forEach { key ->
                this[key] = this[key].filter { it.value != symbol }.toSet()
            }
        }
    }

    fun setCorrectSymbol(position: Position, symbol: Symbol) {
        if (symbol == Symbol.EQUALS) {
            // Remove equals from all other positions
            keys.forEach { key ->
                this[key] = this[key].filter { it.value != Symbol.EQUALS }.toSet()
            }
        }
        this[position] = setOf(KnowledgeSymbol(symbol, Proximity.CORRECT))
    }

    private fun anyMisplacedOrCorrect(symbol: Symbol): Boolean {
        return values.any {
            it.contains(KnowledgeSymbol(symbol, Proximity.MISPLACED)) || it.contains(KnowledgeSymbol(symbol, Proximity.CORRECT))
        }
    }
}
