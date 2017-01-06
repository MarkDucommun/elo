package io.ducommun.elo.server

import io.ducommun.elo.league_orchestrator.PlayerDescriber
import io.ducommun.elo.league_orchestrator.PlayerDescription
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PlayerOverviewController(
        private val playerDescriber: PlayerDescriber
) {

    @GetMapping("/leagues/{leagueName}/players/{playerName}")
    fun describePlayer(
            @PathVariable leagueName: String,
            @PathVariable playerName: String
    ): ResponseEntity<PlayerDescription> {

        return ResponseEntity.ok().body(playerDescriber.describePlayer(
                leagueName = leagueName,
                playerName = playerName
        ))
    }
}