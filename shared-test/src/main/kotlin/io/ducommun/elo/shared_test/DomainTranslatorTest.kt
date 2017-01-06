package io.ducommun.elo.eloratings

import io.ducommun.elo.DomainTranslator
import org.assertj.core.api.KotlinAssertions
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

abstract class DomainTranslatorTest<domainType, entityType> {

    abstract val subject: DomainTranslator<domainType, entityType>

    abstract val domainObjectToTranslate: domainType

    abstract val time: Long

    abstract val expectedEntity: entityType

    @Test
    fun testTranslation() {
        val translatedDomainObject = subject.domainToEntity(domainObject = domainObjectToTranslate, time = time)

        assertThat(translatedDomainObject).isEqualToComparingFieldByField(expectedEntity)
    }
}