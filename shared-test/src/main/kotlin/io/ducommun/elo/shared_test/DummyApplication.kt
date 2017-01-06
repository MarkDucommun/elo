package io.ducommun.elo.shared_test

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

@SpringBootApplication
open class DummyApplication {
    @Bean
    open fun clock() = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC)

    @Bean
    open fun objectMapperBuilder() = Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())
}