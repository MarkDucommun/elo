package io.ducommun.elo.players

import io.ducommun.elo.EntityTranslator
import org.springframework.stereotype.Service

@Service
open class PlayerFinder(
        val repository: PlayerRepository,
        playerEntityTranslator: PlayerEntityTranslator

) : EntityTranslator<PlayerEntity, Player> by playerEntityTranslator {

    open fun findByName(name: String) = repository.findByName(name).toPlayer()

    open fun findByIds(ids: List<Long>) = repository.findByIdIn(ids).map { it.toPlayer() }

    private fun PlayerEntity.toPlayer() = entityToDomain(this)
}