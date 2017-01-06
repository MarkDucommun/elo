package io.ducommun.elo.server

import io.ducommun.elo.league_orchestrator.LeagueOrchestrator
import io.ducommun.elo.league_orchestrator.LeagueSeed
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class LeagueLoader(
        val leagueOrchestrator: LeagueOrchestrator,
        @Value("\${file.location}") val fileLocation: String,
        @Value("\${k.factor}") val kFactor: Int,
        @Value("\${default.elo}") val defaultElo: Int
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (fileLocation.isBlank()) return

        leagueOrchestrator.create(LeagueSeed(
                fileLocation,
                kFactor,
                defaultElo
        ))
    }
}