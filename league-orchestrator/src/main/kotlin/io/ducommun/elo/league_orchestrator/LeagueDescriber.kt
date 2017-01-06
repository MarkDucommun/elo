package io.ducommun.elo.league_orchestrator

interface LeagueDescriber {

    fun describe(leagueName: String, minRounds: Int): LeagueOverview
}