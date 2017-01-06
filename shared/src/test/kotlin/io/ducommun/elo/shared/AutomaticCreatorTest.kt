package io.ducommun.elo.shared

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.ducommun.elo.DomainTranslator
import io.ducommun.elo.EntityTranslator
import io.ducommun.elo.shared.AutomaticCreator
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test
import org.springframework.data.repository.CrudRepository
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class AutomaticCreatorTest {

    val translatedTestEntity = TestEntity()
    val savedTestEntity = TestEntity()
    val translatedTestDomain = TestDomain()

    val mockRepository = createMockRepository()
    val testEntityTranslator = createMockEntityTranslator()
    val testDomainTranslator = createMockDomainTranslator()

    val clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC)

    val subject = TestAutomaticCreator(
            repository = mockRepository,
            entityTranslator = testEntityTranslator,
            domainTranslator = testDomainTranslator,
            clock = clock
    )

    @Test
    fun create_translatesTheDomainObjectIntoAnEntity() {
        val newDomain = TestDomain()

        subject.create(newDomain)

        verify(testDomainTranslator).domainToEntity(newDomain, 0L)
    }

    @Test
    fun create_savesTheEntity() {

        subject.create(TestDomain())

        verify(mockRepository).save(translatedTestEntity)
    }

    @Test
    fun create_translatesTheEntityBackToDomainObjectAndReturnsIt() {
        subject.create(TestDomain())

        verify(testEntityTranslator).entityToDomain(savedTestEntity)
    }

    @Test
    fun create_returnsTheDomainObject() {
        val returnedTestDomain = subject.create(TestDomain())

        assertThat(returnedTestDomain).isSameAs(translatedTestDomain)
    }

    private fun createMockRepository() = mock<CrudRepository<TestEntity, Long>>() {
        on { save(any<TestEntity>()) } doReturn savedTestEntity
    }

    private fun createMockEntityTranslator() = mock<EntityTranslator<TestEntity, TestDomain>>() {
        on { entityToDomain(com.nhaarman.mockito_kotlin.any<TestEntity>()) } doReturn translatedTestDomain
    }

    private fun createMockDomainTranslator() = mock<DomainTranslator<TestDomain, TestEntity>>() {
        on {
            domainToEntity(domainObject = any<TestDomain>(), time = any<Long>())
        } doReturn translatedTestEntity
    }

    class TestAutomaticCreator(
            repository: CrudRepository<TestEntity, Long>,
            entityTranslator: EntityTranslator<TestEntity, TestDomain>,
            domainTranslator: DomainTranslator<TestDomain, TestEntity>,
            clock: Clock
    ) : AutomaticCreator<TestEntity, TestDomain>(
            repository = repository,
            entityTranslator = entityTranslator,
            domainTranslator = domainTranslator,
            clock = clock
    )

    data class TestEntity(
            val id: Long = 0,
            val list: List<String> = emptyList()
    )

    data class TestDomain(
            val id: Long = 0,
            val list: List<String> = emptyList()
    )
}