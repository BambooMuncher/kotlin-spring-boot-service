package com.example.pizzatopping.exceptions

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val body: MutableMap<String, List<String?>> = mutableMapOf()

        val errors: List<String?> = ex.bindingResult
            .fieldErrors
            .map{ "${it.field}: ${it.defaultMessage}" }
            .toList()

        body["errors"] =  errors

        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }
}