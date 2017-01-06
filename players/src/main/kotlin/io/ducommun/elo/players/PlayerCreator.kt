package io.ducommun.elo.players

import io.ducommun.elo.shared.AutomaticCreator
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class PlayerCreator(
        repository: PlayerRepository,
        entityTranslator: PlayerEntityTranslator,
        domainTranslator: PlayerDomainTranslator,
        clock: Clock
) : AutomaticCreator<PlayerEntity, Player>(
        repository = repository,
        entityTranslator = entityTranslator,
        domainTranslator = domainTranslator,
        clock = clock
)