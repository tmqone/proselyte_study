package com.tmq.util;

import org.flywaydb.core.Flyway;

public class FlywayUtil {
    private static final Flyway flyway = Flyway.configure()
            .dataSource(
                    PropertiesUtil.get("hibernate.connection.url"),
                    PropertiesUtil.get("hibernate.connection.username"),
                    PropertiesUtil.get("hibernate.connection.password"))
            .locations(PropertiesUtil.get("flyway.migrations.path"))
            .baselineOnMigrate(true)
            .driver(PropertiesUtil.get("hibernate.connection.driver_class"))
            .cleanDisabled(false)
            .load();

    public static void migrate() {
        flyway.migrate();
    }

    public static void clean(){
        flyway.clean();
    }
}
