package io.ducommun.elo.shared

interface Creator<domainType> {

    fun create(domainObject: domainType) : domainType
}