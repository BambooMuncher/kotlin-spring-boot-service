package com.example.imdb.database

import com.example.imdb.models.database.ActorEntity
import org.springframework.data.repository.CrudRepository

interface ActorRepository : CrudRepository<ActorEntity, Int> {
    fun findActorByLowercaseName(lowercaseName: String): ActorEntity?
}
