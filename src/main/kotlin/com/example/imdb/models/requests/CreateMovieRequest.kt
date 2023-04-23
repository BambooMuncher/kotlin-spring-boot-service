package com.example.imdb.models.requests

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class CreateMovieRequest(
    @field:NotNull
    @field:Size(min = 1, max = 100)
    @field:Schema(example = "Pulp Fiction")
    val title: String,

    @field:JsonDeserialize(using = LocalDateDeserializer::class)
    @field:JsonSerialize(using = LocalDateSerializer::class)
    @field:NotNull
    @field:Schema(example = "1994–10-14")
    val releaseDate: LocalDate,

    @field:NotNull
    @field:Size(min = 1, max = 100)
    @field:Schema(
        example =
        """[
            "John Travolta",
            "Uma Thurman",
            "Samuel L. Jackson"
        ]"""
    )
    val actors: Set<String>
)
