# AI 与 Neo4j 配置说明

## 一、AI 适配层
后端已封装：
- `AiClient`：统一聊天接口
- `BysjChatAiClient`：真实 bysj-chat-api 适配器
- `MockAiClient`：本地降级实现
- `AiSupportService`：统一调用入口与日志记录

### 配置项
```yaml
app:
  ai:
    provider: mock   # mock / bysj
    base-url: http://aichat.bysj.site
    project-id: emergency_plan_kg
    timeout-ms: 60000
    mock-enabled: true
```

### 切换建议
- 答辩演示默认：`provider=mock`
- 真机联调：`provider=bysj` 且 `mock-enabled=false`

## 二、Neo4j 图数据库
系统已预留：
- `GraphStoreClient`：图查询抽象接口
- `MysqlGraphStoreClient`：MySQL 降级实现
- `Neo4jGraphStoreClient`：Neo4j 查询入口与失败降级

### 配置项
```yaml
app:
  neo4j:
    enabled: false
    uri: bolt://127.0.0.1:7687
    username: neo4j
    password: 12345678
```

### 行为说明
- `enabled=false`：全部图查询由 MySQL 降级实现完成
- `enabled=true`：Cypher 查询优先走 Neo4j，失败后自动回退 MySQL

## 三、已知说明
1. 当前版本优先保证“完整骨架 + 核心闭环 + 可演示性”
2. 真实 bysj-chat-api 返回格式可能与平台部署版本存在差异，必要时调整解析方法
3. Neo4j 同步写入可继续在 `syncVersion()` 基础上扩展
