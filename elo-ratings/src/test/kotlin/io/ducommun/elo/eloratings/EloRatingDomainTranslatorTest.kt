package io.ducommun.elo.eloratings

class EloRatingDomainTranslatorTest : DomainTranslatorTest<EloRating, EloRatingEntity>() {

    override val subject = EloRatingDomainTranslator()

    override val domainObjectToTranslate = EloRating(
            leagueId = 1,
            playerId = 1,
            scoreId = 1,
            version = 1,
            value = 1
    )

    override val time: Long = 1

    override val expectedEntity = EloRatingEntity(
            id = 0,
            leagueId = 1,
            playerId = 1,
            scoreId = 1,
            version = 1,
            value = 1,
            createdAt = time,
            updatedAt = time
    )
}