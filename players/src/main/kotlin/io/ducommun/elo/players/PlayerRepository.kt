package io.ducommun.elo.players

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository : CrudRepository<PlayerEntity, Long> {
    fun findByName(name: String) : PlayerEntity
    fun findByIdIn(ids: List<Long>): List<PlayerEntity>
}