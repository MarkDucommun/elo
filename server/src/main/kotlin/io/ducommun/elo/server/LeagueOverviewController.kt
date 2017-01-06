package io.ducommun.elo.server

import io.ducommun.elo.league_orchestrator.LeagueDescriber
import io.ducommun.elo.league_orchestrator.LeagueOverview
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LeagueOverviewController(
        private val leagueDescriber: LeagueDescriber
) {

    @GetMapping("/leagues/{name}")
    fun describe(@PathVariable name: String) : ResponseEntity<LeagueOverview> {

        return ResponseEntity.ok().body(leagueDescriber.describe(name))
    }
}