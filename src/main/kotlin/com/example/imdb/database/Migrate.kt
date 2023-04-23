package com.example.imdb.database

import jakarta.annotation.PostConstruct
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Autowired
import javax.sql.DataSource

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class Migrate {
    @Autowired
    var ds: DataSource? = null

    @PostConstruct
    fun migrateWithFlyway() {
        val flyway: Flyway = Flyway.configure()
            .dataSource(ds)
            .locations("db/migration")
            .load()
        flyway.migrate()
    }
}
