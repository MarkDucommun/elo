package io.ducommun.elo

interface DomainTranslator<in domainType, out entityType> {

    fun domainToEntity(domainObject: domainType, time: Long) : entityType
}