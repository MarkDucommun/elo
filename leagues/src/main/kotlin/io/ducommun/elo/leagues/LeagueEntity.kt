package io.ducommun.elo.leagues

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class LeagueEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        val name: String = "",
        val description: String = "",
        val kFactor: Int = 0,
        val defaultELO: Int = 0,
        val createdAt: Long = 0,
        val updatedAt: Long = 0
)