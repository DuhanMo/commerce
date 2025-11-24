package org.duhan.commerce.storage.db.core.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
internal class CoreDataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "storage.datasource.core")
    fun coreHikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    fun coreDataSource(@Qualifier("coreHikariConfig") config: HikariConfig): HikariDataSource {
        return HikariDataSource(config)
    }

    @Bean
    fun coreJdbcTemplate(@Qualifier("coreDataSource") dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }
}
