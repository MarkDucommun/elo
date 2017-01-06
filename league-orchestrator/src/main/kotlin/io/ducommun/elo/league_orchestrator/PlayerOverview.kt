package io.ducommun.elo.league_orchestrator

data class PlayerOverview(
        val name: String,
        val rating: Int,
        val rank: Int = 0,
        val rounds: Int
)