package com.example.imdb.controllers

import com.example.imdb.exceptions.ConflictException
import com.example.imdb.exceptions.NotFoundException
import com.example.imdb.models.Actor
import com.example.imdb.models.Movie
import com.example.imdb.models.requests.CreateMovieRequest
import com.example.imdb.services.ActorService
import com.example.imdb.services.MovieService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate

@WebMvcTest
class MovieControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var movieService: MovieService

    @MockBean
    lateinit var actorService: ActorService

    private val objectMapper = ObjectMapper()

    private val testActors = setOf("Ed Helms", "Jack Black", "Russel Crowe")
    private val testActorResponse = setOf(Actor(id = 1, name = "Ed Helms"))

    private val testTitle = "Shawshenk Redemption"

    private val testMovie = Movie(id = 1L, title = testTitle, actors = testActorResponse, releaseDate = LocalDate.of(2000, 1, 1))

    @BeforeEach
    fun before() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `empty title results in 400 response from create movie end-point`() {
        val request = CreateMovieRequest(title = "", releaseDate = LocalDate.now(), actors = testActors)

        mockMvc
            .perform(
                post("/api/v1/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `empty actor list results in 400 response from create movie end-point`() {
        val request = CreateMovieRequest(title = testTitle, releaseDate = LocalDate.now(), actors = setOf())

        mockMvc
            .perform(
                post("/api/v1/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `conflict results in 409 response from create movie end-point`() {
        val request = CreateMovieRequest(title = testTitle, releaseDate = LocalDate.now(), actors = testActors)
        whenever(movieService.createMovie(any())).thenThrow(ConflictException("test"))

        mockMvc
            .perform(
                post("/api/v1/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isConflict)
    }

    @Test
    fun `valid input results in 201 response from create movie end-point`() {
        val request = CreateMovieRequest(title = testTitle, releaseDate = LocalDate.now(), actors = testActors)

        mockMvc
            .perform(
                post("/api/v1/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated)
    }

    @Test
    fun `valid input results in 200 from get movie end-point`() {
        whenever(movieService.retrieveMovie(eq(1L))).thenReturn(testMovie)

        mockMvc
            .perform(
                get("/api/v1/movies/1")
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.title").value(testTitle))
    }

    @Test
    fun `invalid ID format results in 400 from get movie end-point`() {
        mockMvc
            .perform(
                get("/api/v1/movies/hello")
            )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `not found results in 404 from get movie end-point`() {
        whenever(movieService.retrieveMovie(eq(1L))).thenThrow(NotFoundException("test"))

        mockMvc
            .perform(
                get("/api/v1/movies/1")
            )
            .andExpect(status().isNotFound)
    }
}
