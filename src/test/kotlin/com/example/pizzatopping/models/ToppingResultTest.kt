package com.example.pizzatopping.models

import com.example.pizzatopping.models.responses.ToppingResult
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ToppingResultTest {
    private val testList: List<ToppingResult> = listOf(
        ToppingResult(totalTimesSubmitted = 3, totalTimesFavorited = 3),
        ToppingResult(totalTimesSubmitted = 1, totalTimesFavorited = 1),
        ToppingResult(totalTimesSubmitted = 3, totalTimesFavorited = 2),
        ToppingResult(totalTimesSubmitted = 4, totalTimesFavorited = 0),
        ToppingResult(totalTimesSubmitted = 0, totalTimesFavorited = 0),
        ToppingResult(totalTimesSubmitted = 3, totalTimesFavorited = 0)
    )

    private val expectedSortResult: List<ToppingResult> = listOf(
        ToppingResult(totalTimesSubmitted = 4, totalTimesFavorited = 0),
        ToppingResult(totalTimesSubmitted = 3, totalTimesFavorited = 3),
        ToppingResult(totalTimesSubmitted = 3, totalTimesFavorited = 2),
        ToppingResult(totalTimesSubmitted = 3, totalTimesFavorited = 0),
        ToppingResult(totalTimesSubmitted = 1, totalTimesFavorited = 1),
        ToppingResult(totalTimesSubmitted = 0, totalTimesFavorited = 0)
    )

    @Test
    fun `topping results sorted by total times submitted then total times favorited`() {
        // given

        // when
        val result = testList.sortedDescending()

        // then
        assert(result.size == expectedSortResult.size)
        for (i in result.indices) {
            assert(result[i].totalTimesSubmitted == expectedSortResult[i].totalTimesSubmitted)
            assert(result[i].totalTimesFavorited == expectedSortResult[i].totalTimesFavorited)
        }
    }
}
