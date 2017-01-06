package io.ducommun.elo

interface EntityTranslator<in entityType, out domainType> {

    fun entityToDomain(entity: entityType): domainType
}