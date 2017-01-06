package io.ducommun.elo.league_orchestrator

data class LeagueSeed(
        val fileLocation: String,
        val kFactor: Int,
        val defaultElo: Int
)