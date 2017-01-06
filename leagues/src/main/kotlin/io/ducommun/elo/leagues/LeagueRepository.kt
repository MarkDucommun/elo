package io.ducommun.elo.leagues

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LeagueRepository : CrudRepository<LeagueEntity, Long> {

    fun findByName(name: String) : LeagueEntity
}