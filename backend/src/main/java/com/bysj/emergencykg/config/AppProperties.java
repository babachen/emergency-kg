package com.bysj.emergencykg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Jwt jwt = new Jwt();
    private Storage storage = new Storage();
    private Ai ai = new Ai();
    private Neo4j neo4j = new Neo4j();
    @Data public static class Jwt { private String secret; private Integer expireHours = 24; }
    @Data public static class Storage { private String location; private String accessPath; }
    @Data public static class Ai { private String provider = "mock"; private String baseUrl; private String projectId; private Integer timeoutMs = 60000; private Boolean mockEnabled = true; }
    @Data public static class Neo4j { private Boolean enabled = false; private Boolean syncOnStartup = true; private String uri; private String username; private String password; }
}
