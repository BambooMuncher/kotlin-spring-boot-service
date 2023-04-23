package com.example.imdb.service

import com.example.imdb.database.ActorRepository
import com.example.imdb.database.MovieRepository
import com.example.imdb.exceptions.NotFoundException
import com.example.imdb.exceptions.UnprocessableException
import com.example.imdb.models.Actor
import com.example.imdb.models.Movie
import com.example.imdb.services.ActorService
import com.example.imdb.services.MovieService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

/**
 * These are tests which ensure the ActorService is able to function as expected when integrating the database.
 * Mocking the database interactions would limit the value of the tests, since the primary function of the
 * service is to perform the necessary database operations to carry out each function.
 *
 */
@SpringBootTest
class ActorServiceTest {
    @Autowired
    private lateinit var movieService: MovieService

    @Autowired
    private lateinit var actorRepository: ActorRepository

    @Autowired
    private lateinit var movieRepository: MovieRepository

    @Autowired
    private lateinit var actorService: ActorService

    private val testTitle = "Shawshenk Redemption"
    private val testActor = Actor(id = null, name = "Ed Helms")
    private val testActors = setOf(testActor)
    private val testMovie = Movie(id = null, title = testTitle, actors = testActors, releaseDate = LocalDate.of(2000, 1, 1))

    @BeforeEach
    fun beforeEach() {
        actorRepository.deleteAll()
        movieRepository.deleteAll()
    }

    @Test
    fun `get actor happy path`() {
        val movie = movieService.createMovie(testMovie)
        val result = actorService.retrieveActor(movie.actors!!.iterator().next().id!!)

        assertNotNull(result.id)
        assertEquals(testActor.name, result.name)
        assertEquals(1, actorRepository.count())
    }

    @Test
    fun `get actor not found`() {
        assertThrows<NotFoundException> { actorService.retrieveActor(1L) }
        assertEquals(0, actorRepository.count())
    }

    @Test
    fun `delete actor happy path`() {
        val movie = movieService.createMovie(testMovie)
        movieService.deleteMovie(movie.id!!)

        assertDoesNotThrow { actorService.deleteActor(movie.actors!!.iterator().next().id!!) }
        assertEquals(0, actorRepository.count())
    }

    @Test
    fun `delete actor not found does not cause exception`() {
        assertDoesNotThrow { actorService.deleteActor(1L) }
        assertEquals(0, actorRepository.count())
    }

    @Test
    fun `delete actor unprocessable when movies remain which actor is a part of`() {
        val movie = movieService.createMovie(testMovie)

        assertThrows<UnprocessableException> { actorService.deleteActor(movie.actors!!.iterator().next().id!!) }
        assertEquals(1, actorRepository.count())
    }
}
