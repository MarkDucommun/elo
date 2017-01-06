package io.ducommun.elo.eloratings

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class EloRatingEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        val leagueId: Long = 0,
        val playerId: Long = 0,
        val scoreId: Long = 0,
        val version: Int = 0,
        val value: Int = 0,
        val createdAt: Long = -1,
        val updatedAt: Long = -1
)