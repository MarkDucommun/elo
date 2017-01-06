package io.ducommun.elo.new_game_orchestrator.domain

import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.leagues.League
import io.ducommun.elo.players.Player

data class PlayerWithScoreAndElo(
        val player: Player,
        val elo: EloRating,
        val score: Double
) {

    val id = player.id
    val name = player.name
    val rating = elo.value
    val nextVersion = elo.version + 1
}