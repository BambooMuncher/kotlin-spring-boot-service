package com.example.pizzatopping.controllers

import com.example.pizzatopping.models.requests.SubmitPizzaToppingsRequest
import com.example.pizzatopping.models.responses.GetPizzaToppingResultsResponse
import com.example.pizzatopping.services.PizzaToppingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/pizzas/toppings")
@Validated
class PizzaToppingController(private val pizzaToppingService: PizzaToppingService) {

    @Operation(
        summary = "Submit pizza toppings",
        description = "Re-submitting toppings for the same email replaces the toppings submitted. Also allows for submitting favorite topping. A valid email format will be required for the `emailAddress` field."
    )
    @ApiResponse(responseCode = "204", description = "Successfully submitted toppings")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PutMapping
    fun submitToppings(
        @Valid
        @RequestBody
        request: SubmitPizzaToppingsRequest
    ): ResponseEntity<String> {
        pizzaToppingService.submitToppings(
            rawEmail = request.emailAddress,
            toppings = request.toppings,
            favoriteTopping = request.favoriteTopping
        )

        // could return attributes of the person here potentially in the response body if it's helpful
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Get report of pizza topping submission results", description = "Provides total times submitted and favorited per topping.")
    @ApiResponse(responseCode = "200", description = "Successfully submitted toppings")
    @GetMapping("/results")
    fun getPizzaToppingResults(): GetPizzaToppingResultsResponse {
        val submissionCountsByTopping = pizzaToppingService.retrieveSubmissionCountsByTopping()
        val peopleCount = pizzaToppingService.retrievePeopleCount()
        val totalToppings = submissionCountsByTopping.size.toLong()

        return GetPizzaToppingResultsResponse(
            resultsByTopping = submissionCountsByTopping,
            totalSubmissions = peopleCount,
            totalToppings = totalToppings
        )
    }
}
