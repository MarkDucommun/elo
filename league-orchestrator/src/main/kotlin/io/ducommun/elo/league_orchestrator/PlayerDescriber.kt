package io.ducommun.elo.league_orchestrator

interface PlayerDescriber {

    fun describePlayer(leagueName: String, playerName: String): PlayerDescription
}