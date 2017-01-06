package io.ducommun.elo.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.elo.players.Player
import io.ducommun.elo.shared.Creator
import org.junit.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class PlayerCreationControllerTest : CreationControllerTest<Player, Player>() {

    override val input = Player(name = "Bob")
    override val output = input.copy(id = 1)

    val mockPlayerCreator = createMockPlayerCreator()
    override val subject = PlayerCreationController(mockPlayerCreator)
    override val endpoint = "/players"

    @Test
    fun create_shouldDelegateToPlayerService() {

        sendCreateRequest()

        verifyPlayerWasSaved()
    }

    private fun verifyPlayerWasSaved() {
        verify(mockPlayerCreator).create(input)
    }

    private fun createMockPlayerCreator() = mock<Creator<Player>>() {
        on { create(any<Player>()) } doReturn output
    }
}