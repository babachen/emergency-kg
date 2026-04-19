package com.bysj.emergencykg.config;

import com.bysj.emergencykg.service.KnowledgeGraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Neo4jStartupSyncRunner implements ApplicationRunner {
    private final AppProperties appProperties;
    private final KnowledgeGraphService knowledgeGraphService;

    public Neo4jStartupSyncRunner(AppProperties appProperties, KnowledgeGraphService knowledgeGraphService) {
        this.appProperties = appProperties;
        this.knowledgeGraphService = knowledgeGraphService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!Boolean.TRUE.equals(appProperties.getNeo4j().getEnabled())) {
            return;
        }
        if (!Boolean.TRUE.equals(appProperties.getNeo4j().getSyncOnStartup())) {
            log.info("Neo4j 已启用，但已关闭启动自动同步");
            return;
        }
        try {
            knowledgeGraphService.syncGraphStoreSnapshot("应用启动自动同步");
            log.info("Neo4j 启动自动同步完成");
        } catch (Exception ex) {
            log.warn("Neo4j 启动自动同步失败：{}", ex.getMessage(), ex);
        }
    }
}
