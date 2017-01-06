package io.ducommun.elo.scores

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ScoreRepository : CrudRepository<ScoreEntity, Long>