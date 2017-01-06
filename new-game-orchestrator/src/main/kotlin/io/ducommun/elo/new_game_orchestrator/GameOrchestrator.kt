package io.ducommun.elo.new_game_orchestrator

import io.ducommun.elo.new_game_orchestrator.domain.SavedGame
import io.ducommun.elo.new_game_orchestrator.domain.UnsavedGame

interface GameOrchestrator {

    fun create(unsavedGame: UnsavedGame) : SavedGame
}