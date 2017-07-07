package dev22.com.contactutility

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val numberOfPlayers = 7
        val scoreOfPlayers = mutableListOf(100, 100, 50, 40, 40, 20, 10)
        val beatLevelNumberOfAlice = 4
        val valueOfLevel = mutableListOf(5, 25, 50, 120)

        for (level in valueOfLevel){
            scoreOfPlayers.add(level)
            scoreOfPlayers.sortDescending()
            val sortedScore = scoreOfPlayers.distinct()
            println(sortedScore)
            println(sortedScore.indexOf(level) + 1)
        }
    }
}
