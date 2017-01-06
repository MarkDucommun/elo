package io.ducommun.elo.games

import io.ducommun.elo.eloratings.DomainTranslatorTest

class GameDomainTranslatorTest : DomainTranslatorTest<Game, GameEntity>() {

    override val subject = GameDomainTranslator()

    override val domainObjectToTranslate = Game(leagueId = 1)

    override val time: Long = 0

    override val expectedEntity = GameEntity(
            id = 0,
            leagueId = 1,
            createdAt = time,
            updatedAt = time
    )
}