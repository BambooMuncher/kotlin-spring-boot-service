package com.example.imdb.models.database

import com.example.imdb.models.Actor
import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity(name = "actors")
class ActorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    lateinit var name: String

    @Column(name = "lowercase_name")
    lateinit var lowercaseName: String

    @Column(name = "created_at")
    lateinit var createdAt: ZonedDateTime

    @Column(name = "last_updated_at")
    lateinit var lastUpdatedAt: ZonedDateTime

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "performances")
    lateinit var performances: MutableSet<MovieEntity>

    companion object {
        fun from(actor: Actor, now: ZonedDateTime): ActorEntity {
            val actorEntity = ActorEntity()
            actorEntity.name = actor.name
            actorEntity.lowercaseName = actor.name.lowercase()
            actorEntity.createdAt = now
            actorEntity.lastUpdatedAt = now
            return actorEntity
        }

        fun toActor(actorEntity: ActorEntity): Actor {
            return Actor(id = actorEntity.id, name = actorEntity.name)
        }
    }
}
