package io.ducommun.elo.scores

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ScoreEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        val gameId: Long,
        val playerId: Long = 0,
        val score: Double = 0.0,
        val createdAt: Long = 0,
        val updatedAt: Long = 0
)