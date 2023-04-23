package com.example.imdb.service

import com.example.imdb.database.ActorRepository
import com.example.imdb.database.MovieRepository
import com.example.imdb.exceptions.ConflictException
import com.example.imdb.exceptions.NotFoundException
import com.example.imdb.models.Actor
import com.example.imdb.models.Movie
import com.example.imdb.services.MovieService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

/**
 * These are tests which ensure the MovieService is able to function as expected when integrating the database.
 * Mocking the database interactions would limit the value of the tests, since the primary function of the
 * service is to perform the necessary database operations to carry out each function.
 *
 */
@SpringBootTest
class MovieServiceTest {

    @Autowired
    private lateinit var movieService: MovieService

    @Autowired
    private lateinit var actorRepository: ActorRepository

    @Autowired
    private lateinit var movieRepository: MovieRepository

    private val testTitle = "Shawshenk Redemption"
    private val testTitle2 = "Avatar"

    private val testActors = setOf(Actor(id = null, name = "Ed Helms"))
    private val testActors2 = setOf(Actor(id = null, name = "Ed Helms"), Actor(id = null, name = "Keanu Reaves"))

    private val testMovie = Movie(id = null, title = testTitle, actors = testActors, releaseDate = LocalDate.of(2000, 1, 1))
    private val testMovie2 = Movie(id = null, title = testTitle2, actors = testActors2, releaseDate = LocalDate.of(2000, 1, 2))

    @BeforeEach
    fun beforeEach() {
        actorRepository.deleteAll()
        movieRepository.deleteAll()
    }

    @Test
    fun `create movie happy path`() {
        val result = movieService.createMovie(testMovie)

        verifyMovie(testMovie, result)
        assertEquals(1, actorRepository.count())
    }

    @Test
    fun `can create movies which share a title with different release dates`() {
        val result = movieService.createMovie(testMovie)
        val result2 = movieService.createMovie(testMovie2)

        verifyMovie(testMovie, result)
        verifyMovie(testMovie2, result2)
        assertEquals(2, actorRepository.count())
    }

    @Test
    fun `can create movies which share a release date with different titles`() {
        val result = movieService.createMovie(testMovie)
        val result2 = movieService.createMovie(testMovie2)

        verifyMovie(testMovie, result)
        verifyMovie(testMovie2, result2)
    }

    @Test
    fun `conflict thrown when creating movies with shared title and release date`() {
        movieService.createMovie(testMovie)

        assertThrows<ConflictException> { movieService.createMovie(testMovie) }
    }

    @Test
    fun `get movie happy path`() {
        val movie = movieService.createMovie(testMovie)
        val result = movieService.retrieveMovie(movie.id!!)

        verifyMovie(movie, result)
    }

    @Test
    fun `get movie not found`() {
        assertThrows<NotFoundException> { movieService.retrieveMovie(2L) }
    }

    private fun verifyMovie(expected: Movie, result: Movie) {
        assertNotNull(result.id)
        assertEquals(expected.title, result.title)
        assertEquals(expected.releaseDate, result.releaseDate)

        assertNotNull(result.actors)
        assertEquals(expected.actors!!.size, result.actors!!.size)
        val expectedIt = expected.actors!!.iterator()
        val resultIt = result.actors!!.iterator()
        while (expectedIt.hasNext()) {
            assertEquals(expectedIt.next().name, resultIt.next().name)
        }
    }
}
