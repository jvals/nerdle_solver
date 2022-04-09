package game

import simpleCalculator.calculate

data class Sequence(
    val symbols: List<Symbol>
) {
    init {
        require(symbols.size == 8)
        require(symbols.filter { it == Symbol.EQUALS }.size == 1)
        require(isValid())
    }

    override fun toString(): String = symbols.joinToString(separator = "") { it.str }

    private fun isValid(): Boolean {
        val equalsIndex = symbols.indexOf(Symbol.EQUALS)
        val lhs = symbols.subList(0, equalsIndex).joinToString(separator = "") { it.str }
        val rhs = symbols.subList(equalsIndex+1, symbols.size).joinToString(separator = "") { it.str }
        check(rhs.isNumeric()) // The right-hand side should always be a number

        return calculate(lhs) == calculate(rhs)
    }
}

private fun String.isNumeric(): Boolean {
    return this.all { it.isDigit() }
}
