package com.example.imdb.controllers

import com.example.imdb.models.Actor
import com.example.imdb.services.ActorService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/actors")
@Validated
class ActorController(private val actorService: ActorService) {
    @Operation(summary = "Retrieves an actor by ID.")
    @ApiResponse(responseCode = "200", content = [Content(schema = Schema(implementation = Actor::class))])
    @GetMapping("/{actor_id}")
    fun getActor(@PathVariable("actor_id") id: Long): Actor {
        return actorService.retrieveActor(id)
    }

    @Operation(summary = "Deletes an actor by ID.")
    @ApiResponse(responseCode = "202", description = "Successfully deleted actor if it existed.")
    @ApiResponse(responseCode = "400", description = "Invalid ID format (must be numeric).")
    @ApiResponse(responseCode = "422", description = "Cannot delete actor if it is associated with any movies.")
    @DeleteMapping("/{actor_id}")
    fun deleteActor(@PathVariable("actor_id") id: Long): ResponseEntity<String> {
        actorService.deleteActor(id)
        return ResponseEntity.noContent().build()
    }
}
