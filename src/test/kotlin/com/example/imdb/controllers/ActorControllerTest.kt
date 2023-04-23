package com.example.imdb.controllers

import com.example.imdb.exceptions.UnprocessableException
import com.example.imdb.models.Actor
import com.example.imdb.services.ActorService
import com.example.imdb.services.MovieService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
class ActorControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var movieService: MovieService

    @MockBean
    lateinit var actorService: ActorService

    private val objectMapper = ObjectMapper()

    private val testActor = Actor(id = 1, name = "Ed Helms")

    @BeforeEach
    fun before() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `invalid ID format results in 400 from get actor end-point`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/actors/hello")
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `valid input results in 200 from get actor end-point`() {
        whenever(actorService.retrieveActor(eq(1L))).thenReturn(testActor)

        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/actors/1")
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testActor.id.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(testActor.name))
    }

    @Test
    fun `invalid ID format results in 400 from delete actor end-point`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/api/v1/actors/hello")
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `unprocessable exception results in 422 from delete actor end-point`() {
        whenever(actorService.deleteActor(eq(testActor.id!!))).thenThrow(UnprocessableException("test"))

        mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/api/v1/actors/${testActor.id}")
            )
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity)
    }

    @Test
    fun `valid input results in 200 from delete actor end-point`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/actors/${testActor.id}")
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}
