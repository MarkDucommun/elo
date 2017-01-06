package io.ducommun.calculator

import junit.framework.Assert.assertEquals
import org.junit.Test

class StandardCalculatorTest {
    var K_FACTOR = 0

    @Test
    fun testCasesFor32() {
        K_FACTOR = 32

        1500.win(2000).changeBy(30)
        1500.draw(2000).changeBy(14)
        1500.lose(2000).changeBy(-2)

        2000.win(2000).changeBy(16)
        2000.draw(2000).changeBy(0)
        2000.lose(2000).changeBy(-16)

        2000.win(1500).changeBy(2)
        2000.draw(1500).changeBy(-14)
        2000.lose(1500).changeBy(-30)
    }

    @Test
    fun testCasesFor20() {
        K_FACTOR = 20

        1500.win(2000).changeBy(19)
        1500.draw(2000).changeBy(9)
        1500.lose(2000).changeBy(-1)

        2000.win(2000).changeBy(10)
        2000.draw(2000).changeBy(0)
        2000.lose(2000).changeBy(-10)

        2000.win(1500).changeBy(1)
        2000.draw(1500).changeBy(-9)
        2000.lose(1500).changeBy(-19)
    }

    fun Int.win(opponentRating : Int) : Calculator {
        return StandardCalculator(
                currentRating = this,
                opponentRating = opponentRating,
                playerScore = 1.0,
                opponentScore = 0.0,
                kFactor = K_FACTOR
        )
    }

    fun Int.draw(opponentRating : Int) : Calculator {
        return StandardCalculator(
                currentRating = this,
                opponentRating = opponentRating,
                playerScore = 0.0,
                opponentScore = 0.0,
                kFactor = K_FACTOR
        )
    }

    fun Int.lose(opponentRating : Int) : Calculator {
        return StandardCalculator(
                currentRating = this,
                opponentRating = opponentRating,
                playerScore = 0.0,
                opponentScore = 1.0,
                kFactor = K_FACTOR
        )
    }

    fun Calculator.changeBy(value: Int) {
        assertEquals(currentRating + value, newRating())
    }
}