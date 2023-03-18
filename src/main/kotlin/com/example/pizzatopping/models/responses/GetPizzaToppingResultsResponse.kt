package com.example.pizzatopping.models.responses

data class GetPizzaToppingResultsResponse(
    val resultsByTopping: Map<String, ToppingResult>,
    val totalSubmissions: Long,
    val totalToppings: Long
)
