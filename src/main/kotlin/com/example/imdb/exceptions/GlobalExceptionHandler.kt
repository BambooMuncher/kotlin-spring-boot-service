package com.example.imdb.exceptions

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
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
            .map { "${it.field}: ${it.defaultMessage}" }
            .toList()

        body["errors"] = errors

        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.CONFLICT) // 409
    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException): ResponseEntity<Any>? {
        return ResponseEntity(ex.message, HttpStatus.CONFLICT)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException): ResponseEntity<Any>? {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // 404
    @ExceptionHandler(UnprocessableException::class)
    fun handleNotFound(ex: UnprocessableException): ResponseEntity<Any>? {
        return ResponseEntity(ex.message, HttpStatus.UNPROCESSABLE_ENTITY)
    }
}
