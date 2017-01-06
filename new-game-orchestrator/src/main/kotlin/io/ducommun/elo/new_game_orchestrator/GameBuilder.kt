package io.ducommun.elo.new_game_orchestrator

import io.ducommun.calculator.CalculatorFactory
import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.games.Game
import io.ducommun.elo.leagues.League
import io.ducommun.elo.new_game_orchestrator.domain.PlayerRating
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithScoreAndElo
import io.ducommun.elo.new_game_orchestrator.domain.SavedGame
import io.ducommun.elo.scores.Score
import io.ducommun.elo.shared.Creator

class GameBuilder(
        private val scoreCreator: Creator<Score>,
        private val eloRatingCreator: Creator<EloRating>,
        private val game: Game,
        private val league: League,
        private val playersSortedByScore: List<PlayerWithScoreAndElo>,
        private val calculatorFactory: CalculatorFactory
) {

    fun create() = SavedGame(leagueName = league.name, ratings = playerRatings())

    private fun playerRatings() = playersSortedByScore.map { it.summarizePlayerChanges() }

    private fun PlayerWithScoreAndElo.summarizePlayerChanges() : PlayerRating {

        val newEloRating = saveNewEloRating()

        return PlayerRating(
                name = name,
                score = score,
                newRating = newEloRating,
                ratingChange = newEloRating - rating
        )
    }

    private fun PlayerWithScoreAndElo.saveNewEloRating() = newRating().apply {
        EloRating(
                leagueId = league.id,
                playerId = id,
                scoreId = createScore().id,
                value = this,
                version = nextVersion
        ).save()
    }

    private fun PlayerWithScoreAndElo.newRating() =
            opponents().map { opponent -> calculateEloRatingAfterMatchWith(opponent) }.average()

    private fun PlayerWithScoreAndElo.calculateEloRatingAfterMatchWith(opponent: PlayerWithScoreAndElo) =
            calculatorFactory.build(
                    currentRating = rating,
                    opponentRating = opponent.rating,
                    playerScore = score,
                    opponentScore = opponent.score,
                    kFactor = league.kFactor
            ).newRating()

    private fun PlayerWithScoreAndElo.createScore() = Score(
            gameId = game.id,
            playerId = id,
            score = score
    ).save()

    private fun PlayerWithScoreAndElo.opponents() = playersSortedByScore.filter { it != this }

    private fun Score.save() = scoreCreator.create(this)

    private fun EloRating.save() = eloRatingCreator.create(this)

    private fun List<Int>.average() = sum() / size
}