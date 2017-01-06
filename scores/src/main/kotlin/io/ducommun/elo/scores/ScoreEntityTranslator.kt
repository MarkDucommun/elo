package io.ducommun.elo.scores

import io.ducommun.elo.EntityTranslator
import org.springframework.stereotype.Component

@Component
class ScoreEntityTranslator : EntityTranslator<ScoreEntity, Score> {

    override fun entityToDomain(entity: ScoreEntity) = Score(
            id = entity.id,
            gameId = entity.gameId,
            playerId = entity.playerId,
            score = entity.score
    )
}