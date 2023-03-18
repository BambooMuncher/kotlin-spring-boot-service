package com.example.pizzatopping.services

import com.example.pizzatopping.database.PersonRepository
import com.example.pizzatopping.database.ToppingsRepository
import com.example.pizzatopping.models.database.PersonEntity
import com.example.pizzatopping.models.database.ToppingEntity
import com.example.pizzatopping.models.responses.ToppingResult
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PizzaToppingService(
    private val personRepository: PersonRepository,
    private val toppingsRepository: ToppingsRepository
) {

    @Transactional
    fun submitToppings(rawEmail: String, toppings: List<String>, favoriteTopping: String?) {
        val email = rawEmail.lowercase()

        var person = personRepository.findPeopleByEmail(email).firstOrNull()

        if(person == null) {
            person = PersonEntity.from(email = email)
        }
        else {
            person.submittedToppings = mutableListOf()
            person.favoriteTopping = null
        }

        var favoriteToppingEntity: ToppingEntity? = null

        toppings.forEach {
            val topping = submitTopping(rawToppingName = it, person = person)
            if(it == favoriteTopping) {
                favoriteToppingEntity = topping
            }
        }

        /*
         * We only need to submit the topping here if we have a favorite which wasn't included in the toppings list.
         * This lets us support submitting the favorite topping as a topping regardless of whether it's in the list.
         */
        if(favoriteTopping != null && favoriteToppingEntity == null) {
            favoriteToppingEntity = submitTopping(rawToppingName = favoriteTopping, person = person)
        }

        person.favoriteTopping = favoriteToppingEntity
        personRepository.save(person)
    }

    private fun submitTopping(rawToppingName: String, person: PersonEntity): ToppingEntity {
        // simple way to prevent duplicates due to casing and sanitize data
        val toppingName = rawToppingName.lowercase()
        var topping = toppingsRepository.findToppingsByName(toppingName).firstOrNull()
        if(topping == null) {
            topping = ToppingEntity.from(name = toppingName)
            toppingsRepository.save(topping)
        }

        person.submittedToppings.add(topping)

        return topping
    }

    @Transactional
    fun retrieveSubmissionCountsByTopping(): Map<String, ToppingResult> {
        val toppings = toppingsRepository.findAll()
        return toppings.associate {
            it.name to ToppingResult(it.peopleSubmittedBy.size.toLong(), it.peopleFavoritedBy.size.toLong())
        }
    }

    fun retrievePeopleCount(): Long {
        return personRepository.count()
    }
}