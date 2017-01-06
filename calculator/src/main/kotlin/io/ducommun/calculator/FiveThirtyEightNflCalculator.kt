package io.ducommun.calculator

class FiveThirtyEightNflCalculator(
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

    val winnerRating = if (playerWon()) currentRating else opponentRating
    val loserRating = if (playerLost()) currentRating else opponentRating

    override fun newRating(): Int {
        return (currentRating + kFactor * marginOfVictoryMultiplier()
                * adjustedResult()).toNearestInt()
    }

    private fun marginOfVictoryMultiplier(): Double {
        return Math.log(scoreDifferential() + 1.0) * (2.2 / (0.001 * ratingDifferential() + 2.2))
    }

    private fun scoreDifferential(): Double {
        return Math.abs(playerScore - opponentScore).toDouble()
    }

    private fun ratingDifferential(): Int {
        return winnerRating - loserRating
    }
}
