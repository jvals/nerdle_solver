package simpleCalculator

fun calculate(expression: String): Double {
    val lexemes = lex(expression)
    val tokens = tokenize(lexemes)

    return parse(tokens)
}

private fun parse(tokens: List<Token>): Double {
    var result = parse(tokens, setOf(Operator.MUL, Operator.DIV))
    result = parse(result, setOf(Operator.ADD, Operator.SUB))

    if (result.size != 1) {
        throw ParserException("Result from parsing contained more than one token $result")
    } else {
        when (val token = result.first()) {
            is Operand -> return token.value
            is Operator -> throw ParserException("Parsing tokens resulted in an operator")
        }
    }
}

private fun parse(tokens: List<Token>, operators: Set<Operator>): List<Token> {
    val tokensParsedByOperators = tokens.toMutableList()
    while (operators.any { tokensParsedByOperators.contains(it) }) {
        val operatorIndex = tokensParsedByOperators.indexOfFirst { operators.contains(it) }
        val operator = tokensParsedByOperators[operatorIndex] as Operator
        val left = tokensParsedByOperators[operatorIndex - 1] as Operand
        val right = tokensParsedByOperators[operatorIndex + 1] as Operand
        val res = compute(operator, left, right)
        tokensParsedByOperators[operatorIndex - 1] = res
        tokensParsedByOperators.removeAt(operatorIndex)
        tokensParsedByOperators.removeAt(operatorIndex)
    }
    return tokensParsedByOperators
}

private fun compute(operator: Operator, left: Operand, right: Operand): Operand = when (operator) {
    Operator.MUL -> left * right
    Operator.DIV -> left / right
    Operator.ADD -> left + right
    Operator.SUB -> left - right
}

private fun tokenize(lexemes: List<Lexeme>): List<Token> = lexemes.map {
    when {
        isOperand(it) -> Operand(it.toDouble())
        isOperator(it) -> Operator.from(it)
        else -> throw TokenizerException("Cannot tokenize unknown lexeme $it in lexemes: $lexemes")
    }
}

private fun lex(expression: String): List<Lexeme> {
    val operators = "*/+\\-"
    val regex = Regex("((?=[$operators])|(?<=[$operators]))") // positive lookahead and lookbehind
    return expression.split(regex)
}

private fun isOperand(lexeme: Lexeme): Boolean = lexeme.all { it.isDigit() }

private fun isOperator(lexeme: Lexeme): Boolean {
    require(lexeme.length == 1)
    val operators = "*/+-"
    return lexeme.first() in operators
}

private typealias Lexeme = String

private sealed interface Token

private enum class Operator(private val c: Char) : Token {
    MUL('*'), DIV('/'), ADD('+'), SUB('-');

    companion object {
        fun from(s: String): Operator {
            require(s.length == 1)
            return values().first { it.c == s.first() }
        }
    }
}

@JvmInline
private value class Operand(val value: Double) : Token {
    operator fun plus(other: Operand) = Operand(this.value + other.value)
    operator fun minus(other: Operand) = Operand(this.value - other.value)
    operator fun times(other: Operand) = Operand(this.value * other.value)
    operator fun div(other: Operand) = Operand(this.value / other.value)
}

private open class CalculatorException(reason: String): RuntimeException(reason)
private class ParserException(reason: String): CalculatorException(reason)
private class TokenizerException(reason: String): CalculatorException(reason)
