package com.example.imdb.controllers

import com.example.imdb.models.Actor
import com.example.imdb.models.Movie
import com.example.imdb.models.requests.CreateMovieRequest
import com.example.imdb.services.MovieService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/movies")
@Validated
class MovieController(private val movieService: MovieService) {

    @Operation(
        summary = "Attempts to create a new movie.",
        description = "All movies must have at least one actor."
    )
    @ApiResponse(responseCode = "201", description = "Successfully created movie")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "409", description = "Movie already exists with given title and release date.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createMovie(
        @Valid
        @RequestBody
        request: CreateMovieRequest
    ): Movie {
        val actors = request.actors.map { Actor(name = it) }.toSet()
        return movieService.createMovie(
            movie = Movie(title = request.title, releaseDate = request.releaseDate, actors = actors)
        )
    }

    @Operation(summary = "Retrieves a movie by ID.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved movie.")
    @ApiResponse(responseCode = "400", description = "Invalid ID format (must be numeric).")
    @ApiResponse(responseCode = "404", description = "Movie not found with given ID.")
    @GetMapping("/{movie_id}")
    fun getMovie(@PathVariable("movie_id") id: Long): Movie {
        return movieService.retrieveMovie(id)
    }

    @Operation(summary = "Deletes a movie by ID.")
    @ApiResponse(responseCode = "202", description = "Successfully deleted movie if it existed.")
    @ApiResponse(responseCode = "400", description = "Invalid ID format (must be numeric).")
    @DeleteMapping("/{movie_id}")
    fun deleteMovie(@PathVariable("movie_id") id: Long): ResponseEntity<String> {
        movieService.deleteMovie(id)
        return ResponseEntity.noContent().build()
    }
}
