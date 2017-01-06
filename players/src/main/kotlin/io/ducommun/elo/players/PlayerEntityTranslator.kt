package io.ducommun.elo.players

import io.ducommun.elo.EntityTranslator
import org.springframework.stereotype.Component

@Component
class PlayerEntityTranslator : EntityTranslator<PlayerEntity, Player> {

    override fun entityToDomain(entity: PlayerEntity) = Player(
            id = entity.id,
            name = entity.name
    )
}