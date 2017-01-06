package io.ducommun.elo.eloratings

import io.ducommun.elo.Finder
import org.springframework.stereotype.Service

@Service
open class EloRatingFinder(
        val eloRatingRepository: EloRatingRepository,
        eloRatingEntityTranslator: EloRatingEntityTranslator
) : Finder<EloRatingEntity, EloRating>(
        repository = eloRatingRepository,
        entityTranslator = eloRatingEntityTranslator
) {

    open fun findCurrentEloRatingForPlayerInLeague(playerId: Long, leagueId: Long) =
            eloRatingRepository
                    .findByPlayerIdAndLeagueId(playerId, leagueId)
                    .sortedByDescending { it.version }
                    .firstOrNull()
                    ?.toDomain()
                    ?: defaultEloRating(leagueId, playerId)

    open fun findCurrentEloRatingsForLeague(leagueId: Long) =
            eloRatingRepository
                    .findByLeagueId(leagueId)
                    .groupBy({ it.playerId }, { it })
                    .map { it ->
                        it.value
                                .sortedByDescending { it.version }
                                .first()
                                .toDomain()
                    }

    open fun findEloRatingsForPlayerInLeague(playerId: Long, leagueId: Long): List<EloRating> =
            eloRatingRepository
                    .findByPlayerIdAndLeagueId(playerId, leagueId)
                    .map { it.toDomain() }

    private fun defaultEloRating(leagueId: Long, playerId: Long): EloRating {
        return EloRating(
                id = 0,
                leagueId = leagueId,
                playerId = playerId,
                scoreId = 0,
                version = 0,
                value = 1500
        )
    }
}