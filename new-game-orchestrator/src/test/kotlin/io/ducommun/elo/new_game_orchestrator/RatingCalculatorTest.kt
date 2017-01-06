package io.ducommun.elo.new_game_orchestrator

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.calculator.Calculator
import io.ducommun.calculator.CalculatorFactory
import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithScoreAndElo
import io.ducommun.elo.players.Player
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

class RatingCalculatorTest {

    @Test
    fun `should delegate to the CalculatorFactory`() {

        val mockCalculatorFactory = createMockCalculatorFactory()

        val subject = RatingCalculator(mockCalculatorFactory)

        subject.calculateNewRating(
                player = defaultPlayerScore.copy(elo = defaultEloRating.copy(value = 1500), score = 20.0),
                opponent = defaultPlayerScore.copy(elo = defaultEloRating.copy(value = 1400), score = 10.0),
                kFactor = 20
        )

        verify(mockCalculatorFactory).build(
                currentRating = 1500,
                playerScore = 20.0,
                opponentRating = 1400,
                opponentScore = 10.0,
                kFactor = 20
        )
    }

    @Test
    fun `should call the Calculator`() {

        val mockCalculator: Calculator = mock()

        val subject = RatingCalculator(createMockCalculatorFactory(calculator = mockCalculator))

        subject.calculateNewRating(
                player = defaultPlayerScore.copy(elo = defaultEloRating.copy(value = 1500), score = 20.0),
                opponent = defaultPlayerScore.copy(elo = defaultEloRating.copy(value = 1400), score = 10.0),
                kFactor = 20
        )

        verify(mockCalculator).newRating()
    }

    @Test
    fun `should return what the Calculator returns`() {

        val subject = RatingCalculator(createMockCalculatorFactory(rating = 1400))

        val rating = subject.calculateNewRating(
                player = defaultPlayerScore.copy(elo = defaultEloRating.copy(value = 1500), score = 20.0),
                opponent = defaultPlayerScore.copy(elo = defaultEloRating.copy(value = 1400), score = 10.0),
                kFactor = 20
        )

        assertThat(rating).isEqualTo(1400)
    }

    private fun createMockCalculator(rating: Int = 1200): Calculator = mock {
        on { newRating() } doReturn rating
    }

    private fun createMockCalculatorFactory(rating: Int): CalculatorFactory =
            createMockCalculatorFactory(createMockCalculator(rating = rating))

    private fun createMockCalculatorFactory(calculator: Calculator = createMockCalculator()): CalculatorFactory = mock {
        on { build(any(), any(), any(), any(), any()) } doReturn calculator
    }

    val defaultEloRating = EloRating(leagueId = 0, playerId = 0, scoreId = 0, value = 0, version = 0)
    val defaultPlayer = Player(name = "")
    val defaultPlayerScore = PlayerWithScoreAndElo(player = defaultPlayer, elo = defaultEloRating, score = 0.0)
}