package io.ducommun.elo.players

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.KotlinAssertions.assertThat
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
class PlayerRepositoryTest {

    @Autowired
    lateinit var subject: PlayerRepository

    @Test
    fun findByIdIn() {
        val bob = PlayerEntity(
                name = "Bob",
                createdAt = 0,
                updatedAt = 0
        )
        val tim = bob.copy(name = "Tim")
        val joe = bob.copy(name = "Joe")

        subject.save(bob)
        subject.save(tim)
        subject.save(joe)

        val returnedPlayers = subject.findByIdIn(listOf(bob.id, tim.id, joe.id))

        assertThat(returnedPlayers).contains(bob, tim, joe)
    }
}

@SpringBootApplication
open class DummyApplication {
    @Bean
    open fun clock() = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC)

    @Bean
    open fun objectMapperBuilder() = Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())
}