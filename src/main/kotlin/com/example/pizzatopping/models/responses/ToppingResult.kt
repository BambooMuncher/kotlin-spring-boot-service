package com.example.pizzatopping.models.responses

data class ToppingResult(
    val totalTimesSubmitted: Long,
    val totalTimesFavorited: Long
) : Comparable<ToppingResult> {
    override fun compareTo(other: ToppingResult) = compareValuesBy(this, other, { it.totalTimesSubmitted }, { it.totalTimesFavorited })
}
