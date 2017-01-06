package io.ducommun.elo.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.elo.league_orchestrator.LeagueDescriber
import io.ducommun.elo.league_orchestrator.LeagueOverview
import io.ducommun.elo.league_orchestrator.PlayerOverview
import org.junit.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class LeagueOverviewControllerTest {

    @Test
    fun `passes the league name to be described to service with a default minRounds value of 0`() {

        val mockLeagueDescriber: LeagueDescriber = mock()

        val subject = LeagueOverviewController(mockLeagueDescriber)

        val mockMvc = MockMvcBuilders.standaloneSetup(subject).build()

        mockMvc
                .perform(get("/leagues/tree"))
                .andExpect(status().isOk)

        verify(mockLeagueDescriber).describe("tree", 0)
    }

    @Test
    fun `should pass in the minRounds parameter if present`() {

        val mockLeagueDescriber: LeagueDescriber = mock()

        val subject = LeagueOverviewController(mockLeagueDescriber)

        val mockMvc = MockMvcBuilders.standaloneSetup(subject).build()

        mockMvc
                .perform(get("/leagues/tree").param("minRounds", "10"))
                .andExpect(status().isOk)

        verify(mockLeagueDescriber).describe("tree", minRounds = 10)
    }

    @Test
    fun `it returns the league overview created by the service`() {

        val leagueOverview = LeagueOverview(
                name = "tree",
                players = listOf(
                        PlayerOverview(name = "Bob", rating = 1500, rounds = 1),
                        PlayerOverview(name = "Tim", rating = 1550, rounds = 1)
                )
        )
        val contentString = ObjectMapper().writeValueAsString(leagueOverview)

        val subject = LeagueOverviewController(mock {
            on { describe(any(), any()) } doReturn leagueOverview
        })

        val mockMvc = MockMvcBuilders.standaloneSetup(subject).build()

        mockMvc
                .perform(get("/leagues/tree"))
                .andExpect(status().isOk)
                .andExpect(content().string(contentString))
    }
}