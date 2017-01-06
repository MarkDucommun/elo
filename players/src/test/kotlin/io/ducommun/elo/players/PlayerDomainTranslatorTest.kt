package io.ducommun.elo.players

import io.ducommun.elo.eloratings.DomainTranslatorTest

class PlayerDomainTranslatorTest : DomainTranslatorTest<Player, PlayerEntity>(){

    override val subject = PlayerDomainTranslator()

    override val domainObjectToTranslate =  Player(
            name = "test-name"
    )

    override val time: Long = 0

    override val expectedEntity = PlayerEntity(
            id = 0,
            name = "test-name",
            createdAt = time,
            updatedAt = time
    )
}