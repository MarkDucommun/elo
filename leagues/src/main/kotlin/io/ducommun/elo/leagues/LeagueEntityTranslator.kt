package io.ducommun.elo.leagues

import io.ducommun.elo.EntityTranslator
import org.springframework.stereotype.Component

@Component
class LeagueEntityTranslator : EntityTranslator<LeagueEntity, League> {

    override fun entityToDomain(entity: LeagueEntity) = League(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            kFactor = entity.kFactor,
            defaultELO = entity.defaultELO
    )
}