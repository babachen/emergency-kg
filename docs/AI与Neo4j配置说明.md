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
    sync-on-startup: true
    uri: bolt://127.0.0.1:7687
    username: neo4j
    password: 12345678
```

### 行为说明
- `enabled=false`：全部图查询由 MySQL 降级实现完成
- `enabled=true`：图谱检索与 Cypher 查询优先走 Neo4j，失败后自动回退 MySQL
- `sync-on-startup=true`：后端启动完成后，自动将当前 MySQL 图谱快照同步到 Neo4j
- 创建图谱版本时：系统会自动将当前三元组快照同步到 Neo4j
- 若 Neo4j 尚未同步快照：图谱检索会自动回退到 MySQL，并给出提示信息

### 独立脚本导入
若你希望不依赖后端同步，也可直接执行：

- `sql/03_neo4j_snapshot.cypher`

执行方式：
1. 启动 Neo4j
2. 打开 Neo4j Browser
3. 将脚本内容粘贴后执行

说明：
- 脚本会创建索引
- 脚本会清空当前 `EmergencyEntity` / `EMERGENCY_RELATION` 演示图数据后再重建
- 导入结果与项目内 31 省演示图谱结构保持一致

## 三、已知说明
1. 当前版本优先保证“完整骨架 + 核心闭环 + 可演示性”
2. 真实 bysj-chat-api 返回格式可能与平台部署版本存在差异，必要时调整解析方法
3. 当前 Neo4j 同步策略为“发布版本时全量覆盖当前图谱快照”，适合答辩演示；后续可扩展为增量同步
