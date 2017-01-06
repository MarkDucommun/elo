package io.ducommun.elo.new_game_orchestrator

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.games.Game
import io.ducommun.elo.leagues.League
import io.ducommun.elo.new_game_orchestrator.domain.GameWithLeague
import io.ducommun.elo.new_game_orchestrator.domain.PlayerRating
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithScoreAndElo
import io.ducommun.elo.players.Player
import io.ducommun.elo.scores.Score
import io.ducommun.elo.shared.Creator
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

class GameMakerThingTest {

    @Test
    fun `should create a Score for the Player`() {

        val mockScoreCreator: Creator<Score> = createMockScoreCreator()

        val subject = createGameMakerThing(scoreCreator = mockScoreCreator)

        subject.processNewGame(
                player = PlayerWithScoreAndElo(
                        player = defaultPlayer.copy(id = 4),
                        elo = defaultEloRating,
                        score = 10.0),
                game = GameWithLeague(game = defaultGame.copy(id = 2), league = defaultLeague),
                opponents = emptyList()
        )

        verify(mockScoreCreator).create(Score(
                gameId = 2,
                playerId = 4,
                score = 10.0
        ))
    }

    @Test
    fun `should calculate a new ELO rating for the Player against each opponent`() {

        val mockRatingCalculator: RatingCalculator = mock()

        val subject = createGameMakerThing(ratingCalculator = mockRatingCalculator)

        val player = defaultPlayerScore.copy(score = 10.0)

        val opponentOne = defaultPlayerScore.copy(score = 15.0)

        val opponentTwo = defaultPlayerScore.copy(score = 55.0)

        subject.processNewGame(
                player = player,
                game = defaultGameWithLeague.copy(league = defaultLeague.copy(kFactor = 30)),
                opponents = listOf(opponentOne, opponentTwo)
        )

        verify(mockRatingCalculator).calculateNewRating(
                player = player,
                opponent = opponentOne,
                kFactor = 30
        )

        verify(mockRatingCalculator).calculateNewRating(
                player = player,
                opponent = opponentTwo,
                kFactor = 30
        )
    }

    @Test
    fun `should save a new Rating for Player that is the average of the ELOs from each opponent`() {

        val mockEloRatingCreator: Creator<EloRating> = mock()

        val subject = createGameMakerThing(
                eloRatingCreator = mockEloRatingCreator,
                scoreCreator = createMockScoreCreator(scoreId = 5),
                ratingCalculator = createMockRatingCalculator(listOf(1400, 1500))
        )

        subject.processNewGame(
                player = defaultPlayerScore.copy(
                        player = defaultPlayer.copy(id = 3),
                        elo = defaultEloRating.copy(version = 2)
                ),
                game = defaultGameWithLeague.copy(league = defaultLeague.copy(id = 2)),
                opponents = listOf(defaultPlayerScore, defaultPlayerScore)
        )

        verify(mockEloRatingCreator).create(EloRating(
                leagueId = 2,
                playerId = 3,
                scoreId = 5,
                version = 3,
                value = 1450
        ))
    }

    @Test
    fun `it returns a PlayerRating`() {

        val subject = createGameMakerThing(
                eloRatingCreator = mock(),
                scoreCreator = createMockScoreCreator(scoreId = 5),
                ratingCalculator = createMockRatingCalculator(listOf(1400, 1500))
        )

        val playerRating = subject.processNewGame(
                player = defaultPlayerScore.copy(
                        player = defaultPlayer.copy(name = "Name"),
                        elo = defaultEloRating.copy(value = 1400),
                        score = 10.0
                ),
                game = defaultGameWithLeague,
                opponents = listOf(defaultPlayerScore, defaultPlayerScore)
        )

        assertThat(playerRating).isEqualTo(PlayerRating(
                name = "Name",
                score = 10.0,
                newRating = 1450,
                ratingChange = 50
        ))
    }

    private fun createGameMakerThing(
            scoreCreator: Creator<Score> = createMockScoreCreator(),
            eloRatingCreator: Creator<EloRating> = mock(),
            ratingCalculator: RatingCalculator = createMockRatingCalculator()
    ) = GameMakerThing(
            scoreCreator = scoreCreator,
            ratingCalculator = ratingCalculator,
            eloRatingCreator = eloRatingCreator
    )

    private fun  createMockRatingCalculator(ratings: List<Int> = listOf(1400)): RatingCalculator = mock {
        on { calculateNewRating(any(), any(), any()) } doReturn ratings
    }

    private fun createMockScoreCreator(scoreId: Long = 0): Creator<Score> = mock {
        on { create(any()) } doReturn defaultScore.copy(id = scoreId)
    }

    val defaultEloRating = EloRating(leagueId = 0, playerId = 0, scoreId = 0, value = 0, version = 0)
    val defaultPlayer = Player(name = "")
    val defaultPlayerScore = PlayerWithScoreAndElo(player = defaultPlayer, elo = defaultEloRating, score = 0.0)

    val defaultScore = Score(gameId = 0, playerId = 0, score = 0.0)

    val defaultLeague = League(name = "", description = "", kFactor = 0, defaultELO = 0)
    val defaultGame = Game(leagueId = 0)
    val defaultGameWithLeague = GameWithLeague(game = defaultGame, league = defaultLeague)
}