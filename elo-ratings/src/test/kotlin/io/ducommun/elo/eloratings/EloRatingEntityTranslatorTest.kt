package io.ducommun.elo.eloratings

class EloRatingEntityTranslatorTest : EntityTranslatorTest<EloRatingEntity, EloRating>() {

    override val subject = EloRatingEntityTranslator()

    override val entityToTranslate = EloRatingEntity(
            id = 1,
            leagueId = 1,
            playerId = 1,
            scoreId = 1,
            version = 1,
            value = 1,
            createdAt = 1,
            updatedAt = 1
    )

    override val expectedDomainObject = EloRating(
            id = 1,
            leagueId = 1,
            playerId = 1,
            scoreId = 1,
            version = 1,
            value = 1
    )
}