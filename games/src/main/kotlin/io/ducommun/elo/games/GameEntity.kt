package io.ducommun.elo.games

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class GameEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val leagueId: Long = 0,
    val createdAt: Long = -1,
    val updatedAt: Long = -1
)