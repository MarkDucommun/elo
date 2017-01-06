package io.ducommun.calculator

import org.springframework.stereotype.Service

@Service
class FiveThirtyEightNflCalculatorFactory : CalculatorFactory {

    override fun build(
            currentRating: Int,
            opponentRating: Int,
            playerScore: Double,
            opponentScore: Double,
            kFactor: Int
    ): Calculator {

        return FiveThirtyEightNflCalculator(
                currentRating,
                opponentRating,
                playerScore,
                opponentScore,
                kFactor
        )
    }
}