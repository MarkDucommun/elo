package io.ducommun.elo.leagues

import io.ducommun.elo.Finder
import org.springframework.stereotype.Service

@Service
open class LeagueFinder(
        val leagueRepository: LeagueRepository,
        leagueEntityTranslator: LeagueEntityTranslator

) : Finder<LeagueEntity, League>(
        repository = leagueRepository,
        entityTranslator = leagueEntityTranslator
) {

    open fun findByName(name: String) = leagueRepository.findByName(name).toLeague()

    private fun LeagueEntity.toLeague() = entityToDomain(this)
}