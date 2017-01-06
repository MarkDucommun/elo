package io.ducommun.elo.league_orchestrator

import io.ducommun.elo.new_game_orchestrator.domain.NamedPlayerScore
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

class CsvFileLoaderTest {

    @Test
    fun `load opens the supplied file`() {
        val fileLocation = "src/test/resources/test.csv"

        val listOfUnsavedGames = CsvFileLoader().load(fileLocation)

        assertThat(listOfUnsavedGames).contains(createListOfGames(listOf("Mark", "Phil"), listOf(50.1, 60.2)))
        assertThat(listOfUnsavedGames).contains(createListOfGames(listOf("Sander", "McCool"), listOf(40.0, 70.0)))
        assertThat(listOfUnsavedGames).contains(createListOfGames(listOf("Mark", "McCool"), listOf(60.0, 80.0)))
        assertThat(listOfUnsavedGames).contains(createListOfGames(listOf("Sander", "Phil", "Ted"), listOf(60.0, 50.0, 30.0)))
    }
}