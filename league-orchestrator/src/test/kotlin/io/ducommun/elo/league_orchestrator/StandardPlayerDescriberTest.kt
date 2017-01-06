package io.ducommun.elo.league_orchestrator

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
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

class StandardPlayerDescriberTest {

    @Test
    fun `it finds the player`() {

        val mockPlayerFinder = createMockPlayerFinder()

        val subject = createStandardPlayerDescriber(playerFinder = mockPlayerFinder)

        subject.describePlayer(leagueName = "league-name", playerName = "player-name")

        verify(mockPlayerFinder).findByName("player-name")
    }

    @Test
    fun `it finds the league`() {

        val mockLeagueFinder = createMockLeagueFinder()

        val subject = createStandardPlayerDescriber(leagueFinder = mockLeagueFinder)

        subject.describePlayer(leagueName = "league-name", playerName = "player-name")

        verify(mockLeagueFinder).findByName("league-name")
    }

    @Test
    fun `it finds all the ELO ratings for the player for a league`() {

        val mockEloRatingFinder: EloRatingFinder = mock()

        val subject = createStandardPlayerDescriber(
                playerFinder = createMockPlayerFinder(playerId = 1),
                leagueFinder = createMockLeagueFinder(leagueId = 1),
                eloRatingFinder = mockEloRatingFinder
        )

        subject.describePlayer(leagueName = "league-name", playerName = "player-name")

        verify(mockEloRatingFinder).findEloRatingsForPlayerInLeague(1, 1)
    }

    @Test
    fun `it returns all the ELO ratings for the Player for a League`() {

        val subject = createStandardPlayerDescriber(
                eloRatingFinder = createMockEloRatingFinder(1400, 1450)
        )

        val playerDescription = subject.describePlayer(leagueName = "league-name", playerName = "player-name")

        assertThat(playerDescription).isEqualToComparingFieldByFieldRecursively(PlayerDescription(
                playerName = "player-name",
                leagueName = "league-name",
                eloRatings = listOf(
                        EloRatingDescription(value = 1450, diff = "+50", version = 2),
                        EloRatingDescription(value = 1400, diff = "-100", version = 1)
                )
        ))

    }

    private fun createStandardPlayerDescriber(
            playerFinder: PlayerFinder = createMockPlayerFinder(),
            leagueFinder: LeagueFinder = createMockLeagueFinder(),
            eloRatingFinder: EloRatingFinder = createMockEloRatingFinder()
    ) = StandardPlayerDescriber(
            playerFinder,
            leagueFinder,
            eloRatingFinder
    )

    private fun createMockPlayerFinder(
            playerId: Long = 0,
            playerName: String = "player-name"
    ): PlayerFinder = mock {
        on { findByName(any()) } doReturn Player(
                id = playerId,
                name = playerName
        )
    }

    private fun createMockLeagueFinder(
            leagueId: Long = 1,
            leagueName: String = "league-name"
    ): LeagueFinder = mock {
        on { findByName(any()) } doReturn League(
                id = leagueId,
                name = leagueName,
                description = "",
                kFactor = 0,
                defaultELO = 0
        )
    }

    private fun createMockEloRatingFinder(vararg ratingsArg: Int): EloRatingFinder = mock {

        val ratings = if (ratingsArg.isEmpty()) listOf(1500) else ratingsArg.toList()

        val eloRatings = ratings.mapIndexed { index, rating ->
            EloRating(
                    id = index + 1L,
                    leagueId = 1,
                    playerId = 1,
                    scoreId = 1,
                    version = index + 1,
                    value = rating
            )
        }

        on { findEloRatingsForPlayerInLeague(any(), any()) } doReturn eloRatings
    }
}