package io.ducommun.elo.scores

data class Score(
        val id: Long = 0,
        val gameId: Long,
        val playerId: Long,
        val score: Double
)