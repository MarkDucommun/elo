package io.ducommun.elo.league_orchestrator

import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.eloratings.EloRatingFinder
import io.ducommun.elo.leagues.League
import io.ducommun.elo.leagues.LeagueFinder
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithElo
import io.ducommun.elo.players.Player
import io.ducommun.elo.players.PlayerFinder
import org.springframework.stereotype.Service

@Service
class StandardLeagueDescriber(
        val leagueFinder: LeagueFinder,
        val eloRatingFinder: EloRatingFinder,
        val playerFinder: PlayerFinder
) : LeagueDescriber {

    override fun describe(leagueName: String, minRounds: Int): LeagueOverview {

        val league = leagueFinder.findByName(leagueName)

        val eloRatings = getEloRatingsForLeague(league)

        val playerIdsForLeague = eloRatings.map(EloRating::playerId)

        val players = getLeaguePlayers(playerIdsForLeague)

        val playersWithElos = players with eloRatings

        val playerOverviews = playersWithElos
                .map { it.toPlayerOverview() }
                .removePlayersWithFewerRoundsThan(minRounds)
                .sortByRating()
                .addRank()

        return LeagueOverview(name = league.name, players = playerOverviews)
    }

    private fun List<PlayerOverview>.addRank() = mapIndexed { rank, player -> player.copy(rank = rank + 1) }

    private fun List<PlayerOverview>.sortByRating() = sortedByDescending { it.rating }

    private fun List<PlayerOverview>.removePlayersWithFewerRoundsThan(rounds: Int) = filter { it.rounds >= rounds }

    private fun getLeaguePlayers(ids: List<Long>) = playerFinder.findByIds(ids)

    private fun getEloRatingsForLeague(league: League) =
            eloRatingFinder.findCurrentEloRatingsForLeague(leagueId = league.id)

    private fun PlayerWithElo.toPlayerOverview() = PlayerOverview(
            name = player.name,
            rating = elo.value,
            rounds = elo.version
    )

    private infix fun List<Player>.with(eloRatings: List<EloRating>): List<PlayerWithElo> =
            (playersSortedById() zip eloRatings.sortedByPlayerId()).map {
                it.toPlayerWithElo()
            }

    private fun List<Player>.playersSortedById() = sortedBy(Player::id)

    private fun List<EloRating>.sortedByPlayerId() = sortedBy(EloRating::playerId)

    private fun Pair<Player, EloRating>.toPlayerWithElo() = PlayerWithElo(player = first, elo = second)
}