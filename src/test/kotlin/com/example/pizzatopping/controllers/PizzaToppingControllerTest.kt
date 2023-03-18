package com.example.pizzatopping.controllers

import com.example.pizzatopping.models.requests.SubmitPizzaToppingsRequest
import com.example.pizzatopping.services.PizzaToppingService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest
class PizzaToppingControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var pizzaToppingService: PizzaToppingService

    private val objectMapper = ObjectMapper()

    @Test
    fun `empty email results in 400 response from submit pizza topping end-point`() {
        val request = SubmitPizzaToppingsRequest(emailAddress = "", toppings = listOf(), favoriteTopping = null)

        mockMvc
            .perform(
                put("/api/v1/pizzas/toppings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `invalid email results in 400 response from submit pizza topping end-point`() {
        val request = SubmitPizzaToppingsRequest(emailAddress = "invalid", toppings = listOf(), favoriteTopping = null)

        mockMvc
            .perform(
                put("/api/v1/pizzas/toppings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `empty favorite topping results in 400 response from submit pizza topping end-point`() {
        val request = SubmitPizzaToppingsRequest(emailAddress = "valid@legit.com", toppings = listOf(), favoriteTopping = "")

        mockMvc
            .perform(
                put("/api/v1/pizzas/toppings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `valid email results in 204 response from submit pizza topping end-point`() {
        val request = SubmitPizzaToppingsRequest(emailAddress = "valid@legit.com", toppings = listOf(), favoriteTopping = null)

        mockMvc
            .perform(
                put("/api/v1/pizzas/toppings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNoContent)
    }
}