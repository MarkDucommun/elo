package io.ducommun.elo.server

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.elo.league_orchestrator.CsvLoaderLeagueOrchestrator
import io.ducommun.elo.league_orchestrator.LeagueOrchestrator
import io.ducommun.elo.league_orchestrator.LeagueSeed
import org.junit.Test

class LeagueLoaderTest {

    @Test
    fun run_doesNotCallTheLeagueOrchestratorWhenNoFilePathIsSet() {

        val mockLeagueOrchestrator: LeagueOrchestrator = mock()

        val subject = LeagueLoader(mockLeagueOrchestrator, "", 0, 0)

        subject.run()

        verify(mockLeagueOrchestrator, never()).create(any())
    }

    @Test
    fun run_createsLeagueWhenFilePathIsSetAndUsesDefaultValues() {

        val mockLeagueOrchestrator: LeagueOrchestrator = mock()

        val subject = LeagueLoader(mockLeagueOrchestrator, "filePath", 20, 1500)

        subject.run()

        verify(mockLeagueOrchestrator).create(LeagueSeed("filePath", 20, 1500))
    }

    @Test
    fun run_createsLeagueWhenFilePathIsSetAndPassesThroughKFactorAndDefaultElo() {

        val mockLeagueOrchestrator: LeagueOrchestrator = mock()

        val subject = LeagueLoader(mockLeagueOrchestrator, "filePath", 30, 1600)

        subject.run()

        verify(mockLeagueOrchestrator).create(LeagueSeed("filePath", 30, 1600))
    }
}