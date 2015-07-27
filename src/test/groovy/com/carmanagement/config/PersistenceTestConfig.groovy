package com.carmanagement.config

import org.apache.commons.dbcp.BasicDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver
import org.springframework.orm.hibernate4.HibernateExceptionTranslator
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.carmanagement.repositories")
@Profile("test")
public class PersistenceTestConfig {

    @Bean
    public PlatformTransactionManager transactionManager() {
        EntityManagerFactory factory = entityManagerFactory().getObject()
        return new JpaTransactionManager(factory)
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean()
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter()
        vendorAdapter.setGenerateDdl(Boolean.TRUE)
        vendorAdapter.setShowSql(Boolean.TRUE)
        factory.setDataSource(dataSource())
        factory.setJpaVendorAdapter(vendorAdapter)
        factory.setPackagesToScan("com.carmanagement.entities")
        Properties jpaProperties = new Properties()
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
        jpaProperties.put("hibernate.show_sql", "true")
        jpaProperties.put("hibernate.hbm2ddl.auto", "update")
        factory.setJpaProperties(jpaProperties)
        factory.afterPropertiesSet()
        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver())
        return factory
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator()
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource()
        dataSource.setDriverClassName("org.h2.Driver")
        dataSource.setUrl("jdbc:h2:mem:test")
        dataSource.setUsername("sa")
        dataSource.setPassword("")
        return dataSource
    }
}
