package io.ducommun.elo.new_game_orchestrator.domain

import io.ducommun.elo.players.Player

data class PlayerScore(
        val player: Player,
        val score: Int
)