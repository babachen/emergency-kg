CREATE DATABASE IF NOT EXISTS `emergency_kg` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `emergency_kg`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `ai_call_record`;
DROP TABLE IF EXISTS `graph_version`;
DROP TABLE IF EXISTS `knowledge_completion`;
DROP TABLE IF EXISTS `knowledge_conflict`;
DROP TABLE IF EXISTS `kg_triple`;
DROP TABLE IF EXISTS `kg_relation`;
DROP TABLE IF EXISTS `kg_entity`;
DROP TABLE IF EXISTS `relation_type`;
DROP TABLE IF EXISTS `entity_type`;
DROP TABLE IF EXISTS `extraction_task`;
DROP TABLE IF EXISTS `plan_section`;
DROP TABLE IF EXISTS `plan_document`;
DROP TABLE IF EXISTS `region`;
DROP TABLE IF EXISTS `file_storage`;
DROP TABLE IF EXISTS `sys_dict_data`;
DROP TABLE IF EXISTS `sys_dict_type`;
DROP TABLE IF EXISTS `sys_operation_log`;
DROP TABLE IF EXISTS `sys_role_menu`;
DROP TABLE IF EXISTS `sys_user_role`;
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '登录用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '登录密码',
  `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0停用 1启用',
  `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '角色说明',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0停用 1启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

CREATE TABLE `sys_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父级菜单ID',
  `menu_name` VARCHAR(100) NOT NULL COMMENT '菜单名称',
  `menu_type` VARCHAR(20) NOT NULL COMMENT '菜单类型：CATALOG目录 MENU菜单 BUTTON按钮',
  `route_path` VARCHAR(200) DEFAULT NULL COMMENT '前端路由路径',
  `component_path` VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
  `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标名称',
  `permission_code` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
  `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示：0否 1是',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0停用 1启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单与权限表';

CREATE TABLE `sys_user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  KEY `idx_sys_user_role_user` (`user_id`),
  KEY `idx_sys_user_role_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

CREATE TABLE `sys_role_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  KEY `idx_sys_role_menu_role` (`role_id`),
  KEY `idx_sys_role_menu_menu` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

CREATE TABLE `sys_operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `module_name` VARCHAR(100) NOT NULL COMMENT '模块名称',
  `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
  `operator_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人姓名',
  `operator_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
  `content` VARCHAR(500) DEFAULT NULL COMMENT '操作内容',
  `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';

CREATE TABLE `sys_dict_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典类型ID',
  `dict_code` VARCHAR(50) NOT NULL COMMENT '字典编码',
  `dict_name` VARCHAR(100) NOT NULL COMMENT '字典名称',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_dict_type_code` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

CREATE TABLE `sys_dict_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典数据ID',
  `dict_type_code` VARCHAR(50) NOT NULL COMMENT '字典类型编码',
  `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
  `dict_value` VARCHAR(100) NOT NULL COMMENT '字典值',
  `css_type` VARCHAR(30) DEFAULT NULL COMMENT '样式类型',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0停用 1启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

CREATE TABLE `file_storage` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `business_type` VARCHAR(50) NOT NULL COMMENT '业务类型',
  `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
  `stored_name` VARCHAR(255) NOT NULL COMMENT '存储文件名',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件物理路径',
  `file_url` VARCHAR(500) NOT NULL COMMENT '文件访问URL',
  `file_size` BIGINT DEFAULT NULL COMMENT '文件大小',
  `content_type` VARCHAR(100) DEFAULT NULL COMMENT '文件类型',
  `uploader_id` BIGINT DEFAULT NULL COMMENT '上传人ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件存储表';

CREATE TABLE `region` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '区域ID',
  `region_code` VARCHAR(20) NOT NULL COMMENT '区域编码',
  `region_name` VARCHAR(50) NOT NULL COMMENT '区域名称',
  `region_level` VARCHAR(20) NOT NULL COMMENT '区域层级',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父级区域ID',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0停用 1启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_region_code` (`region_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='省市区域表';

