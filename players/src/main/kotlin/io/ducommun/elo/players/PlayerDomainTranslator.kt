package io.ducommun.elo.players

import io.ducommun.elo.DomainTranslator
import org.springframework.stereotype.Component

@Component
class PlayerDomainTranslator : DomainTranslator<Player, PlayerEntity> {

    override fun domainToEntity(domainObject: Player, time: Long) = PlayerEntity(
            name = domainObject.name,
            createdAt = time,
            updatedAt = time
    )
}