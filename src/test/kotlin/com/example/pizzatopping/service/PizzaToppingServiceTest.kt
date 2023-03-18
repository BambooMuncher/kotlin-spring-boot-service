package com.example.pizzatopping.service

import com.example.pizzatopping.database.PersonRepository
import com.example.pizzatopping.database.ToppingsRepository
import com.example.pizzatopping.models.responses.ToppingResult
import com.example.pizzatopping.services.PizzaToppingService
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


/**
 * These are tests which ensure the PizzaToppingService is able to function as expected when integrating the database.
 * Mocking the database interactions would massively limit the value of the tests, since the primary function of the
 * service is to perform the necessary database operations to carry out each function.
 *
 * We're grouping testing multiple functions together in tests, which could be seen as bad practice and poor isolation.
 * However, I believe it to be worth the cost in this case. The assertions are relatively isolated such that it can be
 * determined which portion is failing. This also streamlines the data instrumentation for testing the functions which
 * require it. It allows us to smoke/integration test that everything is behaving as expected when we generate
 * particular states and conditions, which is quite valuable. We are able to test almost the entirety of the service all
 * at once.
 *
 */
@SpringBootTest
class PizzaToppingServiceTest {

    @Autowired
    private lateinit var pizzaToppingService: PizzaToppingService

    @Autowired
    private lateinit var personRepository: PersonRepository

    @Autowired
    private lateinit var toppingsRepository: ToppingsRepository

    private val testEmail = "test@test.com"
    private val testEmail2 = "test2@test.com"
    private val pepperoni = "pepperoni"
    private val sausage = "sausage"
    private val mushrooms = "mushrooms"

    @BeforeEach
    fun beforeEach() {
        personRepository.deleteAll()
        toppingsRepository.deleteAll()
    }

    @Test
    fun `submission from new person with new toppings persists proper data`() {
        // given

        // when
        pizzaToppingService.submitToppings(testEmail, listOf(mushrooms, pepperoni), null)

        // then
        val results = pizzaToppingService.retrieveSubmissionCountsByTopping()
        val peopleCount = pizzaToppingService.retrievePeopleCount()
        verifyPerson(email = testEmail, expectedSubmittedToppings = listOf(mushrooms, pepperoni))
        verifyTopping(name = mushrooms, expectedPeopleSubmittedBy = listOf(testEmail), result = results[mushrooms])

        verifyTopping(name = pepperoni, expectedPeopleSubmittedBy = listOf(testEmail), result = results[pepperoni])

        assert(results.size == 2)
        assert(peopleCount == 1L)
    }

    @Test
    fun `resubmission for an existing person with new toppings replaces their topping choices`() {
        // given

        // when
        pizzaToppingService.submitToppings(testEmail, listOf(mushrooms, pepperoni), null)
        pizzaToppingService.submitToppings(testEmail, listOf(), null)

        // then
        val results = pizzaToppingService.retrieveSubmissionCountsByTopping()
        val peopleCount = pizzaToppingService.retrievePeopleCount()
        verifyPerson(email = testEmail, expectedSubmittedToppings = listOf())
        verifyTopping(name = mushrooms, expectedPeopleSubmittedBy = listOf(), result = results[mushrooms])
        verifyTopping(name = pepperoni, expectedPeopleSubmittedBy = listOf(), result = results[pepperoni])

        assert(results.size == 2)
        assert(peopleCount == 1L)
    }

    @Test
    fun `submission of overlapping topping choices shares toppings within data model`() {
        // given

        // when
        pizzaToppingService.submitToppings(testEmail, listOf(mushrooms, pepperoni), null)
        pizzaToppingService.submitToppings(testEmail2, listOf(mushrooms, sausage), null)

        // then
        val results = pizzaToppingService.retrieveSubmissionCountsByTopping()
        val peopleCount = pizzaToppingService.retrievePeopleCount()
        verifyPerson(email = testEmail, expectedSubmittedToppings = listOf(mushrooms, pepperoni))
        verifyPerson(email = testEmail2, expectedSubmittedToppings = listOf(mushrooms, sausage))
        verifyTopping(name = mushrooms, expectedPeopleSubmittedBy = listOf(testEmail, testEmail2), result = results[mushrooms])
        verifyTopping(name = pepperoni, expectedPeopleSubmittedBy = listOf(testEmail), result = results[pepperoni])
        verifyTopping(name = sausage, expectedPeopleSubmittedBy = listOf(testEmail2), result = results[sausage])

        assert(results.size == 3)
        assert(peopleCount == 2L)
    }

    // testing favorites

