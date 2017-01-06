package io.ducommun.elo.new_game_orchestrator.domain

import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.players.Player

data class PlayerWithElo(
        val player: Player,
        val elo: EloRating
)