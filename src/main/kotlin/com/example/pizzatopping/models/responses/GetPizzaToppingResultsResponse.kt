package com.example.pizzatopping.models.responses

import io.swagger.v3.oas.annotations.media.Schema

data class GetPizzaToppingResultsResponse(
    @field:Schema(
        example =
        """{
            "pineapple": {
                "totalTimesSubmitted": 20391,
                "totalTimesFavorited": 4239
            },
            "jalapeno": {
                "totalTimesSubmitted": 451,
                "totalTimesFavorited": 52
            },
            "sausage": {
                "totalTimesSubmitted": 6,
                "totalTimesFavorited": 2
            },
            "pepperoni": {
                "totalTimesSubmitted": 2,
                "totalTimesFavorited": 0
            }
        }"""
    )
    val resultsByTopping: Map<String, ToppingResult>,

    @field:Schema(example = "7932")
    val totalSubmissions: Long,

    @field:Schema(example = "4")
    val totalToppings: Long
)
