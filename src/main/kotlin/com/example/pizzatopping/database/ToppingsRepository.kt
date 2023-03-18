package com.example.pizzatopping.database

import com.example.pizzatopping.models.database.ToppingEntity
import org.springframework.data.repository.CrudRepository

interface ToppingsRepository : CrudRepository<ToppingEntity, Int> {
    fun findToppingsByName(name: String): List<ToppingEntity>
}