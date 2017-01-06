package io.ducommun.elo.games

import io.ducommun.elo.DomainTranslator
import org.springframework.stereotype.Service

@Service
class GameDomainTranslator : DomainTranslator<Game, GameEntity> {

    override fun domainToEntity(domainObject: Game, time: Long) = GameEntity(
            leagueId = domainObject.leagueId,
            createdAt = time,
            updatedAt = time
    )
}