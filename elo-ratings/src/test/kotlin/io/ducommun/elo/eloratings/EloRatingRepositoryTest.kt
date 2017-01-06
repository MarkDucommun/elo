package io.ducommun.elo.eloratings

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(DummyApplication::class))
@DirtiesContext
class EloRatingRepositoryTest {

    @Autowired
    lateinit var subject: EloRatingRepository

    @Test
    @Ignore
    fun findCurrentEloRatingForPlayerInLeague_findsTheRightEloRating() {
        val earliestEloRatingEntity = EloRatingEntity(
                leagueId = 2,
                scoreId = 3,
                playerId = 4,
                version = 1,
                value = 1500,
                createdAt = 0,
                updatedAt = 0
        )

        val latestEloRatingEntity = earliestEloRatingEntity.copy(version = 2, value = 1550)
        val anotherPlayersEloRating = earliestEloRatingEntity.copy(playerId = 5, value = 1400)
        val anotherLeagueEloRating = earliestEloRatingEntity.copy(leagueId = 6, value = 1200)

        subject.save(earliestEloRatingEntity)
        subject.save(latestEloRatingEntity)
        subject.save(anotherPlayersEloRating)
        subject.save(anotherLeagueEloRating)

//        val currentEloRating = subject.findCurrentEloRatingForPlayerInLeague(4, 2)

//        assertThat(currentEloRating).isEqualTo(latestEloRatingEntity)
    }

    @Test
    fun findByPlayerIdAndLeagueId_returnsAnEloIfExists() {
        val expectedElo = EloRatingEntity(
                leagueId = 1,
                scoreId = 3,
                playerId = 1,
                version = 1,
                value = 1500,
                createdAt = 0,
                updatedAt = 0
        )
        val otherElo = expectedElo.copy(leagueId = 2)
        val anotherElo = expectedElo.copy(playerId = 2)

        subject.save(expectedElo)
        subject.save(otherElo)
        subject.save(anotherElo)

        val returnedElo = subject.findByPlayerIdAndLeagueId(1,1)

        assertThat(returnedElo).contains(expectedElo)
    }

    @Test
    fun findByPlayerIdAndLeagueId_returnsNullOtherwise() {
        val otherElo = EloRatingEntity(
                leagueId = 3,
                scoreId = 3,
                playerId = 3,
                version = 1,
                value = 1500,
                createdAt = 0,
                updatedAt = 0
        )
        val anotherElo = otherElo.copy(playerId = 2)

        subject.save(otherElo)
        subject.save(anotherElo)

        val returnedElo = subject.findByPlayerIdAndLeagueId(1, 1)

        assertThat(returnedElo).isEmpty()
    }

    @Test
    fun findEloRatingByLeagueg() {
        val eloOne = EloRatingEntity(
                leagueId = 2,
                scoreId = 3,
                playerId = 4,
                version = 1,
                value = 1500,
                createdAt = 0,
                updatedAt = 0
        )

        val eloTwo = eloOne.copy(version = 2, value = 1550)
        val eloThree = eloOne.copy(playerId = 5, value = 1400)
        val anotherLeagueEloRating = eloOne.copy(leagueId = 6, value = 1200)

        subject.save(eloOne)
        subject.save(eloTwo)
        subject.save(eloThree)
        subject.save(anotherLeagueEloRating)

        val currentEloRating = subject.findByLeagueId(2)

        assertThat(currentEloRating).contains(eloOne, eloTwo, eloThree)
    }
}


@SpringBootApplication
open class DummyApplication {
    @Bean
    open fun clock() = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC)

    @Bean
    open fun objectMapperBuilder() = Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())
}