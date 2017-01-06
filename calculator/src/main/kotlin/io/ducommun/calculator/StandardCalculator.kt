package io.ducommun.calculator

class StandardCalculator(
        currentRating: Int,
        opponentRating: Int,
        playerScore: Double,
        opponentScore: Double,
        kFactor: Int
) : Calculator(
        currentRating,
        opponentRating,
        playerScore,
        opponentScore,
        kFactor
) {

    override fun newRating(): Int {
        return (currentRating + kFactor * adjustedResult()).toNearestInt()
    }
}