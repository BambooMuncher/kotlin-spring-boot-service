package com.example.pizzatopping.database

import com.example.pizzatopping.models.database.PersonEntity
import org.springframework.data.repository.CrudRepository

interface PersonRepository : CrudRepository<PersonEntity, Int> {
    fun findPeopleByEmail(email: String): List<PersonEntity>
}
