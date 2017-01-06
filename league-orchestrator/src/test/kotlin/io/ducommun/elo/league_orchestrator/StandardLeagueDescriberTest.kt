package io.ducommun.elo.league_orchestrator

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.elo.Finder
import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.eloratings.EloRatingFinder
import io.ducommun.elo.leagues.League
import io.ducommun.elo.leagues.LeagueFinder
import io.ducommun.elo.players.Player
import io.ducommun.elo.players.PlayerFinder
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

class StandardLeagueDescriberTest {

    @Test
    fun `it gets the league ID`() {

        val mockLeagueFinder = createMockLeagueFinder()

        val subject = createStandardLeagueDescriber(leagueFinder = mockLeagueFinder)

        subject.describe("name", 0)

        verify(mockLeagueFinder).findByName("name")
    }

    @Test
    fun `it gets the ELO ratings for the league`() {

        val mockEloRatingFinder: EloRatingFinder = mock()

        val subject = createStandardLeagueDescriber(
                leagueFinder = createMockLeagueFinder(defaultLeague.copy(id = 1)),
                eloRatingFinder = mockEloRatingFinder
        )

        subject.describe("name", 0)

        verify(mockEloRatingFinder).findCurrentEloRatingsForLeague(leagueId = 1)
    }

    @Test
    fun `it finds the names of the Players in the League`() {

        val mockPlayerFinder: PlayerFinder = mock()

        val subject = createStandardLeagueDescriber(
                eloRatingFinder = createMockEloRatingFinder(listOf(
                        defaultEloRating.copy(playerId = 1),
                        defaultEloRating.copy(playerId = 2)
                )),
                playerFinder = mockPlayerFinder
        )

        subject.describe("name", 0)

        verify(mockPlayerFinder).findByIds(listOf(1, 2))
    }

    @Test
    fun `it returns the current state of the League sorted by ELO`() {

        val subject = createStandardLeagueDescriber(
                leagueFinder = createMockLeagueFinder(defaultLeague.copy(name = "name")),
                eloRatingFinder = createMockEloRatingFinder(listOf(
                        defaultEloRating.copy(value = 1500, version = 2, playerId = 2),
                        defaultEloRating.copy(value = 1400, version = 4, playerId = 3),
                        defaultEloRating.copy(value = 1550, version = 3, playerId = 1)
                )),
                playerFinder = createMockPlayerFinder(listOf(
                        defaultPlayer.copy(id = 1, name = "Tim"),
                        defaultPlayer.copy(id = 2, name = "Bob"),
                        defaultPlayer.copy(id = 3, name = "Joe")
                ))
        )

        val overview = subject.describe("name", minRounds = 0)

        assertThat(overview).isEqualToComparingFieldByFieldRecursively(
                LeagueOverview(
                        name = "name",
                        players = listOf(
                                PlayerOverview(name = "Tim", rating = 1550, rank = 1, rounds = 3),
                                PlayerOverview(name = "Bob", rating = 1500, rank = 2, rounds = 2),
                                PlayerOverview(name = "Joe", rating = 1400, rank = 3, rounds = 4)
                        )
                )
        )
    }

    @Test
    fun `should filter the Players by round when minRounds is not zero`() {

        val subject = createStandardLeagueDescriber(
                leagueFinder = createMockLeagueFinder(defaultLeague.copy(name = "name")),
                eloRatingFinder = createMockEloRatingFinder(listOf(
                        defaultEloRating.copy(value = 1500, version = 2, playerId = 2),
                        defaultEloRating.copy(value = 1400, version = 4, playerId = 3),
                        defaultEloRating.copy(value = 1550, version = 3, playerId = 1)
                )),
                playerFinder = createMockPlayerFinder(listOf(
                        defaultPlayer.copy(id = 1, name = "Tim"),
                        defaultPlayer.copy(id = 2, name = "Bob"),
                        defaultPlayer.copy(id = 3, name = "Joe")
                ))
        )

        val overview = subject.describe("name", minRounds = 3)

        assertThat(overview).isEqualToComparingFieldByFieldRecursively(
                LeagueOverview(
                        name = "name",
                        players = listOf(
                                PlayerOverview(name = "Tim", rating = 1550, rank = 1, rounds = 3),
                                PlayerOverview(name = "Joe", rating = 1400, rank = 2, rounds = 4)
                        )
                )
        )
    }

    private fun createMockLeagueFinder(league: League = defaultLeague): LeagueFinder =
            mock { on { findByName(any()) } doReturn league }

    private fun createMockEloRatingFinder(
            eloRatings: List<EloRating> = listOf(defaultEloRating)
    ): EloRatingFinder = mock {
        on { findCurrentEloRatingsForLeague(any()) } doReturn eloRatings
    }

    private fun createMockPlayerFinder(players: List<Player> = listOf(defaultPlayer)): PlayerFinder =
            mock { on { findByIds(listOf(any())) } doReturn players }

    private fun createStandardLeagueDescriber(
            leagueFinder: LeagueFinder = createMockLeagueFinder(),
            eloRatingFinder: EloRatingFinder = createMockEloRatingFinder(),
            playerFinder: PlayerFinder = createMockPlayerFinder()
    ) = StandardLeagueDescriber(
            leagueFinder = leagueFinder,
            eloRatingFinder = eloRatingFinder,
            playerFinder = playerFinder
    )

    val defaultLeague = League(name = "", description = "", kFactor = 0, defaultELO = 0)

    val defaultPlayer = Player(name = "")

    val defaultEloRating = EloRating(
            id = 0,
            leagueId = 0,
            playerId = 0,
            scoreId = 0,
            version = 0,
            value = 0
    )
}
