package io.ducommun.elo.new_game_orchestrator

import io.ducommun.calculator.CalculatorFactory
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithScoreAndElo
import org.springframework.stereotype.Service

@Service
open class RatingCalculator(
        private val calculatorFactory: CalculatorFactory
) {

    open fun calculateNewRating(
            player: PlayerWithScoreAndElo,
            opponent: PlayerWithScoreAndElo,
            kFactor: Int
    ): Int = calculatorFactory.build(
            currentRating = player.rating,
            opponentRating = opponent.rating,
            playerScore = player.score,
            opponentScore = opponent.score,
            kFactor = kFactor
    ).newRating()
}