package io.ducommun.elo.new_game_orchestrator.domain

import io.ducommun.elo.games.Game
import io.ducommun.elo.leagues.League

data class GameWithLeague(
        val game: Game,
        val league: League
) {
    val gameId = game.id
    val leagueId = league.id
    val kFactor = league.kFactor
}