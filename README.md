# 基于大语言模型的应急预案知识图谱构建

## 1. 项目简介
本项目面向毕业设计答辩场景，围绕“全国省市级防汛应急预案”的采集、预处理、知识抽取、知识图谱构建与智能应用，提供一套**可运行、可演示、可扩展**的前后端完整系统。

核心能力包括：
- 登录认证、用户/角色/菜单/权限管理
- 省市应急预案文档上传、文本录入、分段预处理
- 大语言模型知识抽取任务管理
- 实体、关系、三元组管理
- 图谱检索、图谱可视化、Cypher 封装
- 知识问答、规则推理、冲突检测、知识补全
- 图谱版本管理与统计看板
- AI 调用记录、操作日志、字典数据支撑

## 2. 技术栈
- 后端：Java 17、Spring Boot 3、MyBatis-Plus、MySQL、JWT、SpringDoc
- 前端：Vue 3、Vite、Element Plus、Pinia、Vue Router、ECharts
- 图谱层：MySQL 图谱降级实现 + Neo4j 可选接入
- AI 层：bysj-chat-api 标准适配层 + Mock 降级实现

## 3. 目录结构
```text
.
├── backend/                # Spring Boot 后端工程
├── frontend/               # Vue 3 + Vite 前端工程
├── sql/                    # 数据库建表与演示数据脚本
├── docs/                   # 系统设计与配置文档
└── README.md               # 项目总说明
```

## 4. 环境要求
- JDK 17+
- Maven 3.6+
- Node.js 20+
- MySQL 8.0+
- （可选）Neo4j 5+

## 5. 数据库初始化
1. 创建数据库并导入结构：
```sql
source sql/01_schema.sql;
```
2. 导入演示数据：
```sql
source sql/02_demo_data.sql;
```

默认数据库名：`emergency_kg`

## 6. 后端启动步骤
1. 进入后端目录：
```bash
cd backend
```
2. 如本机 Maven 默认仓库不可写，可使用本项目内 settings：
```bash
mvn -s .mvn-settings.xml clean spring-boot:run
```
3. 默认后端地址：
- 接口根路径：`http://127.0.0.1:8080/api`
- Swagger 文档：`http://127.0.0.1:8080/swagger-ui.html`

## 7. 前端启动步骤
1. 进入前端目录：
```bash
cd frontend
```
2. 安装依赖：
```bash
npm install
```
3. 启动开发环境：
```bash
npm run dev
```
4. 默认前端地址：
- `http://127.0.0.1:5173`

> 若本机 npm 执行受安全策略影响，可优先在可联网且允许脚本执行的环境中执行 `npm install`。

## 8. 默认账号
| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 系统管理员 | admin | 123456 | 拥有全部权限 |
| 知识分析员 | analyst | 123456 | 侧重图谱检索、问答、推理 |
| 数据维护员 | operator | 123456 | 侧重预案录入、预处理、任务执行 |

## 9. AI 配置方式
后端 `backend/src/main/resources/application.yml` 中已预留：
```yaml
app:
  ai:
    provider: mock   # mock / bysj
    base-url: http://aichat.bysj.site
    project-id: emergency_plan_kg
    timeout-ms: 60000
    mock-enabled: true
```

### 9.1 默认运行方式
- 默认 `mock` 模式即可运行，不依赖外部 AI 服务。
- 系统会记录 AI 调用日志，并使用规则+Mock 结果完成演示。

### 9.2 切换为真实 bysj-chat-api
将配置改为：
```yaml
app:
  ai:
    provider: bysj
    mock-enabled: false
    base-url: http://aichat.bysj.site
    project-id: 你的项目ID
```

当前已封装标准接口，若真实响应字段与平台实际略有差异，可在：
- `backend/src/main/java/com/bysj/emergencykg/api/BysjChatAiClient.java`
中调整 `extractText()` 解析逻辑。

## 10. Neo4j 配置方式
默认关闭 Neo4j，系统使用 MySQL 图谱降级实现。

开启方式：
```yaml
app:
  neo4j:
    enabled: true
    uri: bolt://127.0.0.1:7687
    username: neo4j
    password: 12345678
```

说明：
- `GraphStoreClient` 已做接口封装
- 当 Neo4j 不可用时，自动降级到 MySQL 图谱查询
- 当前系统已经支持 Cypher 查询入口与降级提示，便于答辩展示“可扩展性”

## 11. 开题报告缺失信息的合理补全说明
由于开题报告未给出部分细节，本系统做了以下合理补全：
1. 增补了 RBAC 权限模型（用户/角色/菜单/按钮）
2. 为演示完整闭环，补充了操作日志、字典、AI 调用记录
3. 文档解析默认支持“手动录入正文 + txt 文件读取”，Word/PDF 可后续扩展接入 POI/PDFBox
4. AI 抽取默认采用“规则 + Mock 降级”方式，已保留真实 bysj-chat-api 适配层
5. Neo4j 作为可选增强层，不替代 MySQL 业务主库

## 12. 重点演示路径建议
1. 管理员登录 -> 首页看板
2. 预案管理 -> 新增/查看预案 -> 执行文本分段
3. 抽取任务 -> 新建任务 -> 执行任务
4. 图谱中心 -> 查看实体/三元组 -> 图谱检索可视化
5. 问答推理 -> 输入问题展示问答与推理链
6. 质量评估 -> 处理冲突 / 应用补全
7. 版本管理 -> 创建图谱版本
