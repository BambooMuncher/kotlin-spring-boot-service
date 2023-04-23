package com.example.imdb.database

import com.example.imdb.models.database.MovieEntity
import org.springframework.data.repository.CrudRepository
import java.time.LocalDate

interface MovieRepository : CrudRepository<MovieEntity, Int> {
    fun findMovieByLowercaseTitleAndReleaseDate(lowercaseTitle: String, releaseDate: LocalDate): MovieEntity?
}
