package io.ducommun.elo.league_orchestrator

import io.ducommun.elo.new_game_orchestrator.domain.NamedPlayerScore

fun createListOfGames(names: List<String>, scores: List<Double>) =
        names.zip(scores).map {
            val (name, score) = it
            NamedPlayerScore(name, score)
        }

