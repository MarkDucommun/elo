package io.ducommun.elo.eloratings

data class EloRating(
        val id: Long = 0,
        val leagueId: Long,
        val playerId: Long,
        val scoreId: Long,
        val version: Int,
        val value: Int
)