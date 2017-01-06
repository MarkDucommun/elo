package io.ducommun.elo.eloratings

import io.ducommun.elo.DomainTranslator
import org.springframework.stereotype.Component

@Component
class EloRatingDomainTranslator : DomainTranslator<EloRating, EloRatingEntity> {

    override fun domainToEntity(elotRating: EloRating, time: Long): EloRatingEntity {
            return EloRatingEntity(
                    leagueId = elotRating.leagueId,
                    scoreId = elotRating.scoreId,
                    playerId = elotRating.playerId,
                    version = elotRating.version,
                    value = elotRating.value,
                    createdAt = time,
                    updatedAt = time
            )
        }
}