package io.ducommun.elo.league_orchestrator

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.elo.leagues.League
import io.ducommun.elo.new_game_orchestrator.GameOrchestrator
import io.ducommun.elo.new_game_orchestrator.domain.NamedPlayerScore
import io.ducommun.elo.new_game_orchestrator.domain.UnsavedGame
import io.ducommun.elo.players.Player
import io.ducommun.elo.shared.Creator
import org.junit.Test

class CsvLoaderLeagueOrchestratorTest {

    val gameOneScores = createListOfGames(listOf("Mark", "Phil"), listOf(20.0, 30.0))
    val gameTwoScores = createListOfGames(listOf("Tim", "Phil"), listOf(30.0, 20.0))

    val listOfGames = listOf(
            gameOneScores,
            gameTwoScores
    )

    @Test
    fun `it gets the raw data from the CSV file supplied`() {

        val fileLocation = "file-location"

        val mockFileLoader: FileLoader = mock()

        val subject = createLeagueOrchestrator(
                fileLoader = mockFileLoader
        )

        subject.create(LeagueSeed(fileLocation, 0, 0))

        verify(mockFileLoader).load("file-location")
    }

    @Test
    fun `it creates a temporary league using default values for elo and kfactor`() {

        val DEFAULT_KFACTOR = 20
        val DEFAULT_ELO = 1500

        val mockLeagueCreator: Creator<League> = mock()

        val subject = createLeagueOrchestrator(leagueCreator = mockLeagueCreator)

        subject.create(LeagueSeed("", DEFAULT_KFACTOR, DEFAULT_ELO))

        verify(mockLeagueCreator).create(League(
                name = "temp",
                description = "a temporary league",
                kFactor = DEFAULT_KFACTOR,
                defaultELO = DEFAULT_ELO
        ))
    }

    @Test
    fun `it creates a temporary league with injected kfactor and default elo values`() {

        val kFactor = 1600
        val defaultElo = 30

        val mockLeagueCreator: Creator<League> = mock()

        val subject = createLeagueOrchestrator(
                leagueCreator = mockLeagueCreator
        )

        subject.create(LeagueSeed(
                fileLocation = "",
                kFactor = kFactor,
                defaultElo = defaultElo
        ))

        verify(mockLeagueCreator).create(League(
                name = "temp",
                description = "a temporary league",
                kFactor = kFactor,
                defaultELO = defaultElo
        ))
    }

    @Test
    fun `it creates each Player in the temporary League`() {

        val mockPlayerCreator: Creator<Player> = mock()

        val subject = createLeagueOrchestrator(
                fileLoader = createMockFileLoader(returnedGames = listOfGames),
                playerCreator = mockPlayerCreator
        )

        subject.create(LeagueSeed("", 0, 0))

        verify(mockPlayerCreator).create(Player(name = "Mark"))
        verify(mockPlayerCreator).create(Player(name = "Tim"))
        verify(mockPlayerCreator).create(Player(name = "Phil"))
    }

    @Test
    fun `it translates the raw lists of scores into UnsavedGames and passes those to the GameOrchestrator`() {

        val mockGameOrchestrator: GameOrchestrator = mock()

        val subject = createLeagueOrchestrator(
                fileLoader = createMockFileLoader(returnedGames = listOfGames),
                gameOrchestrator = mockGameOrchestrator
        )

        subject.create(LeagueSeed("", 0, 0))

        verify(mockGameOrchestrator).create(UnsavedGame(
                leagueName = "temp",
                scores = gameOneScores
        ))

        verify(mockGameOrchestrator).create(UnsavedGame(
                leagueName = "temp",
                scores = gameTwoScores
        ))
    }

    @Test
    fun `it returns a list of all of the Players EloRatings`() {

        val subject = createLeagueOrchestrator(
                fileLoader = createMockFileLoader(listOfGames)
        )

        subject.create(LeagueSeed("", 0, 0))
    }

    private fun createLeagueOrchestrator(
            fileLoader: FileLoader = mock(),
            leagueCreator: Creator<League> = createMockLeagueCreator(),
            playerCreator: Creator<Player> = mock(),
            gameOrchestrator: GameOrchestrator = mock()
    ) = CsvLoaderLeagueOrchestrator(
            fileLoader,
            leagueCreator,
            playerCreator,
            gameOrchestrator
    )

    private fun createMockFileLoader(
            returnedGames: List<List<NamedPlayerScore>>
    ): FileLoader = mock {
        on { load(any()) } doReturn returnedGames
    }

    private fun createMockLeagueCreator(): Creator<League> = mock {
        on { create(any()) } doReturn League(
                id = 1,
                name = "temp",
                description = "a temporary league",
                kFactor = 20,
                defaultELO = 1500
        )
    }
}