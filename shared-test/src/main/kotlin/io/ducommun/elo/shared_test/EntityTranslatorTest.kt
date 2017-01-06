package io.ducommun.elo.eloratings

import io.ducommun.elo.EntityTranslator
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

abstract class EntityTranslatorTest<entityType, domainType> {

    abstract val subject: EntityTranslator<entityType, domainType>

    abstract val entityToTranslate: entityType

    abstract val expectedDomainObject: domainType

    @Test
    fun testTranslation() {
        val translatedDomainObject = subject.entityToDomain(entity = entityToTranslate)

        assertThat(translatedDomainObject).isEqualToComparingFieldByField(expectedDomainObject)
    }

}