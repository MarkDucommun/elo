package io.ducommun.elo.new_game_orchestrator

import io.ducommun.calculator.CalculatorFactory
import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.eloratings.EloRatingFinder
import io.ducommun.elo.games.Game
import io.ducommun.elo.leagues.League
import io.ducommun.elo.leagues.LeagueFinder
import io.ducommun.elo.new_game_orchestrator.domain.GameWithLeague
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithScoreAndElo
import io.ducommun.elo.new_game_orchestrator.domain.SavedGame
import io.ducommun.elo.new_game_orchestrator.domain.UnsavedGame
import io.ducommun.elo.scores.Score
import io.ducommun.elo.players.PlayerFinder
import io.ducommun.elo.shared.Creator
import org.springframework.stereotype.Service

@Service
class AveragedEloGameOrchestrator(
        private val leagueFinder: LeagueFinder,
        private val playerBuilder: PlayerBuilder,
        private val gameCreator: Creator<Game>,
        private val gameMakerThing: GameMakerThing
) : GameOrchestrator {

    override fun create(unsavedGame: UnsavedGame): SavedGame {

        val league = unsavedGame.league()
        val game = league.newGame()

        val playersWithScores = unsavedGame.scores.map {

            val playerWithElo = playerBuilder.getPlayer(playerName = it.playerName, league = league)

            PlayerWithScoreAndElo(player = playerWithElo.player, elo = playerWithElo.elo, score = it.score)
        }

        val gameWithLeague = GameWithLeague(game = game, league = league)

        val playerRatings = playersWithScores.map { player ->
            gameMakerThing.processNewGame(
                    player = player,
                    opponents = playersWithScores remove player,
                    game = gameWithLeague
            )
        }

        return SavedGame(
                leagueName = league.name,
                ratings = playerRatings
        )
    }

    private infix fun List<PlayerWithScoreAndElo>.remove(player: PlayerWithScoreAndElo) = filter { it != player }

    private fun UnsavedGame.league() = leagueFinder.findByName(leagueName)

    private fun League.newGame() = Game(leagueId = id).save()

    private fun Game.save() = gameCreator.create(this)
}
