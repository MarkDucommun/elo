package io.ducommun.elo.players

import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

class PlayerFinderTest {

    val mockRepository = createMockRepository()
    val subject = PlayerFinder(mockRepository, PlayerEntityTranslator())

    @Test
    fun findByName_passesTheNameToTheRepository() {
        findPlayerByName()

        verify(mockRepository).findByName("name")
    }

    @Test
    fun findByName_returnsThePlayer() {
        val player = findPlayerByName()

        assertThat(player).isEqualTo(Player(id = 1, name = "Bob"))
    }

    @Test
    fun `find by id passes the IDs to the repository`() {
        findByIdsIn(1, 2, 3)

        verify(mockRepository).findByIdIn(listOf(1, 2, 3))
    }

    @Test
    fun `find by id translates and returns the player entities`() {
        whenever(mockRepository.findByIdIn(listOf(any()))).doReturn(listOf(
                PlayerEntity(id = 1, name = "Bob"), PlayerEntity(id = 2, name = "Joe")
        ))

        val returnedPlayers: List<Player> = subject.findByIds(listOf(1, 2))

        assertThat(returnedPlayers).contains(Player(id = 1, name = "Bob"), Player(id = 2, name = "Joe"))
    }

    private fun findPlayerByName() = subject.findByName("name")

    private fun findByIdsIn(vararg ids: Long) = subject.findByIds(ids.toList())

    private fun createMockRepository() = mock<PlayerRepository>() {
        on { findByName(any()) } doReturn PlayerEntity(id = 1, name = "Bob", createdAt = 0, updatedAt = 0)
    }
}