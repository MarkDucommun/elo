package io.ducommun.elo.scores

import io.ducommun.elo.eloratings.DomainTranslatorTest

class ScoreDomainTranslatorTest : DomainTranslatorTest<Score, ScoreEntity>() {

    override val subject = ScoreDomainTranslator()

    override val domainObjectToTranslate = Score(
            gameId = 1,
            playerId = 2,
            score = 10.0
    )

    override val time: Long = 0

    override val expectedEntity = ScoreEntity(
            id = 0,
            gameId = 1,
            playerId = 2,
            score = 10.0,
            createdAt = time,
            updatedAt = time
    )
}