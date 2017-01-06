package io.ducommun.elo.leagues

import io.ducommun.elo.eloratings.EntityTranslatorTest

class LeagueEntityTranslatorTest : EntityTranslatorTest<LeagueEntity, League>() {

    override val subject = LeagueEntityTranslator()

    override val entityToTranslate = LeagueEntity(
            id = 1,
            name = "test-name",
            description = "test-description",
            kFactor = 15,
            defaultELO = 1500,
            createdAt = 0,
            updatedAt = 0
    )

    override val expectedDomainObject = League(
            id = 1,
            name = "test-name",
            description = "test-description",
            kFactor = 15,
            defaultELO = 1500
    )
}