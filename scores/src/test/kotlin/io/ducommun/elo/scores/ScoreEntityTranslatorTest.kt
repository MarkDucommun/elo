package io.ducommun.elo.scores

import io.ducommun.elo.eloratings.EntityTranslatorTest

class ScoreEntityTranslatorTest : EntityTranslatorTest<ScoreEntity, Score>() {

    override val subject = ScoreEntityTranslator()

    override val entityToTranslate = ScoreEntity(
            id = 1,
            gameId = 2,
            playerId = 3,
            score = 10.0,
            createdAt = 0,
            updatedAt = 0
    )

    override val expectedDomainObject = Score(
            id = 1,
            gameId = 2,
            playerId = 3,
            score = 10.0
    )
}