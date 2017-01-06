package io.ducommun.elo.players

import io.ducommun.elo.eloratings.EntityTranslatorTest

class PlayerEntityTranslatorTest : EntityTranslatorTest<PlayerEntity, Player>() {

    override val subject =  PlayerEntityTranslator()

    override val entityToTranslate =  PlayerEntity(
            id = 1,
            name = "test-name"
    )

    override val expectedDomainObject = Player(
            id = 1,
            name = "test-name"
    )
}