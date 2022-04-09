package game

enum class Symbol(val str: String) {
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    ADD("+"),
    SUB("-"),
    TIMES("*"),
    DIV("/"),
    EQUALS("=");

    companion object {
        fun from(str: String): Symbol {
            try {
                return values().first { it.str == str }
            } catch (ex: java.util.NoSuchElementException) {
                throw IllegalArgumentException("Cannot convert $str to game.Symbol")
            }
        }

        fun fromExpr(expr: String): List<Symbol> {
            return expr.map { from(it.toString()) }
        }

        fun operators(): Set<Symbol> {
            return setOf(ADD, SUB, TIMES, DIV, EQUALS)
        }

        fun operands(): Set<Symbol> {
            return setOf(ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE)
        }
    }
}

