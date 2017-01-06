package io.ducommun.elo.league_orchestrator

import io.ducommun.elo.leagues.League
import io.ducommun.elo.new_game_orchestrator.GameOrchestrator
import io.ducommun.elo.new_game_orchestrator.domain.NamedPlayerScore
import io.ducommun.elo.new_game_orchestrator.domain.UnsavedGame
import io.ducommun.elo.players.Player
import io.ducommun.elo.shared.Creator
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class CsvLoaderLeagueOrchestrator(
        val fileLoader: FileLoader,
        val leagueCreator: Creator<League>,
        val playerCreator: Creator<Player>,
        val gameOrchestrator: GameOrchestrator
) : LeagueOrchestrator {

    override fun create(leagueSeed: LeagueSeed) {

        val league = League(
                name = "temp",
                description = "a temporary league",
                kFactor = leagueSeed.kFactor,
                defaultELO = leagueSeed.defaultElo
        ).save()

        val listOfGameScores = fileLoader.load(leagueSeed.fileLocation)

        listOfGameScores.createPlayers()

        listOfGameScores.createGames(league)
    }

    private fun List<List<NamedPlayerScore>>.createPlayers() {
        distinctNames().map { it.createPlayer() }
    }

    private fun String.createPlayer() = Player(name = this).save()

    private fun List<List<NamedPlayerScore>>.distinctNames() = flatMap { it.allNames() }.distinct()

    private fun List<NamedPlayerScore>.allNames() = map(NamedPlayerScore::playerName)

    private fun List<List<NamedPlayerScore>>.createGames(league: League) =
            map {
                UnsavedGame(
                        leagueName = league.name,
                        scores = it
                ).save()
            }

    private fun UnsavedGame.save() = gameOrchestrator.create(this)

    private fun Player.save() = playerCreator.create(this)

    private fun League.save() = leagueCreator.create(this)
}