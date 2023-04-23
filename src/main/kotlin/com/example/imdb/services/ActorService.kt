package com.example.imdb.services

import com.example.imdb.database.ActorRepository
import com.example.imdb.exceptions.NotFoundException
import com.example.imdb.exceptions.UnprocessableException
import com.example.imdb.models.Actor
import com.example.imdb.models.database.ActorEntity
import org.springframework.stereotype.Service

@Service
class ActorService(private val actorRepository: ActorRepository) {
    fun retrieveActor(id: Long): Actor {
        val actorEntityOpt = actorRepository.findById(id.toInt())

        if (actorEntityOpt.isEmpty) {
            throw NotFoundException("Actor with ID of $id not found.")
        }

        val actorEntity = actorEntityOpt.get()
        return ActorEntity.toActor(actorEntity)
    }

    fun deleteActor(id: Long) {
        val actorOpt = actorRepository.findById(id.toInt())
        if (actorOpt.isEmpty) {
            return
        }

        val actor = actorOpt.get()
        if (actor.performances.isNotEmpty()) {
            throw UnprocessableException("Cannot delete actor associated with an existing movie.")
        }

        actorRepository.deleteById(id.toInt())
    }
}
