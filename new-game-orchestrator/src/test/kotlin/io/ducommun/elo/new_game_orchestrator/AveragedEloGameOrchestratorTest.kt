package io.ducommun.elo.new_game_orchestrator

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.ducommun.elo.eloratings.EloRating
import io.ducommun.elo.games.Game
import io.ducommun.elo.leagues.League
import io.ducommun.elo.leagues.LeagueFinder
import io.ducommun.elo.new_game_orchestrator.domain.GameWithLeague
import io.ducommun.elo.new_game_orchestrator.domain.NamedPlayerScore
import io.ducommun.elo.new_game_orchestrator.domain.PlayerRating
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithElo
import io.ducommun.elo.new_game_orchestrator.domain.PlayerWithScoreAndElo
import io.ducommun.elo.new_game_orchestrator.domain.SavedGame
import io.ducommun.elo.new_game_orchestrator.domain.UnsavedGame
import io.ducommun.elo.players.Player
import io.ducommun.elo.shared.Creator
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

class AveragedEloGameOrchestratorTest {

    @Test
    fun `it finds the League by name`() {

        val leagueName = "league-name"

        val mockLeagueFinder = createMockLeagueFinder(leagueName = leagueName)

        val subject = createOrchestrator(leagueFinder = mockLeagueFinder)

        subject.create(buildUnsavedGame(leagueName = leagueName))

        mockLeagueFinder.didFindByName(leagueName)
    }

    @Test
    fun `it creates a new Game for that League`() {

        val leagueId: Long = 1

        val mockGameCreator = createMockGameCreator()

        val subject = createOrchestrator(
                leagueFinder = createMockLeagueFinder(id = leagueId),
                gameCreator = mockGameCreator
        )

        subject.create(buildUnsavedGame())

        mockGameCreator.didCreate(Game(leagueId = leagueId))
    }

    @Test
    fun `it finds the id and elo of each Player`() {

        val leagueId: Long = 1

        val mockPlayerBuilder: PlayerBuilder = createMockPlayerBuilder()

        val subject = createOrchestrator(
                leagueFinder = createMockLeagueFinder(id = leagueId),
                playerBuilder = mockPlayerBuilder
        )

        subject.create(buildUnsavedGame(scores = listOf(
                "Brad" to 10.0,
                "Tim" to 5.0
        )))

        verify(mockPlayerBuilder).getPlayer(playerName = "Brad", league = defaultLeague.copy(leagueId))
        verify(mockPlayerBuilder).getPlayer(playerName = "Tim", league = defaultLeague.copy(leagueId))
    }

    @Test
    fun `it should create a new EloRating for each Player`() {

        val brad = defaultPlayerScore.copy(score = 10.0)
        val tim = defaultPlayerScore.copy(score = 5.0)
        val joe = defaultPlayerScore.copy(score = 2.0)

        val mockGameMakerThing: GameMakerThingInt = mock()

        val subject = createOrchestrator(
                playerBuilder = createMockPlayerBuilder(listOf(
                        defaultPlayerElo,
                        defaultPlayerElo,
                        defaultPlayerElo
                )),
                gameCreator = createMockGameCreator(leagueId = 2),
                leagueFinder = createMockLeagueFinder(id = 2, kFactor = 32),
                gameMakerThing = mockGameMakerThing
        )

        subject.create(buildUnsavedGame(scores = listOf(
                "Brad" to 10.0,
                "Tim" to 5.0,
                "Joe" to 2.0
        )))

        val gameWithLeague = GameWithLeague(
                game = Game(leagueId = 2),
                league = defaultLeague.copy(id = 2, kFactor = 32)
        )

        verify(mockGameMakerThing).processNewGame(
                player = brad,
                opponents = listOf(tim, joe),
                game = gameWithLeague
        )

        verify(mockGameMakerThing).processNewGame(
                player = tim,
                opponents = listOf(brad, joe),
                game = gameWithLeague
        )

        verify(mockGameMakerThing).processNewGame(
                player = joe,
                opponents = listOf(brad, tim),
                game = gameWithLeague
        )
    }

