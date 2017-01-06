package io.ducommun.elo.new_game_orchestrator

import io.ducommun.elo.eloratings.EloRatingFinder
import io.ducommun.elo.leagues.League
import io.ducommun.elo.new_game_orchestrator.domain.NamedPlayerScore
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithElo
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithScoreAndElo
import io.ducommun.elo.players.Player
import io.ducommun.elo.players.PlayerFinder
import org.springframework.stereotype.Service

@Service
open class PlayerBuilder(
        private val playerFinder: PlayerFinder,
        private val eloRatingFinder: EloRatingFinder
) {

    open fun getPlayer(playerName: String, league: League): PlayerWithElo {

        val player = playerFinder.findByName(playerName)

        val elo = eloRatingFinder.findCurrentEloRatingForPlayerInLeague(playerId = player.id, leagueId = league.id)

        return PlayerWithElo(player = player, elo = elo)
    }
}