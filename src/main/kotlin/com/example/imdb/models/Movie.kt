package com.example.imdb.models

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class Movie(

    @field:Schema(example = "1")
    val id: Long? = null,

    @field:Schema(example = "Pulp Fiction")
    var title: String,

    @field:JsonDeserialize(using = LocalDateDeserializer::class)
    @field:JsonSerialize(using = LocalDateSerializer::class)
    @field:Schema(example = "1994–10-14")
    val releaseDate: LocalDate,

    @field:Schema(
        example = """
    [
        {
            "id": 1,
            "name": "John Travolta"
        },
        {
            "id": 2,
            "name": "Uma Thurman"
        },
        {
            "id": 3,
            "name": "Samuel L. Jackson"
        }
    ]
        """
    )
    val actors: Set<Actor>?
)
