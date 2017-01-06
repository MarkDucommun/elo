package io.ducommun.calculator

import java.lang.Math.abs
import java.lang.Math.log

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

    override fun newRating(): Int =
            (currentRating + kFactor * marginOfVictoryMultiplier() * adjustedResult()).toNearestInt()

    private fun marginOfVictoryMultiplier(): Double {
        return log(scoreDifferential() + 1.0) * (2.2 / (0.001 * ratingDifferential() + 2.2))
    }

    private fun scoreDifferential(): Double {
        return abs(playerScore - opponentScore)
    }

    private fun ratingDifferential(): Int {
        return winnerRating - loserRating
    }
}
