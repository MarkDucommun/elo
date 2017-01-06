package io.ducommun.elo.leagues

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.anyString

class LeagueFinderTest {
    lateinit var mockRepository : LeagueRepository
    lateinit var subject : LeagueFinder

    @Before
    fun setup() {
        mockRepository = createMockRepository()
        subject = LeagueFinder(mockRepository, LeagueEntityTranslator())
    }

    @Test
    fun findByName_passesTheNameToTheRepository() {
        findLeagueByName()

        verify(mockRepository).findByName("name")
    }

    @Test
    fun findByName_returnsTheLeague() {

        val league = findLeagueByName()

        assertThat(league).isEqualTo(League(
                id = 1,
                name = "name",
                description = "description",
                kFactor = 20,
                defaultELO = 1500))
    }

    private fun findLeagueByName() = subject.findByName("name")

    private fun createMockRepository() = mock<LeagueRepository>() {
        on { findByName(anyString()) } doReturn LeagueEntity(
                id = 1,
                name = "name",
                description = "description",
                kFactor = 20,
                defaultELO = 1500,
                createdAt = 0,
                updatedAt = 0)
    }
}