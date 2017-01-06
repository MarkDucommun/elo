package io.ducommun.elo.eloratings

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EloRatingRepository : CrudRepository<EloRatingEntity, Long> {

//    @Query("select e from EloRatingEntity e where e.playerId = ?1 and e.leagueId = ?2 and e.version = (select max(e.version) from EloRatingEntity e)")
//    fun findCurrentEloRatingForPlayerInLeague(playerId: Long, leagueId: Long): EloRatingEntity

    fun findByPlayerIdAndLeagueId(playerId: Long, leagueId: Long): List<EloRatingEntity>

    fun findByLeagueId(leagueId: Long): List<EloRatingEntity>
}