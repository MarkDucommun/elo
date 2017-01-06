package io.ducommun.elo.server

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Before
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

abstract class CreationControllerTest<inputType, outputType> {

    abstract val input: inputType
    abstract val output: outputType

    abstract val subject: Any
    abstract val endpoint: String

    lateinit var mockMvc : MockMvc

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject).build()
    }

    @Test
    fun create_shouldReturnTheOutput() {

        sendCreateRequest().andConfirmGameWasReturned()
    }

    fun sendCreateRequest() = mockMvc
            .perform(MockMvcRequestBuilders.post(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(input!!.toJsonString()))!!

    private fun ResultActions.andConfirmGameWasReturned() {
        andExpect(MockMvcResultMatchers.status().isCreated)
        andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        andExpect(MockMvcResultMatchers.content().string(output!!.toJsonString()))
    }

    fun Any.toJsonString() = ObjectMapper().writeValueAsString(this)
}