    @Test
    fun `it returns a summary of the game and the players new ratings`() {

        val one = defaultPlayerRating.copy(score = 10.0)
        val two = defaultPlayerRating.copy(score = 5.0)
        val three = defaultPlayerRating.copy(score = 2.0)

        val mockGameMakerThing: GameMakerThingInt = mock {
            on { processNewGame(any(), any(), any()) } doReturn listOf(one, two, three)
        }

        val subject = createOrchestrator(
                leagueFinder = createMockLeagueFinder(leagueName = "TestLeague"),
                gameMakerThing = mockGameMakerThing
        )

        val returnedSavedGame = subject.create(buildUnsavedGame(
                leagueName = "TestLeague",
                scores = listOf(
                        "Brad" to 10.0,
                        "Tim" to 5.0,
                        "Joe" to 2.0
                )))

        assertThat(returnedSavedGame).isEqualToComparingFieldByFieldRecursively(
                SavedGame(
                        leagueName = "TestLeague",
                        ratings = listOf(one, two, three)
                )
        )
    }

    private fun LeagueFinder.didFindByName(leagueName: String) {
        verify(this).findByName(leagueName)
    }

    private fun <T> Creator<T>.didCreate(vararg domainObjects: T) {
        domainObjects.forEach { verify(this).create(it) }
    }

    private fun createMockLeagueFinder(
            id: Long = 1,
            leagueName: String = "",
            kFactor: Int = 0
    ): LeagueFinder = mock {
        on { findByName(any()) } doReturn defaultLeague.copy(id = id, name = leagueName, kFactor = kFactor)
    }

    private fun createMockGameCreator(
            id: Long = 0,
            leagueId: Long = 0
    ): Creator<Game> {
        return mock {
            on { create(any()) } doReturn Game(id = id, leagueId = leagueId)
        }
    }

    private fun createMockPlayerBuilder(
            players: List<PlayerWithElo> = listOf(PlayerWithElo(player = defaultPlayer, elo = defaultElo))
    ): PlayerBuilder = mock {
        on { getPlayer(any(), any()) } doReturn players
    }

    private fun createOrchestrator(
            leagueFinder: LeagueFinder = createMockLeagueFinder(),
            gameCreator: Creator<Game> = createMockGameCreator(),
            playerBuilder: PlayerBuilder = createMockPlayerBuilder(),
            gameMakerThing: GameMakerThingInt = mock()
    ) = AveragedEloGameOrchestrator(
            leagueFinder = leagueFinder,
            playerBuilder = playerBuilder,
            gameCreator = gameCreator,
            gameMakerThing = gameMakerThing
    )

    private fun buildUnsavedGame(
            leagueName: String = "league-name",
            scores: List<Pair<String, Double>> = listOf("A" to 1.0, "B" to 2.0)
    ) = UnsavedGame(
            leagueName = leagueName,
            scores = scores.map { it.toNamedPlayerScore() }
    )

    private fun Pair<String, Double>.toNamedPlayerScore() = NamedPlayerScore(playerName = first, score = second)

    val defaultLeague = League(id = 1, name = "", description = "", kFactor = 0, defaultELO = 0)
    val defaultElo = EloRating(leagueId = 0, playerId = 0, scoreId = 0, version = 0, value = 0)
    val defaultPlayer = Player(id = 0, name = "")
    val defaultPlayerElo = PlayerWithElo(player = defaultPlayer, elo = defaultElo)
    val defaultPlayerScore = PlayerWithScoreAndElo(player = defaultPlayer, elo = defaultElo, score = 0.0)
    val defaultPlayerRating = PlayerRating(name = "", score = 0.0, newRating = 0, ratingChange = 0)
}