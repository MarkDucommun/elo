package io.ducommun.elo.league_orchestrator

import io.ducommun.elo.leagues.League

interface LeagueOrchestrator {

    fun create(leagueSeed: LeagueSeed)
}