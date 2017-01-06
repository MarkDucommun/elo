package io.ducommun.elo.leagues

import io.ducommun.elo.shared.AutomaticCreator
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class LeagueCreator(
        repository: LeagueRepository,
        entityTranslator: LeagueEntityTranslator,
        domainTranslator: LeagueDomainTranslator,
        clock: Clock
) : AutomaticCreator<LeagueEntity, League>(
        repository = repository,
        entityTranslator = entityTranslator,
        domainTranslator = domainTranslator,
        clock = clock
)