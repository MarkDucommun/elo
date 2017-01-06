package io.ducommun.calculator

abstract class Calculator(
        val currentRating: Int,
        val opponentRating: Int,
        val playerScore: Double,
        val opponentScore: Double,
        val kFactor: Int
) {

    protected val matchResult = calculateMatchResult()

    protected val expectedResult = calculateExpectedResult()

    abstract fun newRating() : Int

    protected fun adjustedResult() = matchResult - expectedResult

    protected fun calculateMatchResult() = when {
        playerWon() -> 1.0
        playerLost() -> 0.0
        else -> 0.5
    }

    protected fun playerWon() = playerScore > opponentScore
    protected fun playerLost() = playerScore < opponentScore

    protected fun calculateExpectedResult(): Double {
        return 1.0 / (1.0 + 10.0.power((opponentRating - currentRating).toDouble() / 400.0))
    }

    protected fun Double.power(exponent: Double) = Math.pow(this, exponent)
    protected fun Double.toNearestInt() = Math.round(this).toInt()
}