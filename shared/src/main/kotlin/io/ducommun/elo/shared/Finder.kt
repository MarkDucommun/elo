package io.ducommun.elo

import org.springframework.data.repository.CrudRepository

abstract class Finder<entityType, domainType>(
        var repository: CrudRepository<entityType, Long>,
        entityTranslator: EntityTranslator<entityType, domainType>

) : EntityTranslator<entityType, domainType> by entityTranslator {

    protected fun entityType.toDomain() = entityToDomain(this)
}