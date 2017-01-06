package io.ducommun.calculator

import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

class FiveThirtyEightNflCalculatorFactoryTest {

    @Test
    fun `creates a FiveThirtyEightNflCalculator`() {
        val calculator = FiveThirtyEightNflCalculatorFactory().build(
                currentRating = 1500,
                opponentRating = 1400,
                playerScore = 10.0,
                opponentScore = 5.0,
                kFactor = 20
        )

        assertThat(calculator).isOfAnyClassIn(FiveThirtyEightNflCalculator::class.java)
    }
}