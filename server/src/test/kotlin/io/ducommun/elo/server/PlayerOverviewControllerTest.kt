package io.ducommun.elo.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.elo.league_orchestrator.EloRatingDescription
import io.ducommun.elo.league_orchestrator.PlayerDescriber
import io.ducommun.elo.league_orchestrator.PlayerDescription
import org.junit.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class PlayerOverviewControllerTest {

    @Test
    fun `passes the league name and player name to be described to service`() {

        val mockPlayerDescriber: PlayerDescriber = mock()

        val subject = PlayerOverviewController(mockPlayerDescriber)

        val mockMvc = MockMvcBuilders.standaloneSetup(subject).build()

        mockMvc
                .perform(MockMvcRequestBuilders.get("/leagues/league-name/players/player-name"))
                .andExpect(MockMvcResultMatchers.status().isOk)

        verify(mockPlayerDescriber).describePlayer(
                leagueName = "league-name",
                playerName = "player-name"
        )
    }

    @Test
    fun `it returns the league overview created by the service`() {

        val playerDescription = PlayerDescription(
                playerName = "player-name",
                leagueName = "league-name",
                eloRatings = listOf(
                        EloRatingDescription(value = 1400, version = 1, diff = "+1"),
                        EloRatingDescription(value = 1500, version = 2, diff = "-1")
                )
        )
        val contentString = ObjectMapper().writeValueAsString(playerDescription)

        val subject = PlayerOverviewController(mock {
            on { describePlayer(any(), any()) } doReturn playerDescription
        })

        val mockMvc = MockMvcBuilders.standaloneSetup(subject).build()

        mockMvc
                .perform(MockMvcRequestBuilders.get("/leagues/league-name/players/player-name"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().string(contentString))
    }
}