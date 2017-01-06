package io.ducommun.elo.new_game_orchestrator.domain

data class PlayerRating(
        val name: String,
        val score: Double,
        val newRating: Int,
        val ratingChange: Int
)