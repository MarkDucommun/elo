package io.ducommun.elo.games

import io.ducommun.elo.EntityTranslator
import io.ducommun.elo.eloratings.EntityTranslatorTest

class GameEntityTranslatorTest : EntityTranslatorTest<GameEntity, Game>() {

    override val subject = GameEntityTranslator()

    override val entityToTranslate = GameEntity(
            id = 1,
            leagueId = 2,
            createdAt = 0,
            updatedAt = 0
    )

    override val expectedDomainObject = Game(
            id = 1,
            leagueId = 2
    )
}