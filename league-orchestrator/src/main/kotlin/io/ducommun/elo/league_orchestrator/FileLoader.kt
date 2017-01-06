package io.ducommun.elo.league_orchestrator

import io.ducommun.elo.new_game_orchestrator.domain.NamedPlayerScore

interface FileLoader {

    fun load(file: String) : List<List<NamedPlayerScore>>
}