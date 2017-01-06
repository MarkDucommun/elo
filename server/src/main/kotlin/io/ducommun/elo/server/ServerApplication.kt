package io.ducommun.elo.server

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ducommun.calculator.CalculatorFactory
import io.ducommun.calculator.FiveThirtyEightNflCalculatorFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.bind.RelaxedPropertyResolver
import org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.time.Clock
import javax.sql.DataSource

@SpringBootApplication
@ComponentScan("io.ducommun.elo")
@Configuration
@EnableJpaRepositories(basePackages = arrayOf(
        "io.ducommun.elo.players",
        "io.ducommun.elo.leagues",
        "io.ducommun.elo.eloratings",
        "io.ducommun.elo.scores",
        "io.ducommun.elo.games"))
@EnableAutoConfiguration
@EnableTransactionManagement
open class ServerApplication

@Configuration
open class DatabaseConfiguration : EnvironmentAware {

    private lateinit var jpaPropertyResolver: RelaxedPropertyResolver

    @Autowired(required = false)
    private val persistenceUnitManager: PersistenceUnitManager? = null

    override fun setEnvironment(environment: Environment) {
        this.jpaPropertyResolver = RelaxedPropertyResolver(environment, "spring.jpa.")
    }

    @Bean
    @DependsOn("jdbcTemplate")
    open fun entityManagerFactory(
            dataSource: DataSource,
            jpaVendorAdapter: JpaVendorAdapter
    ): LocalContainerEntityManagerFactoryBean {
        val entityManagerFactoryBean = LocalContainerEntityManagerFactoryBean()
        if (persistenceUnitManager != null) {
            entityManagerFactoryBean.setPersistenceUnitManager(persistenceUnitManager)
        }
        entityManagerFactoryBean.jpaVendorAdapter = jpaVendorAdapter
        entityManagerFactoryBean.dataSource = dataSource
        entityManagerFactoryBean.setPackagesToScan("io.ducommun.elo")
        entityManagerFactoryBean.jpaPropertyMap.putAll(jpaPropertyResolver.getSubProperties("properties."))
        val properties = entityManagerFactoryBean.jpaPropertyMap
        properties.put("hibernate.ejb.naming_strategy", jpaPropertyResolver.getProperty("hibernate.naming-strategy", SpringNamingStrategy::class.java.name))
        properties.put("hibernate.hbm2ddl.auto", jpaPropertyResolver.getProperty("hibernate.ddl-auto", "create-drop"))
        return entityManagerFactoryBean
    }

    @Bean
    open fun clock() = Clock.systemUTC()

    @Bean
    open fun objectMapperBuilder() = Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())

    @Bean
    open fun calculatorFactory() = FiveThirtyEightNflCalculatorFactory()
}

fun main(args: Array<String>) {
    SpringApplication.run(ServerApplication::class.java, *args)
}
