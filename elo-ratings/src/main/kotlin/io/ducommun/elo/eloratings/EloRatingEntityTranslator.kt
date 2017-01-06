package io.ducommun.elo.eloratings

import io.ducommun.elo.EntityTranslator
import org.springframework.stereotype.Component

@Component
class EloRatingEntityTranslator : EntityTranslator<EloRatingEntity, EloRating> {

    override fun entityToDomain(eloRatingEntity: EloRatingEntity): EloRating {
        return EloRating(
                id = eloRatingEntity.id,
                leagueId = eloRatingEntity.leagueId,
                scoreId = eloRatingEntity.scoreId,
                playerId = eloRatingEntity.playerId,
                version = eloRatingEntity.version,
                value = eloRatingEntity.value
        )
    }
}