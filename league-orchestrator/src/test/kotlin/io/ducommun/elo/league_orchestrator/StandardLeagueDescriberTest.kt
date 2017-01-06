package io.ducommun.elo.league_orchestrator

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
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

        val mockLeagueFinder = mock<LeagueFinder> {
            on { findByName(any()) } doReturn(League.create(id = 1))
        }

        val subject = StandardLeagueDescriber(
                mockLeagueFinder,
                mock(),
                mock()
        )

        subject.describe("name")

        verify(mockLeagueFinder).findByName("name")
    }

    @Test
    fun `it gets the ELO ratings for the league`() {

        val mockEloRatingFinder = mock<EloRatingFinder>()

        val subject = StandardLeagueDescriber(
                mock { on { findByName(any()) } doReturn(League.create(id = 1)) },
                mockEloRatingFinder,
                mock()
        )

        subject.describe("name")

        verify(mockEloRatingFinder).findCurrentEloRatingsForLeague(1)
    }

    @Test
    fun `it finds the names of the Players in the League`() {

        val mockPlayerFinder = mock<PlayerFinder>()

        val subject = StandardLeagueDescriber(
                mock { on { findByName(any()) } doReturn(League.create(id = 1)) },
                mock { on { findCurrentEloRatingsForLeague(any()) } doReturn(listOf(
                        defaultEloRating.copy(playerId = 1),
                        defaultEloRating.copy(playerId = 2)
                ))},
                mockPlayerFinder
        )

        subject.describe("name")

        verify(mockPlayerFinder).findByIds(listOf(1, 2))
    }

    @Test
    fun `it returns the current state of the League sorted by ELO`() {

        val subject = StandardLeagueDescriber(
                mock { on { findByName(any()) } doReturn(League.create(id = 1, name = "name")) },
                mock { on { findCurrentEloRatingsForLeague(any()) } doReturn(listOf(
                        defaultEloRating.copy(playerId = 1, value = 1500),
                        defaultEloRating.copy(playerId = 2, value = 1550, version = 3)
                ))},
                mock { on { findByIds(listOf(any())) } doReturn(listOf(
                        Player(id = 1, name = "Bob"),
                        Player(id = 2, name = "Tim")
                )) }
        )

        val stuff: LeagueOverview = subject.describe("name")

        assertThat(stuff).isEqualToComparingFieldByFieldRecursively(
                LeagueOverview(
                        name = "name",
                        players = listOf(
                                PlayerOverview(name = "Tim", rating = 1550, rank = 1, rounds = 3),
                                PlayerOverview(name = "Bob", rating = 1500, rank = 2, rounds = 2)
                        )
                )
        )
    }

    val defaultEloRating = EloRating(
            id = 0,
            leagueId = 1,
            playerId = 1,
            scoreId = 1,
            version = 2,
            value = 1
    )
}