CREATE TABLE `plan_document` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '预案文档ID',
  `title` VARCHAR(255) NOT NULL COMMENT '预案标题',
  `region_id` BIGINT NOT NULL COMMENT '所属区域ID',
  `plan_year` INT DEFAULT NULL COMMENT '预案年份',
  `plan_type` VARCHAR(100) DEFAULT NULL COMMENT '预案类型',
  `source_url` VARCHAR(500) DEFAULT NULL COMMENT '来源链接',
  `file_id` BIGINT DEFAULT NULL COMMENT '附件文件ID',
  `file_name` VARCHAR(255) DEFAULT NULL COMMENT '附件名称',
  `file_path` VARCHAR(500) DEFAULT NULL COMMENT '附件路径',
  `content` LONGTEXT COMMENT '预案正文内容',
  `preprocess_status` TINYINT NOT NULL DEFAULT 0 COMMENT '预处理状态：0未处理 1已分段',
  `extraction_status` TINYINT NOT NULL DEFAULT 0 COMMENT '抽取状态：0未抽取 1待抽取 2抽取中 3已完成 4失败',
  `publish_org` VARCHAR(255) DEFAULT NULL COMMENT '发布单位',
  `approval_date` VARCHAR(50) DEFAULT NULL COMMENT '审批日期',
  `version_no` VARCHAR(50) DEFAULT NULL COMMENT '版本号',
  `summary` VARCHAR(1000) DEFAULT NULL COMMENT '预案摘要',
  `create_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_plan_document_region` (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应急预案文档表';

CREATE TABLE `plan_section` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分段ID',
  `document_id` BIGINT NOT NULL COMMENT '预案文档ID',
  `section_no` VARCHAR(50) NOT NULL COMMENT '分段编号',
  `section_title` VARCHAR(255) NOT NULL COMMENT '章节标题',
  `section_level` INT NOT NULL DEFAULT 1 COMMENT '章节层级',
  `section_content` LONGTEXT COMMENT '章节内容',
  `word_count` INT NOT NULL DEFAULT 0 COMMENT '字数统计',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_plan_section_doc` (`document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预案文本分段表';

CREATE TABLE `extraction_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '抽取任务ID',
  `document_id` BIGINT NOT NULL COMMENT '关联文档ID',
  `task_name` VARCHAR(255) NOT NULL COMMENT '任务名称',
  `model_name` VARCHAR(100) DEFAULT NULL COMMENT '模型名称',
  `prompt_template` LONGTEXT COMMENT '提示模板',
  `task_status` TINYINT NOT NULL DEFAULT 0 COMMENT '任务状态：0待执行 1执行中 2已完成 3失败',
  `progress_percent` INT NOT NULL DEFAULT 0 COMMENT '进度百分比',
  `extracted_count` INT NOT NULL DEFAULT 0 COMMENT '抽取数量',
  `started_at` DATETIME DEFAULT NULL COMMENT '开始时间',
  `finished_at` DATETIME DEFAULT NULL COMMENT '完成时间',
  `error_message` VARCHAR(1000) DEFAULT NULL COMMENT '错误信息',
  `create_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_extraction_task_doc` (`document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='大模型知识抽取任务表';

CREATE TABLE `entity_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '实体类型ID',
  `type_code` VARCHAR(50) NOT NULL COMMENT '实体类型编码',
  `type_name` VARCHAR(100) NOT NULL COMMENT '实体类型名称',
  `color` VARCHAR(50) DEFAULT NULL COMMENT '展示颜色',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '说明',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_entity_type_code` (`type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识图谱实体类型表';

CREATE TABLE `relation_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关系类型ID',
  `relation_code` VARCHAR(50) NOT NULL COMMENT '关系类型编码',
  `relation_name` VARCHAR(100) NOT NULL COMMENT '关系类型名称',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '说明',
  `direction_desc` VARCHAR(100) DEFAULT NULL COMMENT '方向说明',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_relation_type_code` (`relation_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识图谱关系类型表';

CREATE TABLE `kg_entity` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图谱实体ID',
  `entity_name` VARCHAR(255) NOT NULL COMMENT '实体名称',
  `entity_type_id` BIGINT NOT NULL COMMENT '实体类型ID',
  `region_id` BIGINT DEFAULT NULL COMMENT '所属区域ID',
  `source_document_id` BIGINT DEFAULT NULL COMMENT '来源预案ID',
  `description` VARCHAR(1000) DEFAULT NULL COMMENT '实体描述',
  `confidence` DECIMAL(5,2) DEFAULT NULL COMMENT '置信度',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1有效',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_kg_entity_type` (`entity_type_id`),
  KEY `idx_kg_entity_region` (`region_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识图谱实体表';

CREATE TABLE `kg_relation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图谱关系ID',
  `subject_entity_id` BIGINT NOT NULL COMMENT '主体实体ID',
  `relation_type_id` BIGINT DEFAULT NULL COMMENT '关系类型ID',
  `object_entity_id` BIGINT NOT NULL COMMENT '客体实体ID',
  `source_document_id` BIGINT DEFAULT NULL COMMENT '来源预案ID',
  `relation_desc` VARCHAR(255) DEFAULT NULL COMMENT '关系描述',
  `confidence` DECIMAL(5,2) DEFAULT NULL COMMENT '置信度',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识图谱关系表';

CREATE TABLE `kg_triple` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '三元组ID',
  `subject_name` VARCHAR(255) NOT NULL COMMENT '主体名称',
  `predicate_name` VARCHAR(100) NOT NULL COMMENT '谓词名称',
  `object_name` VARCHAR(255) NOT NULL COMMENT '客体名称',
  `subject_entity_id` BIGINT DEFAULT NULL COMMENT '主体实体ID',
  `relation_id` BIGINT DEFAULT NULL COMMENT '关系ID',
  `object_entity_id` BIGINT DEFAULT NULL COMMENT '客体实体ID',
  `source_document_id` BIGINT DEFAULT NULL COMMENT '来源预案ID',
  `version_id` BIGINT DEFAULT NULL COMMENT '图谱版本ID',
  `confidence` DECIMAL(5,2) DEFAULT NULL COMMENT '置信度',
  `validation_status` TINYINT NOT NULL DEFAULT 0 COMMENT '校验状态：0待校验 1已确认 2存在冲突',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_kg_triple_doc` (`source_document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识三元组表';

CREATE TABLE `knowledge_conflict` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '冲突记录ID',
  `triple_id` BIGINT NOT NULL COMMENT '三元组ID',
  `conflict_type` VARCHAR(100) NOT NULL COMMENT '冲突类型',
  `conflict_desc` VARCHAR(1000) NOT NULL COMMENT '冲突描述',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态：0待处理 1已处理',
  `suggested_resolution` VARCHAR(1000) DEFAULT NULL COMMENT '建议处理方案',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识冲突记录表';

CREATE TABLE `knowledge_completion` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '补全建议ID',
  `document_id` BIGINT NOT NULL COMMENT '预案ID',
  `completion_type` VARCHAR(100) NOT NULL COMMENT '补全类型',
  `missing_subject` VARCHAR(255) NOT NULL COMMENT '缺失主体',
  `missing_predicate` VARCHAR(100) NOT NULL COMMENT '缺失关系',
  `missing_object` VARCHAR(255) NOT NULL COMMENT '缺失客体',
  `suggestion_content` VARCHAR(1000) DEFAULT NULL COMMENT '补全建议内容',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '应用状态：0待应用 1已应用',
  `apply_result` VARCHAR(1000) DEFAULT NULL COMMENT '应用结果',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识补全建议表';

CREATE TABLE `graph_version` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '图谱版本ID',
  `version_name` VARCHAR(100) NOT NULL COMMENT '版本名称',
  `version_no` VARCHAR(50) NOT NULL COMMENT '版本号',
  `source_desc` VARCHAR(500) DEFAULT NULL COMMENT '来源说明',
  `node_count` INT NOT NULL DEFAULT 0 COMMENT '节点数量',
  `relation_count` INT NOT NULL DEFAULT 0 COMMENT '关系数量',
  `triple_count` INT NOT NULL DEFAULT 0 COMMENT '三元组数量',
  `quality_score` DECIMAL(5,2) DEFAULT NULL COMMENT '质量评分',
  `published_status` TINYINT NOT NULL DEFAULT 0 COMMENT '发布状态：0草稿 1当前版本 2已归档',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图谱版本表';

CREATE TABLE `ai_call_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AI调用记录ID',
  `biz_type` VARCHAR(50) NOT NULL COMMENT '业务类型',
  `biz_id` BIGINT DEFAULT NULL COMMENT '业务ID',
  `provider_name` VARCHAR(100) DEFAULT NULL COMMENT '调用提供方',
  `project_id` VARCHAR(100) DEFAULT NULL COMMENT '助手项目ID',
  `request_prompt` LONGTEXT COMMENT '请求提示词',
  `response_text` LONGTEXT COMMENT '响应文本',
  `call_status` TINYINT NOT NULL DEFAULT 1 COMMENT '调用状态：0失败 1成功',
  `duration_ms` BIGINT DEFAULT NULL COMMENT '耗时毫秒',
  `error_message` VARCHAR(1000) DEFAULT NULL COMMENT '错误信息',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI调用记录表';

SET FOREIGN_KEY_CHECKS = 1;
