package io.ducommun.elo.league_orchestrator

import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.eloratings.EloRatingFinder
import io.ducommun.elo.leagues.League
import io.ducommun.elo.leagues.LeagueFinder
import io.ducommun.elo.players.Player
import io.ducommun.elo.players.PlayerFinder
import org.springframework.stereotype.Service

@Service
class StandardLeagueDescriber(
        val leagueFinder: LeagueFinder,
        val eloRatingFinder: EloRatingFinder,
        val playerFinder: PlayerFinder
) : LeagueDescriber {

    override fun describe(leagueName: String): LeagueOverview {

        val league = leagueFinder.findByName(leagueName)

        val eloRatings = getEloRatingsForLeague(league)

        val playerOverviews = createPlayerOverviews(eloRatings)

        return LeagueOverview(name = league.name, players = playerOverviews)
    }

    private fun getEloRatingsForLeague(league: League) =
            eloRatingFinder
                    .findCurrentEloRatingsForLeague(leagueId = league.id)
                    .sortedBy(EloRating::playerId)

    private fun createPlayerOverviews(eloRatings: List<EloRating>) =
            playerFinder
                    .findByIds(eloRatings.map(EloRating::playerId))
                    .sortedBy(Player::id)
                    .zip(eloRatings)
                    .map { (player, rating) -> player.createOverview(rating) }
                    .sortedByDescending { it.rating }
                    .mapIndexed { rank, player -> player.copy(rank = rank + 1) }

    private fun Player.createOverview(rating: EloRating) =
            PlayerOverview(
                    name = name,
                    rating = rating.value,
                    rounds = rating.version
            )
}