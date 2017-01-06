package io.ducommun.elo.new_game_orchestrator

import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.new_game_orchestrator.domain.GameWithLeague
import io.ducommun.elo.new_game_orchestrator.domain.PlayerRating
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithScoreAndElo
import io.ducommun.elo.scores.Score
import io.ducommun.elo.shared.Creator
import org.springframework.stereotype.Service

@Service
open class GameMakerThing(
    private val scoreCreator: Creator<Score>,
    private val eloRatingCreator: Creator<EloRating>,
    private val ratingCalculator: RatingCalculator
) {

    open fun processNewGame(
            player: PlayerWithScoreAndElo,
            opponents: List<PlayerWithScoreAndElo>,
            game: GameWithLeague
    ): PlayerRating {

        val newRating = opponents.map { opponent ->
            ratingCalculator.calculateNewRating(
                    player = player,
                    opponent = opponent,
                    kFactor = game.kFactor
            )
        }.average().toInt()

        val score = scoreCreator.create(Score(
                gameId = game.gameId,
                playerId = player.id,
                score = player.score
        ))

        eloRatingCreator.create(EloRating(
                leagueId = game.leagueId,
                scoreId = score.id,
                playerId = player.id,
                version = player.nextVersion,
                value = newRating
        ))

        return PlayerRating(
                name = player.name,
                score = player.score,
                newRating = newRating,
                ratingChange = newRating - player.rating
        )
    }
}