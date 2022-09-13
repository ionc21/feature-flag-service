package com.mettle.flyway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfiguration {

    private final boolean cleanDatabaseOnStartup;

    public FlywayConfiguration(@Value("${spring.flyway.cleanDatabaseOnStartup}") boolean cleanDatabaseOnStartup) {
        this.cleanDatabaseOnStartup = cleanDatabaseOnStartup;
    }

    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            if (cleanDatabaseOnStartup) {
                flyway.clean();
            }
            flyway.migrate();
        };
    }
}
