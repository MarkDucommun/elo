package io.ducommun.elo.server

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.elo.new_game_orchestrator.*
import io.ducommun.elo.new_game_orchestrator.domain.SavedGame
import io.ducommun.elo.new_game_orchestrator.domain.UnsavedGame
import org.junit.Test

class GameCreationControllerTest : CreationControllerTest<UnsavedGame, SavedGame>() {

    override val input = createUnsavedGame()
    override val output = createSavedGame()

    val mockGameOrchestrator = createMockGameOrchestrator()
    override val subject = GameCreationController(mockGameOrchestrator)
    override val endpoint = "/games"

    @Test
    fun create_shouldDelegateToGameService() {

        sendCreateRequest()

        verifyGameWasSaved()
    }

    private fun verifyGameWasSaved() {
        verify(mockGameOrchestrator).create(input)
    }

    private fun createMockGameOrchestrator() = mock<GameOrchestrator> {
        // TODO look into this in greater detail, mockito cannot handle having a list in a matcher
        on { create(input) } doReturn output
    }

    private fun createUnsavedGame() = UnsavedGame(
            leagueName = "test-league",
            scores = listOf(
                    NamedPlayerScore("Player One", 10.0),
                    NamedPlayerScore("Player Two", 5.0)
            )
    )

    private fun createSavedGame() = SavedGame(
            leagueName = "test-league",
            ratings = listOf(
                    PlayerRating("Player One", 10.0, 1515, 15),
                    PlayerRating("Player Two", 5.0, 1485, -15)
            )
    )
}
