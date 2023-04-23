package com.example.imdb.models

import io.swagger.v3.oas.annotations.media.Schema

data class Actor(

    @field:Schema(example = "1")
    val id: Long? = null,

    @field:Schema(example = "Brad Pitt")
    var name: String
)
