package io.ducommun.elo.scores

import io.ducommun.elo.DomainTranslator
import org.springframework.stereotype.Component

@Component
class ScoreDomainTranslator : DomainTranslator<Score, ScoreEntity> {

    override fun domainToEntity(domainObject: Score, time: Long) = ScoreEntity(
            gameId = domainObject.gameId,
            playerId = domainObject.playerId,
            score = domainObject.score,
            createdAt = time,
            updatedAt = time
    )
}