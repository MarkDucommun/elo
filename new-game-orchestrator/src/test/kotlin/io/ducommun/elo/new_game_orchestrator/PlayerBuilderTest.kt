package io.ducommun.elo.new_game_orchestrator

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.eloratings.EloRatingFinder
import io.ducommun.elo.leagues.League
import io.ducommun.elo.new_game_orchestrator.domain.NamedPlayerScore
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithScoreAndElo
import io.ducommun.elo.players.Player
import io.ducommun.elo.players.PlayerFinder
import org.assertj.core.api.KotlinAssertions
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

class PlayerBuilderTest {

    @Test
    fun `should find the Player by name`() {

        val mockPlayerFinder = createMockPlayerFinder()

        val subject = createPlayerBuilder(playerFinder = mockPlayerFinder)

        subject.getPlayer(playerName = "Tim", league = defaultLeague)

        verify(mockPlayerFinder).findByName("Tim")
    }

    @Test
    fun `should retrieve the Elo for the Player`() {

        val mockEloRatingFinder: EloRatingFinder = createMockEloRatingFinder()
        val leagueId: Long = 3
        val startingPlayerId: Long = 2

        val subject = createPlayerBuilder(
                playerFinder = createMockPlayerFinder(startingId = startingPlayerId),
                eloRatingFinder = mockEloRatingFinder
        )

        subject.getPlayer(playerName = "Tim", league = defaultLeague.copy(id = leagueId))

        verify(mockEloRatingFinder).findCurrentEloRatingForPlayerInLeague(startingPlayerId, leagueId)
    }

    @Test
    fun `should return the Player including their elo`() {

        val subject = createPlayerBuilder(eloRatingFinder = createMockEloRatingFinder(1450, 1500))

        val player = subject.getPlayer(playerName = "Tim", league = defaultLeague)

        assertThat(player.elo).isEqualTo(defaultEloRating.copy(value = 1450))
        assertThat(player.player).isEqualTo(Player(id = 1, name = "Tim"))
    }

    private fun createPlayerBuilder(
            playerFinder: PlayerFinder = createMockPlayerFinder(),
            eloRatingFinder: EloRatingFinder = createMockEloRatingFinder()
    ): PlayerBuilder = PlayerBuilder(
            playerFinder,
            eloRatingFinder
    )

    private fun createMockPlayerFinder(startingId: Long = 1): PlayerFinder = mock {
        var idCount = startingId

        on { findByName(any()) }.thenAnswer { it ->
            Player(id = idCount++, name = it.arguments.first() as String)
        }
    }

    private fun createMockEloRatingFinder(vararg returnedElos: Int): EloRatingFinder = mock {
        if (returnedElos.isEmpty()) {
            on { findCurrentEloRatingForPlayerInLeague(any(), any()) } doReturn defaultEloRating
        } else  {
            on { findCurrentEloRatingForPlayerInLeague(any(), any()) } doReturn returnedElos.map {
                defaultEloRating.copy(value = it)
            }
        }
    }

    val defaultLeague = League(id = 0, name = "", description = "", kFactor = 0, defaultELO = 0)
    val defaultEloRating = EloRating(id = 0, leagueId = 0, playerId = 0, scoreId = 0, value = 0, version = 0)
}