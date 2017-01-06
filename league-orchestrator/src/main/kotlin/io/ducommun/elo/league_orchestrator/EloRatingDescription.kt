package io.ducommun.elo.league_orchestrator

data class EloRatingDescription(
        val value: Int,
        val diff: String,
        val version: Int
)