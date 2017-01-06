package io.ducommun.elo.shared

import io.ducommun.elo.DomainTranslator
import io.ducommun.elo.EntityTranslator
import io.ducommun.elo.shared.Creator
import org.springframework.data.repository.CrudRepository
import java.time.Clock

abstract class AutomaticCreator<entityType, domainType>(
        private val repository: CrudRepository<entityType, Long>,
        private val entityTranslator: EntityTranslator<entityType, domainType>,
        private val domainTranslator: DomainTranslator<domainType, entityType>,
        private val clock: Clock
) :     Creator<domainType>,
        EntityTranslator<entityType, domainType> by entityTranslator,
        DomainTranslator<domainType, entityType> by domainTranslator {

    override fun create(newDomain: domainType) = repository
            .save(newDomain.toEntity(time = currentTime()))
            .toDomain()

    protected fun domainType.toEntity(time: Long) = domainToEntity(this, time)

    protected fun entityType.toDomain() = entityToDomain(this)

    protected fun currentTime() = clock
            .instant()
            .toEpochMilli()
}