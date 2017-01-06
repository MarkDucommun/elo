package io.ducommun.elo.league_orchestrator

data class PlayerDescription(
        val playerName: String,
        val leagueName: String,
        val eloRatings: List<EloRatingDescription>
)