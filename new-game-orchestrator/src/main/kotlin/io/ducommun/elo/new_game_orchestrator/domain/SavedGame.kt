package io.ducommun.elo.new_game_orchestrator.domain

data class SavedGame(
        val leagueName: String,
        val ratings: List<PlayerRating>
)