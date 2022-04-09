package game

data class OutputSequence(val outputSymbols: List<OutputSymbol>) {
    fun isCorrect(): Boolean {
        return outputSymbols.all { it.proximity == Proximity.CORRECT }
    }
}
