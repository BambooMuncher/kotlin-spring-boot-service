package com.example.pizzatopping.models.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class SubmitPizzaToppingsRequest(
    @field:Email(regexp = ".+[@].+[\\.].+")
    @field:Size(min = 1, max = 100)
    val emailAddress: String,

    /*
     * Topping could be an enum, but in this case, we want to discover which toppings customers are interested in, which
     * means we want to support storing any topping values which are submitted. This way we allow for topping values we
     * don't originally expect. Once the toppings become established choices we would likely want an enum.
     *
     * We are currently not validating the size of the strings within topping list. This could be added if needed.
     */
    @field:Size(min = 0, max = 100)
    val toppings: List<String>,

    /*
     * We could consider making toppings a list of Topping objects and adding favorite as an attribute of the toppings.
     * It's possible we will get a favorite which isn't also in the list of submitted toppings. When that happens, we
     * will include the favorite topping as part of the topping submission.
     */
    @field:Size(min = 1, max = 100)
    val favoriteTopping: String?
)
