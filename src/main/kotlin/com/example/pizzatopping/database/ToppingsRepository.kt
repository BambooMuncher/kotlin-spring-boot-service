package com.example.pizzatopping.database

import com.example.pizzatopping.models.database.PersonEntity
import com.example.pizzatopping.models.database.ToppingEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.*
import org.springframework.stereotype.Repository

@Repository
class ToppingsRepository {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun retrieveSortedToppings(): List<ToppingEntity> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val query = criteriaBuilder.createQuery(ToppingEntity::class.java)
        val root = query.from(ToppingEntity::class.java)
        // left join allows us to include toppings which have been submitted and then un-submitted as well as favorited and then un-favorited
        val peopleSubmittedBy: SetJoin<ToppingEntity, List<PersonEntity>> = root.joinSet("peopleSubmittedBy", JoinType.LEFT)
        val peopleFavoritedBy: SetJoin<ToppingEntity, List<PersonEntity>> = root.joinSet("peopleFavoritedBy", JoinType.LEFT)
        val groupById: Path<Int> = root.get("id")
        val orderList = mutableListOf<Order>()
        orderList.add(criteriaBuilder.desc(criteriaBuilder.count(peopleSubmittedBy)))
        orderList.add(criteriaBuilder.desc(criteriaBuilder.count(peopleFavoritedBy)))
        query.groupBy(groupById)
        query.orderBy(orderList)

        return entityManager.createQuery(query).resultList
    }
}