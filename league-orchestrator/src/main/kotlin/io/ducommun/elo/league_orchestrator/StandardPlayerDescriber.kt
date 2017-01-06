package io.ducommun.elo.league_orchestrator

import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.eloratings.EloRatingFinder
import io.ducommun.elo.leagues.LeagueFinder
import io.ducommun.elo.players.PlayerFinder
import org.springframework.stereotype.Service

@Service
class StandardPlayerDescriber(
        private val playerFinder: PlayerFinder,
        private val leagueFinder: LeagueFinder,
        private val eloRatingFinder: EloRatingFinder
) : PlayerDescriber {

    override fun describePlayer(leagueName: String, playerName: String): PlayerDescription {

        val player = playerFinder.findByName(playerName)

        val league = leagueFinder.findByName(leagueName)

        val eloRatings = eloRatingFinder
                .findEloRatingsForPlayerInLeague(playerId = player.id, leagueId = league.id)
                .sortedBy { it.version }

        val points = addLeagueDefault(eloRatings)

        val eloDescriptions = eloRatings.zip(points).map {
            it.first.toEloRatingDescription(it.second)
        }

        return PlayerDescription(
                playerName = playerName,
                leagueName = leagueName,
                eloRatings = eloDescriptions.reversed()
        )
    }

    private fun addLeagueDefault(eloRatings: List<EloRating>): List<Int> {
        return eloRatings
                .reversed()
                .map { it.value }
                .plus(1500)
                .reversed()
    }

    private fun EloRating.toEloRatingDescription(previousEloValue: Int): EloRatingDescription {
        val diff = value - previousEloValue
        return EloRatingDescription(
                value = value,
                diff = if (diff >= 0) "+$diff" else "$diff",
                version = version
        )
    }
}