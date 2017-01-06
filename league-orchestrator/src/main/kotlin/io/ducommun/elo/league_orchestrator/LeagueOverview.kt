package io.ducommun.elo.league_orchestrator

data class LeagueOverview(
        val name: String,
        val players: List<PlayerOverview>
)