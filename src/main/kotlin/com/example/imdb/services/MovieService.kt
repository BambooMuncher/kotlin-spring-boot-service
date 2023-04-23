package com.example.imdb.services

import com.example.imdb.database.ActorRepository
import com.example.imdb.database.MovieRepository
import com.example.imdb.exceptions.ConflictException
import com.example.imdb.exceptions.NotFoundException
import com.example.imdb.models.Actor
import com.example.imdb.models.Movie
import com.example.imdb.models.database.ActorEntity
import com.example.imdb.models.database.MovieEntity
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class MovieService(
    private val actorRepository: ActorRepository,
    private val movieRepository: MovieRepository
) {
    companion object {
        private const val MAX_ACTOR_NAME_LENGTH = 100
    }

    @Transactional
    fun createMovie(movie: Movie): Movie {
        // sanitize input
        movie.title = movie.title.trim()
        var movieEntity = movieRepository.findMovieByLowercaseTitleAndReleaseDate(movie.title.lowercase(), movie.releaseDate)

        if (movieEntity != null) {
            throw ConflictException(message = "Movie already exists with the given 'title' and 'releaseDate'.")
        }

        val now = ZonedDateTime.now()
        movieEntity = MovieEntity.from(movie = movie, now)

        movieEntity.performances = movie.actors!!.map { findOrCreateActor(it, now) }.toMutableSet()

        movieRepository.save(movieEntity)

        return MovieEntity.toMovie(movieEntity)
    }

    private fun findOrCreateActor(actor: Actor, now: ZonedDateTime): ActorEntity {
        // sanitize input - probably overkill to fail out the whole request
        actor.name = actor.name.trim().take(MAX_ACTOR_NAME_LENGTH)
        var actorEntity = actorRepository.findActorByLowercaseName(actor.name.lowercase())

        if (actorEntity != null) {
            return actorEntity
        }

        actorEntity = ActorEntity.from(actor, now)
        actorRepository.save(actorEntity)
        return actorEntity
    }

    fun retrieveMovie(id: Long): Movie {
        val movieEntityOpt = movieRepository.findById(id.toInt())

        if (movieEntityOpt.isEmpty) {
            throw NotFoundException("Movie not found.")
        }

        return MovieEntity.toMovie(movieEntityOpt.get())
    }

    fun deleteMovie(id: Long) {
        movieRepository.deleteById(id.toInt())
    }
}
