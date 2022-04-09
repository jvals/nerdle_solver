package simpleCalculator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

internal class CalculatorKtTest() {

    @Test
    fun `Simple expressions`() {
        assertEquals(9.0, calculate("3+6"))
        assertEquals(6.0, calculate("7-1"))
        assertEquals(16.0, calculate("2*8"))
        assertEquals(5.0, calculate("25/5"))
    }

    @Test
    fun `Expressions with operator precedence`() {
        assertEquals(4.0, calculate("3+20/10/2"))
        assertEquals(11.0, calculate("1+0+7*10/7"))
        assertEquals(1.0, calculate("1+1-1+1-1"))
    }

    @Test
    fun `Unknown symbols should result in tokenization exceptions`() {
        assertFails { calculate("abc") }
        assertFails { calculate("1++2") }
    }
    @Test
    fun `Negative numbers should fail`() {
//        assertFails { calculate("5-9") }
        assertFails { calculate("-3-4") }
    }

    @Test
    fun `Floating point numbers`() {
        // 160/9=17,777777778
        assertEquals(17, calculate("160/9").toInt())
    }
}