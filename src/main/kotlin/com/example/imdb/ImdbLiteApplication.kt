package com.example.imdb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ImdbLiteApplication

fun main(args: Array<String>) {
    runApplication<ImdbLiteApplication>(*args)
}
