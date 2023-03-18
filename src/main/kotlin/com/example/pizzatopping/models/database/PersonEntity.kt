package com.example.pizzatopping.models.database

import jakarta.persistence.*


@Entity(name="people")
class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    lateinit var email: String

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "submitted_toppings",
        joinColumns = [JoinColumn(name = "person_id")],
        inverseJoinColumns = [JoinColumn(name = "topping_id")]
    )
   lateinit var submittedToppings: MutableList<ToppingEntity>

    @JoinColumn(name = "favorite_topping")
    @ManyToOne(cascade = [CascadeType.ALL])
    var favoriteTopping: ToppingEntity? = null


    companion object {
        fun from(email: String): PersonEntity {
            val person = PersonEntity()
            person.email = email
            person.submittedToppings = mutableListOf()
            return person
        }
    }

}