package io.ducommun.calculator

interface CalculatorFactory {

    fun build(
            currentRating: Int,
            opponentRating: Int,
            playerScore: Double,
            opponentScore: Double,
            kFactor: Int
    ) : Calculator
}