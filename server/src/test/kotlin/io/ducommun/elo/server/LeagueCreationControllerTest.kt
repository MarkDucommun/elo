package io.ducommun.elo.server

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.elo.leagues.League
import io.ducommun.elo.shared.Creator
import org.junit.Test

class LeagueCreationControllerTest : CreationControllerTest<League, League>() {

    override val input = League(name = "name", description = "description", kFactor = 0, defaultELO = 1500)
    override val output = input.copy(id = 1)

    val mockLeagueCreator = createMockLeagueCreator()
    override val subject = LeagueCreationController(mockLeagueCreator)
    override val endpoint = "/leagues"

    @Test
    fun create_shouldDelegateToLeagueService() {

        sendCreateRequest()

        verifyLeagueWasSaved()
    }

    private fun verifyLeagueWasSaved() {
        verify(mockLeagueCreator).create(input)
    }

    private fun createMockLeagueCreator() = mock<Creator<League>>() {
        on { create(any<League>()) } doReturn output
    }
}