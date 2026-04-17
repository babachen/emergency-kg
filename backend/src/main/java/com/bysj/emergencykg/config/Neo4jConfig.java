package com.bysj.emergencykg.config;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class Neo4jConfig {
    @Bean(destroyMethod = "close")
    public Driver neo4jDriver(AppProperties appProperties) {
        if (!Boolean.TRUE.equals(appProperties.getNeo4j().getEnabled())) {
            log.info("Neo4j 未启用，系统将使用 MySQL 图谱降级实现");
            return null;
        }
        return GraphDatabase.driver(appProperties.getNeo4j().getUri(), AuthTokens.basic(appProperties.getNeo4j().getUsername(), appProperties.getNeo4j().getPassword()));
    }
}