    @Test
    fun `submitting favorite missing from toppings includes favorite as topping for the submission`() {
        // given

        // when
        pizzaToppingService.submitToppings(testEmail, listOf(), sausage)

        // then
        val results = pizzaToppingService.retrieveSubmissionCountsByTopping()
        val peopleCount = pizzaToppingService.retrievePeopleCount()
        verifyPerson(email = testEmail, expectedSubmittedToppings = listOf(sausage), expectedFavoriteTopping = sausage)
        verifyTopping(name = sausage, expectedPeopleSubmittedBy = listOf(testEmail), expectedPeopleFavoritedBy = listOf(testEmail), result = results[sausage])

        assert(results.size == 1)
        assert(peopleCount == 1L)
    }

    // note: we're also testing including favorites within the toppings list here
    @Test
    fun `multiple submissions with a shared favorite accumulate properly`() {
        // given

        // when
        pizzaToppingService.submitToppings(testEmail, listOf(mushrooms, sausage), sausage)
        pizzaToppingService.submitToppings(testEmail2, listOf(pepperoni, sausage), sausage)

        // then
        val results = pizzaToppingService.retrieveSubmissionCountsByTopping()
        val peopleCount = pizzaToppingService.retrievePeopleCount()
        verifyPerson(email = testEmail, expectedSubmittedToppings = listOf(mushrooms, sausage), expectedFavoriteTopping = sausage)
        verifyPerson(email = testEmail2, expectedSubmittedToppings = listOf(pepperoni, sausage), expectedFavoriteTopping = sausage)
        verifyTopping(name = sausage, expectedPeopleSubmittedBy = listOf(testEmail, testEmail2), expectedPeopleFavoritedBy = listOf(testEmail, testEmail2), result = results[sausage])
        verifyTopping(name = mushrooms, expectedPeopleSubmittedBy = listOf(testEmail), result = results[mushrooms])
        verifyTopping(name = pepperoni, expectedPeopleSubmittedBy = listOf(testEmail2), result = results[pepperoni])

        assert(results.size == 3)
        assert(peopleCount == 2L)
    }

    @Test
    fun `resubmission for an existing person with new favorite replaces their favorite choice`() {
        // given

        // when
        pizzaToppingService.submitToppings(testEmail, listOf(), sausage)
        pizzaToppingService.submitToppings(testEmail, listOf(), null)

        // then
        val results = pizzaToppingService.retrieveSubmissionCountsByTopping()
        val peopleCount = pizzaToppingService.retrievePeopleCount()
        verifyPerson(email = testEmail, expectedSubmittedToppings = listOf())
        verifyTopping(name = sausage, expectedPeopleSubmittedBy = listOf(), result = results[sausage])

        assert(results.size == 1)
        assert(peopleCount == 1L)
    }

    private fun verifyPerson(email: String, expectedSubmittedToppings: List<String>, expectedFavoriteTopping: String? = null) {
        val person = personRepository.findPeopleByEmail(email).firstOrNull()
        assert(person != null)
        assert(person!!.email == email)

        assert(person.submittedToppings.size == expectedSubmittedToppings.size)
        expectedSubmittedToppings.forEach{ expectedToppingName ->
            assert(person.submittedToppings.firstOrNull{ it.name == expectedToppingName } != null)
        }

        if(expectedFavoriteTopping == null) {
            assert(person.favoriteTopping == null)
        }
        else {
            assert(person.favoriteTopping != null)
            assert(person.favoriteTopping!!.name == expectedFavoriteTopping)
        }
    }

    private fun verifyTopping(name: String, expectedPeopleSubmittedBy: List<String>, expectedPeopleFavoritedBy: List<String>? = listOf(), result: ToppingResult?) {
        val topping = toppingsRepository.findToppingsByName(name).firstOrNull()
        assert(topping != null)
        assert(topping!!.name == name)

        assert(topping.peopleSubmittedBy.size == expectedPeopleSubmittedBy.size)
        expectedPeopleSubmittedBy.forEach{ expectedEmail ->
            assert(topping.peopleSubmittedBy.firstOrNull{ it.email == expectedEmail } != null)
        }

        assert(topping.peopleFavoritedBy.size == expectedPeopleFavoritedBy!!.size)
        expectedPeopleFavoritedBy.forEach{ expectedEmail ->
            assert(topping.peopleFavoritedBy.firstOrNull{ it.email == expectedEmail } != null)
        }

        // holding off result assertions until afterwards to first verify the data is populated expected
        assert(result != null)
        assert(result!!.totalSubmissions == expectedPeopleSubmittedBy.size.toLong())
        assert(result.totalTimesFavorited == expectedPeopleFavoritedBy.size.toLong())
    }
}