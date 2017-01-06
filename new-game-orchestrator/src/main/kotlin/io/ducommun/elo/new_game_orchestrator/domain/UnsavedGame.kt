package io.ducommun.elo.new_game_orchestrator.domain

data class UnsavedGame(
        val leagueName: String,
        val scores: List<NamedPlayerScore>
)