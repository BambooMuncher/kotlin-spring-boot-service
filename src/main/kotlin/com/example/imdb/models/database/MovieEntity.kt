package com.example.imdb.models.database

import com.example.imdb.models.Movie
import jakarta.persistence.*
import java.time.LocalDate
import java.time.ZonedDateTime

@Entity(name = "movies")
class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    lateinit var title: String

    @Column(name = "lowercase_title")
    lateinit var lowercaseTitle: String

    @Column(name = "release_date")
    lateinit var releaseDate: LocalDate

    @Column(name = "created_at")
    lateinit var createdAt: ZonedDateTime

    @Column(name = "last_updated_at")
    lateinit var lastUpdatedAt: ZonedDateTime

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "performances",
        joinColumns = [JoinColumn(name = "movie_id")],
        inverseJoinColumns = [JoinColumn(name = "actor_id")]
    )
    lateinit var performances: MutableSet<ActorEntity>

    companion object {
        fun from(movie: Movie, now: ZonedDateTime): MovieEntity {
            val movieEntity = MovieEntity()
            movieEntity.title = movie.title
            movieEntity.lowercaseTitle = movie.title.lowercase()
            movieEntity.releaseDate = movie.releaseDate
            movieEntity.lastUpdatedAt = now
            movieEntity.createdAt = now
            return movieEntity
        }

        fun toMovie(movieEntity: MovieEntity): Movie {
            val actors = movieEntity.performances.map { ActorEntity.toActor(it) }.toSet()
            return Movie(id = movieEntity.id, title = movieEntity.title, releaseDate = movieEntity.releaseDate, actors = actors)
        }
    }
}
