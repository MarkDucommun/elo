package io.ducommun.elo.games

import io.ducommun.elo.EntityTranslator
import org.springframework.stereotype.Service

@Service
class GameEntityTranslator : EntityTranslator<GameEntity, Game> {

    override fun entityToDomain(entity: GameEntity) = Game(
            id = entity.id,
            leagueId = entity.leagueId
    )
}