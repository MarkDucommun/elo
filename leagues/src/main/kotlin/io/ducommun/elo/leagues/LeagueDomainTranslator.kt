package io.ducommun.elo.leagues

import io.ducommun.elo.DomainTranslator
import org.springframework.stereotype.Component

@Component
class LeagueDomainTranslator : DomainTranslator<League, LeagueEntity> {

    override fun domainToEntity(domainObject: League, time: Long) = LeagueEntity(
            name = domainObject.name,
            description = domainObject.description,
            kFactor = domainObject.kFactor,
            defaultELO = domainObject.defaultELO,
            createdAt = time,
            updatedAt = time
    )
}