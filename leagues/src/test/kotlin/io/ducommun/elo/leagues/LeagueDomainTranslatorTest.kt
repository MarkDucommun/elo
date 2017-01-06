package io.ducommun.elo.leagues

import io.ducommun.elo.eloratings.DomainTranslatorTest

class LeagueDomainTranslatorTest : DomainTranslatorTest<League, LeagueEntity>() {

    override val subject = LeagueDomainTranslator()

    override val domainObjectToTranslate = League(
            name = "test-name",
            description = "test-description",
            kFactor = 15,
            defaultELO = 1500
    )

    override val time: Long = 0

    override val expectedEntity = LeagueEntity(
            id = 0,
            name = "test-name",
            description = "test-description",
            kFactor = 15,
            defaultELO = 1500,
            createdAt = time,
            updatedAt = time
    )
}