package io.ducommun.elo.eloratings

import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyLong

class EloRatingFinderTest {
    lateinit var mockRepository: EloRatingRepository
    lateinit var mockEntityTranslator: EloRatingEntityTranslator
    lateinit var subject: EloRatingFinder

    @Before
    fun setup() {
        mockRepository = createMockRepository()
        mockEntityTranslator = EloRatingEntityTranslator()
        subject = EloRatingFinder(mockRepository, mockEntityTranslator)
    }

    @Test
    fun findCurrentEloRatingForPlayerInLeague_passesPlayerAndLeagueIdsToTheRepository() {
        findCurrentEloRating()

        verify(mockRepository).findByPlayerIdAndLeagueId(playerId = 1, leagueId = 2)
    }

    @Test
    fun findCurrentEloRatingForPlayerInLeague_returnsTheEloRating() {
        val player = findCurrentEloRating()

        assertThat(player).isEqualTo(EloRating(
                id = 2,
                leagueId = 2,
                scoreId = 3,
                playerId = 4,
                version = 2,
                value = 1550))
    }

    @Test
    fun `find by league delegates to the repository`() {
        subject.findCurrentEloRatingsForLeague(1L)

        verify(mockRepository).findByLeagueId(1L)
    }

    @Test
    fun `find by league selects the most recent elo version for each player`() {
        val playerOneRating = EloRatingEntity(leagueId = 1, playerId = 1, version = 1)
        val playerTwoRating = playerOneRating.copy(playerId = 2)
        whenever(mockRepository.findByLeagueId(any())).thenReturn(listOf(
                playerOneRating,
                playerOneRating.copy(version = 2),
                playerOneRating.copy(version = 3),
                playerTwoRating,
                playerTwoRating.copy(version = 2)
        ))

        val currentEloRatingsForLeague = subject.findCurrentEloRatingsForLeague(1L)

        assertThat(currentEloRatingsForLeague).contains(
                defaultEloRating.copy(leagueId = 1, playerId = 1, version = 3),
                defaultEloRating.copy(leagueId = 1, playerId = 1, version = 3)
        )
    }

    @Test
    fun `find ELO Ratings by Player and League looks up ELO Ratings`() {

        subject.findEloRatingsForPlayerInLeague(1, 1)

        verify(mockRepository).findByPlayerIdAndLeagueId(1, 1)
    }

    @Test
    fun `find ELO Ratings by Player and League returns the ELO Ratings`() {
        val playerOneRating = EloRatingEntity(leagueId = 1, playerId = 1, version = 1)
        whenever(mockRepository.findByPlayerIdAndLeagueId(any(), any())).thenReturn(listOf(
                playerOneRating,
                playerOneRating.copy(version = 2),
                playerOneRating.copy(version = 3)
        ))

        val playersEloRatings = subject.findEloRatingsForPlayerInLeague(1, 1)

        assertThat(playersEloRatings).contains(
                defaultEloRating.copy(leagueId = 1, playerId = 1, version = 1),
                defaultEloRating.copy(leagueId = 1, playerId = 1, version = 2),
                defaultEloRating.copy(leagueId = 1, playerId = 1, version = 3)
        )
    }

    private fun findCurrentEloRating() = subject.findCurrentEloRatingForPlayerInLeague(playerId = 1, leagueId = 2)

    private fun createMockRepository() = mock<EloRatingRepository>() {
        val elo = EloRatingEntity(
                id = 1,
                leagueId = 2,
                scoreId = 3,
                playerId = 4,
                version = 1,
                value = 1500,
                createdAt = 0,
                updatedAt = 0
        )
        val newestElo = elo.copy(id = 2, version = 2, value = 1550)
        on { findByPlayerIdAndLeagueId(anyLong(), anyLong()) } doReturn listOf(elo, newestElo)
    }

    val defaultEloRating = EloRating(
            id = 0,
            leagueId = 0,
            playerId = 0,
            scoreId = 0,
            version = 0,
            value = 0
    )
}