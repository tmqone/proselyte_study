package com.tmq.transaction_service.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Configuration
@EnableConfigurationProperties(FlywayConfig.FlywayShardProperties.class)
public class FlywayConfig {

    private final FlywayShardProperties properties;

    public FlywayConfig(FlywayShardProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void migrateAll() {
        for (FlywayShardProperties.ShardDataSource shard : properties.getShards()) {
            Flyway.configure()
                .dataSource(shard.getUrl(), shard.getUser(), shard.getPassword())
                .locations("classpath:db/migration")
                .schemas("transaction")
                .createSchemas(true)
                .baselineOnMigrate(true)
                .load()
                .migrate();
        }
    }

    @ConfigurationProperties(prefix = "app.flyway")
    public static class FlywayShardProperties {

        private List<ShardDataSource> shards;

        public List<ShardDataSource> getShards() { return shards; }
        public void setShards(List<ShardDataSource> shards) { this.shards = shards; }

        public static class ShardDataSource {
            private String url;
            private String user;
            private String password;

            public String getUrl() { return url; }
            public void setUrl(String url) { this.url = url; }

            public String getUser() { return user; }
            public void setUser(String user) { this.user = user; }

            public String getPassword() { return password; }
            public void setPassword(String password) { this.password = password; }
        }
    }
}