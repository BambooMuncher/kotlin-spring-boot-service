package com.example.pizzatopping.models.database

import jakarta.persistence.*

@Entity(name = "toppings")
class ToppingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    lateinit var name: String

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "submittedToppings")
    lateinit var peopleSubmittedBy: MutableList<PersonEntity>

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "favoriteTopping")
    lateinit var peopleFavoritedBy: MutableList<PersonEntity>

    companion object {
        fun from(name: String): ToppingEntity {
            val topping = ToppingEntity()
            topping.name = name
            return topping
        }
    }
}
