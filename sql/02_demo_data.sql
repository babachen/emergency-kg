USE `emergency_kg`;
SET NAMES utf8mb4;

INSERT INTO `sys_role` (`id`,`role_code`,`role_name`,`description`,`status`,`create_time`,`update_time`) VALUES
(1,'admin','系统管理员','拥有全部业务与系统管理权限',1,NOW(),NOW()),
(2,'analyst','知识分析员','负责预案分析、图谱查询、问答推理',1,NOW(),NOW()),
(3,'operator','数据维护员','负责预案录入、预处理、抽取任务执行',1,NOW(),NOW());

INSERT INTO `sys_user` (`id`,`username`,`password`,`real_name`,`phone`,`email`,`avatar`,`status`,`last_login_time`,`remark`,`create_time`,`update_time`) VALUES
(1,'admin','123456','系统管理员','13800000001','admin@demo.com',NULL,1,NOW(),'默认管理员账号',NOW(),NOW()),
(2,'analyst','123456','知识分析员','13800000002','analyst@demo.com',NULL,1,NOW(),'负责图谱分析与问答演示',NOW(),NOW()),
(3,'operator','123456','数据维护员','13800000003','operator@demo.com',NULL,1,NOW(),'负责预案上传和抽取执行',NOW(),NOW());

INSERT INTO `sys_user_role` (`id`,`user_id`,`role_id`) VALUES
(1,1,1),(2,2,2),(3,3,3);

INSERT INTO `sys_menu` (`id`,`parent_id`,`menu_name`,`menu_type`,`route_path`,`component_path`,`icon`,`permission_code`,`sort_order`,`visible`,`status`,`create_time`,`update_time`) VALUES
(1,0,'首页看板','MENU','/dashboard','views/dashboard/Dashboard.vue','DataAnalysis','dashboard:view',1,1,1,NOW(),NOW()),
(10,0,'系统管理','CATALOG','/system','layout','Setting',NULL,10,1,1,NOW(),NOW()),
(11,10,'用户管理','MENU','/system/users','views/system/UserManage.vue','User','system:user:view',11,1,1,NOW(),NOW()),
(12,10,'角色管理','MENU','/system/roles','views/system/RoleManage.vue','Avatar','system:role:view',12,1,1,NOW(),NOW()),
(13,10,'菜单管理','MENU','/system/menus','views/system/MenuManage.vue','Menu','system:menu:view',13,1,1,NOW(),NOW()),
(14,10,'操作日志','MENU','/system/logs','views/system/LogManage.vue','Document','system:log:view',14,1,1,NOW(),NOW()),
(15,11,'新增用户','BUTTON',NULL,NULL,NULL,'system:user:add',15,1,1,NOW(),NOW()),
(16,11,'编辑用户','BUTTON',NULL,NULL,NULL,'system:user:edit',16,1,1,NOW(),NOW()),
(17,11,'删除用户','BUTTON',NULL,NULL,NULL,'system:user:delete',17,1,1,NOW(),NOW()),
(18,12,'新增角色','BUTTON',NULL,NULL,NULL,'system:role:add',18,1,1,NOW(),NOW()),
(19,12,'编辑角色','BUTTON',NULL,NULL,NULL,'system:role:edit',19,1,1,NOW(),NOW()),
(20,12,'删除角色','BUTTON',NULL,NULL,NULL,'system:role:delete',20,1,1,NOW(),NOW()),
(21,13,'新增菜单','BUTTON',NULL,NULL,NULL,'system:menu:add',21,1,1,NOW(),NOW()),
(22,13,'编辑菜单','BUTTON',NULL,NULL,NULL,'system:menu:edit',22,1,1,NOW(),NOW()),
(23,13,'删除菜单','BUTTON',NULL,NULL,NULL,'system:menu:delete',23,1,1,NOW(),NOW()),
(30,0,'预案管理','CATALOG','/document','layout','Document',NULL,30,1,1,NOW(),NOW()),
(31,30,'预案文档','MENU','/document/manage','views/document/DocumentManage.vue','Files','document:view',31,1,1,NOW(),NOW()),
(32,30,'文本分段','MENU','/document/sections','views/document/SectionManage.vue','Tickets','document:view',32,1,1,NOW(),NOW()),
(33,31,'上传附件','BUTTON',NULL,NULL,NULL,'document:upload',33,1,1,NOW(),NOW()),
(34,31,'新增预案','BUTTON',NULL,NULL,NULL,'document:add',34,1,1,NOW(),NOW()),
(35,31,'编辑预案','BUTTON',NULL,NULL,NULL,'document:edit',35,1,1,NOW(),NOW()),
(36,31,'删除预案','BUTTON',NULL,NULL,NULL,'document:delete',36,1,1,NOW(),NOW()),
(37,31,'预案预处理','BUTTON',NULL,NULL,NULL,'document:preprocess',37,1,1,NOW(),NOW()),
(40,0,'抽取中心','CATALOG','/extraction','layout','Cpu',NULL,40,1,1,NOW(),NOW()),
(41,40,'抽取任务','MENU','/extraction/tasks','views/extraction/TaskManage.vue','Operation','extract:task:view',41,1,1,NOW(),NOW()),
(42,41,'新增任务','BUTTON',NULL,NULL,NULL,'extract:task:add',42,1,1,NOW(),NOW()),
(43,41,'执行任务','BUTTON',NULL,NULL,NULL,'extract:task:run',43,1,1,NOW(),NOW()),
(50,0,'图谱中心','CATALOG','/kg','layout','Share',NULL,50,1,1,NOW(),NOW()),
(51,50,'本体管理','MENU','/kg/ontology','views/kg/OntologyManage.vue','Collection','kg:ontology:view',51,1,1,NOW(),NOW()),
(52,50,'实体管理','MENU','/kg/entities','views/kg/EntityManage.vue','Connection','kg:entity:view',52,1,1,NOW(),NOW()),
(53,50,'三元组管理','MENU','/kg/triples','views/kg/TripleManage.vue','Tickets','kg:triple:view',53,1,1,NOW(),NOW()),
(54,50,'图谱检索','MENU','/kg/graph','views/kg/GraphView.vue','Share','kg:graph:view',54,1,1,NOW(),NOW()),
(55,50,'问答推理','MENU','/kg/qa','views/kg/QaReasoning.vue','ChatLineRound','kg:qa:use',55,1,1,NOW(),NOW()),
(56,50,'质量评估','MENU','/kg/quality','views/kg/QualityManage.vue','Warning','kg:quality:view',56,1,1,NOW(),NOW()),
(57,50,'版本管理','MENU','/kg/versions','views/kg/VersionManage.vue','Clock','kg:version:view',57,1,1,NOW(),NOW()),
(58,51,'本体编辑','BUTTON',NULL,NULL,NULL,'kg:ontology:edit',58,1,1,NOW(),NOW()),
(59,55,'规则推理','BUTTON',NULL,NULL,NULL,'kg:reasoning:use',59,1,1,NOW(),NOW()),
(60,56,'质量处理','BUTTON',NULL,NULL,NULL,'kg:quality:edit',60,1,1,NOW(),NOW()),
(61,57,'新增版本','BUTTON',NULL,NULL,NULL,'kg:version:add',61,1,1,NOW(),NOW());

INSERT INTO `sys_role_menu` (`id`,`role_id`,`menu_id`) VALUES
(1,1,1),(2,1,10),(3,1,11),(4,1,12),(5,1,13),(6,1,14),(7,1,15),(8,1,16),(9,1,17),(10,1,18),(11,1,19),(12,1,20),(13,1,21),(14,1,22),(15,1,23),(16,1,30),(17,1,31),(18,1,32),(19,1,33),(20,1,34),(21,1,35),(22,1,36),(23,1,37),(24,1,40),(25,1,41),(26,1,42),(27,1,43),(28,1,50),(29,1,51),(30,1,52),(31,1,53),(32,1,54),(33,1,55),(34,1,56),(35,1,57),(36,1,58),(37,1,59),(38,1,60),(39,1,61),
(40,2,1),(41,2,30),(42,2,31),(43,2,32),(44,2,40),(45,2,41),(46,2,50),(47,2,51),(48,2,52),(49,2,53),(50,2,54),(51,2,55),(52,2,56),(53,2,57),(54,2,59),
(55,3,1),(56,3,30),(57,3,31),(58,3,32),(59,3,33),(60,3,34),(61,3,35),(62,3,37),(63,3,40),(64,3,41),(65,3,42),(66,3,43),(67,3,50),(68,3,52),(69,3,53),(70,3,54),(71,3,56);

INSERT INTO `sys_dict_type` (`id`,`dict_code`,`dict_name`,`description`,`create_time`,`update_time`) VALUES
(1,'document_status','预案状态字典','用于展示预案抽取与分段状态',NOW(),NOW()),
(2,'task_status','任务状态字典','用于展示抽取任务状态',NOW(),NOW()),
(3,'conflict_status','冲突状态字典','用于展示冲突处理状态',NOW(),NOW());

INSERT INTO `sys_dict_data` (`id`,`dict_type_code`,`dict_label`,`dict_value`,`css_type`,`sort_order`,`status`,`create_time`,`update_time`) VALUES
(1,'document_status','未处理','0','info',1,1,NOW(),NOW()),(2,'document_status','已分段','1','success',2,1,NOW(),NOW()),(3,'document_status','未抽取','0','info',3,1,NOW(),NOW()),(4,'document_status','待抽取','1','warning',4,1,NOW(),NOW()),(5,'document_status','抽取中','2','warning',5,1,NOW(),NOW()),(6,'document_status','已完成','3','success',6,1,NOW(),NOW()),(7,'document_status','失败','4','danger',7,1,NOW(),NOW()),
(8,'task_status','待执行','0','info',1,1,NOW(),NOW()),(9,'task_status','执行中','1','warning',2,1,NOW(),NOW()),(10,'task_status','已完成','2','success',3,1,NOW(),NOW()),(11,'task_status','失败','3','danger',4,1,NOW(),NOW()),
(12,'conflict_status','待处理','0','warning',1,1,NOW(),NOW()),(13,'conflict_status','已处理','1','success',2,1,NOW(),NOW());

INSERT INTO `file_storage` (`id`,`business_type`,`original_name`,`stored_name`,`file_path`,`file_url`,`file_size`,`content_type`,`uploader_id`,`create_time`) VALUES
(1,'plan','浙江省防汛应急预案.txt','zhejiang_plan.txt','/demo/files/zhejiang_plan.txt','/uploads/demo/zhejiang_plan.txt',15230,'text/plain',1,NOW()),
(2,'plan','广东省防汛应急预案.txt','guangdong_plan.txt','/demo/files/guangdong_plan.txt','/uploads/demo/guangdong_plan.txt',17420,'text/plain',1,NOW());

INSERT INTO `region` (`id`,`region_code`,`region_name`,`region_level`,`parent_id`,`sort_order`,`status`,`create_time`,`update_time`) VALUES
(1,'11','北京市','省级',0,1,1,NOW(),NOW()),
(2,'12','天津市','省级',0,2,1,NOW(),NOW()),
(3,'13','河北省','省级',0,3,1,NOW(),NOW()),
(4,'14','山西省','省级',0,4,1,NOW(),NOW()),
(5,'15','内蒙古自治区','省级',0,5,1,NOW(),NOW()),
(6,'21','辽宁省','省级',0,6,1,NOW(),NOW()),
(7,'22','吉林省','省级',0,7,1,NOW(),NOW()),
(8,'23','黑龙江省','省级',0,8,1,NOW(),NOW()),
(9,'31','上海市','省级',0,9,1,NOW(),NOW()),
(10,'32','江苏省','省级',0,10,1,NOW(),NOW()),
(11,'33','浙江省','省级',0,11,1,NOW(),NOW()),
(12,'34','安徽省','省级',0,12,1,NOW(),NOW()),
(13,'35','福建省','省级',0,13,1,NOW(),NOW()),
(14,'36','江西省','省级',0,14,1,NOW(),NOW()),
(15,'37','山东省','省级',0,15,1,NOW(),NOW()),
(16,'41','河南省','省级',0,16,1,NOW(),NOW()),
(17,'42','湖北省','省级',0,17,1,NOW(),NOW()),
(18,'43','湖南省','省级',0,18,1,NOW(),NOW()),
(19,'44','广东省','省级',0,19,1,NOW(),NOW()),
(20,'45','广西壮族自治区','省级',0,20,1,NOW(),NOW()),
(21,'46','海南省','省级',0,21,1,NOW(),NOW()),
(22,'50','重庆市','省级',0,22,1,NOW(),NOW()),
(23,'51','四川省','省级',0,23,1,NOW(),NOW()),
(24,'52','贵州省','省级',0,24,1,NOW(),NOW()),
(25,'53','云南省','省级',0,25,1,NOW(),NOW()),
(26,'54','西藏自治区','省级',0,26,1,NOW(),NOW()),
(27,'61','陕西省','省级',0,27,1,NOW(),NOW()),
(28,'62','甘肃省','省级',0,28,1,NOW(),NOW()),
(29,'63','青海省','省级',0,29,1,NOW(),NOW()),
(30,'64','宁夏回族自治区','省级',0,30,1,NOW(),NOW()),
(31,'65','新疆维吾尔自治区','省级',0,31,1,NOW(),NOW());

INSERT INTO `entity_type` (`id`,`type_code`,`type_name`,`color`,`description`,`create_time`,`update_time`) VALUES
(1,'机构','机构','#3b82f6','应急管理部门、气象部门、水利部门、指挥部等',NOW(),NOW()),
(2,'任务','任务','#8b5cf6','预警发布、人员转移、风险研判等应急任务',NOW(),NOW()),
(3,'资源','资源','#f97316','冲锋舟、沙袋、照明设备等应急资源',NOW(),NOW()),
(4,'地点','地点','#06b6d4','区域、站点、重点部位',NOW(),NOW()),
(5,'时间','时间','#22c55e','汛期、响应时段等时间要素',NOW(),NOW()),
(6,'阶段','阶段','#ef4444','监测预警、响应处置、恢复重建等阶段',NOW(),NOW());

INSERT INTO `relation_type` (`id`,`relation_code`,`relation_name`,`description`,`direction_desc`,`create_time`,`update_time`) VALUES
(1,'负责','负责','主体负责执行某项任务','主体 -> 任务',NOW(),NOW()),
(2,'协同','协同','两个主体在应急处置中协同配合','主体 -> 主体',NOW(),NOW()),
(3,'调用','调用','主体或任务调用应急资源','主体/任务 -> 资源',NOW(),NOW()),
(4,'前置','前置','任务之间存在前置依赖关系','任务 -> 任务',NOW(),NOW()),
(5,'后置','后置','任务之间存在后续依赖关系','任务 -> 任务',NOW(),NOW());

CREATE TEMPORARY TABLE `tmp_entity_template` (
  `slot` INT PRIMARY KEY,
  `entity_name` VARCHAR(100) NOT NULL,
  `entity_type_id` BIGINT NOT NULL,
  `use_region_prefix` TINYINT NOT NULL,
  `description` VARCHAR(255) NOT NULL
);

INSERT INTO `tmp_entity_template` (`slot`,`entity_name`,`entity_type_id`,`use_region_prefix`,`description`) VALUES
(1,'防汛指挥部',1,1,'统一指挥风险研判、预警发布和应急响应'),
(2,'应急管理部门',1,1,'牵头组织人员转移与抢险力量协调'),
(3,'气象部门',1,1,'负责监测预报和降雨趋势分析'),
(4,'水利部门',1,1,'负责河道堤防巡查和水工程调度'),
(5,'交通运输部门',1,1,'负责交通保通和重点路段管控'),
(6,'风险研判',2,0,'研判雨情、水情、工情和险情'),
(7,'预警发布',2,0,'面向防汛响应的预警信息发布'),
(8,'人员转移',2,0,'组织危险区域群众转移避险'),
(9,'冲锋舟',3,0,'用于涉水救援和转运'),
(10,'沙袋',3,0,'用于封堵漫溢和临时加固'),
(11,'应急照明设备',3,0,'用于夜间抢险和巡查照明'),
(12,'排涝泵站',3,0,'用于城市内涝和低洼区域排水'),
(13,'应急通信设备',3,0,'用于多部门协同联络'),
(14,'无人机',3,0,'用于巡查侦察和灾情回传');

CREATE TEMPORARY TABLE `tmp_entity_template_obj` AS
SELECT * FROM `tmp_entity_template`;

CREATE TEMPORARY TABLE `tmp_relation_template` (
  `slot` INT PRIMARY KEY,
  `subject_slot` INT NOT NULL,
  `relation_type_id` BIGINT NOT NULL,
  `relation_name` VARCHAR(50) NOT NULL,
  `object_slot` INT NOT NULL
);

INSERT INTO `tmp_relation_template` (`slot`,`subject_slot`,`relation_type_id`,`relation_name`,`object_slot`) VALUES
(1,1,1,'负责',6),
(2,1,1,'负责',7),
(3,2,1,'负责',8),
(4,3,2,'协同',1),
(5,4,2,'协同',1),
(6,5,2,'协同',2),
(7,6,4,'前置',7),
(8,7,4,'前置',8),
(9,8,3,'调用',9),
(10,2,3,'调用',10),
(11,1,3,'调用',11),
(12,2,3,'调用',12),
(13,2,3,'调用',13),
(14,1,3,'调用',14);

INSERT INTO `plan_document` (`id`,`title`,`region_id`,`plan_year`,`plan_type`,`source_url`,`file_id`,`file_name`,`file_path`,`content`,`preprocess_status`,`extraction_status`,`publish_org`,`approval_date`,`version_no`,`summary`,`create_by`,`create_time`,`update_time`)
SELECT
  1000 + r.id AS id,
  CASE
    WHEN r.id IN (1,2,9,10,11,13,19,20,21) THEN CONCAT(r.region_name, '防汛防台风应急预案')
    WHEN r.id IN (17,23,24,25,26,29) THEN CONCAT(r.region_name, '山洪灾害防御应急预案')
    ELSE CONCAT(r.region_name, '防汛应急预案')
  END AS title,
  r.id AS region_id,
  2026 AS plan_year,
  CASE
    WHEN r.id IN (1,2,9,10,11,13,19,20,21) THEN '防汛防台风应急预案'
    WHEN r.id IN (17,23,24,25,26,29) THEN '山洪灾害防御预案'
    ELSE '防汛应急预案'
  END AS plan_type,
  CONCAT('https://example.com/plans/', r.region_code) AS source_url,
  CASE WHEN r.id = 11 THEN 1 WHEN r.id = 19 THEN 2 ELSE NULL END AS file_id,
  CASE WHEN r.id = 11 THEN '浙江省防汛应急预案.txt' WHEN r.id = 19 THEN '广东省防汛应急预案.txt' ELSE NULL END AS file_name,
  CASE WHEN r.id = 11 THEN '/demo/files/zhejiang_plan.txt' WHEN r.id = 19 THEN '/demo/files/guangdong_plan.txt' ELSE NULL END AS file_path,
  CONCAT(
    '一、总则\n', r.region_name, '防汛指挥部负责统一指挥风险研判、预警发布、人员转移和应急响应。\n',
    '二、会商联动\n', r.region_name, '应急管理部门、气象部门、水利部门、交通运输部门滚动开展联合会商，分析雨情、水情、工情和险情。\n',
    '三、资源保障\n根据响应等级组织堤防巡查、群众转移、排涝抢险和应急通信保障，调用冲锋舟、沙袋、排涝泵站、应急照明设备、无人机等资源。',
    CASE
      WHEN r.id IN (1,2,9,10,11,13,19,20,21) THEN '\n四、沿海台风防御\n同步落实海堤巡查、避风港调度和沿海危险区域人员转移。'
      WHEN r.id IN (17,23,24,25,26,29) THEN '\n四、山洪地灾防御\n强化山洪沟道巡查、地灾隐患点转移和监测预警联动。'
      ELSE '\n四、城市内涝防控\n强化易涝点值守、重点地下空间防护和排水调度。'
    END
  ) AS content,
  1 AS preprocess_status,
  3 AS extraction_status,
  CASE WHEN r.id IN (1,2,9,22) THEN CONCAT(r.region_name, '应急管理局') ELSE CONCAT(r.region_name, '应急管理厅') END AS publish_org,
  DATE_FORMAT(DATE_ADD('2026-01-01', INTERVAL r.id DAY), '%Y-%m-%d') AS approval_date,
  CONCAT('V', 1 + MOD(r.id, 3), '.', MOD(r.id, 2)) AS version_no,
  CONCAT('覆盖', r.region_name, '防汛组织体系、监测预警、响应处置与资源保障链路，可用于跨省知识图谱检索与推理。') AS summary,
  1 AS create_by,
  NOW(),
  NOW()
FROM `region` r
ORDER BY r.id;

INSERT INTO `plan_section` (`id`,`document_id`,`section_no`,`section_title`,`section_level`,`section_content`,`word_count`,`create_time`,`update_time`)
SELECT
  5000 + base.region_id * 10 + base.section_idx AS id,
  base.document_id,
  CONCAT('S00', base.section_idx) AS section_no,
  base.section_title,
  1 AS section_level,
  base.section_content,
  CHAR_LENGTH(base.section_content) AS word_count,
  NOW(),
  NOW()
FROM (
  SELECT r.id AS region_id, 1000 + r.id AS document_id, 1 AS section_idx, '一、总则' AS section_title,
         CONCAT(r.region_name, '防汛指挥部负责统一指挥风险研判、预警发布、人员转移和应急响应。') AS section_content
  FROM `region` r
  UNION ALL
  SELECT r.id, 1000 + r.id, 2, '二、会商联动',
         CONCAT(r.region_name, '应急管理部门、气象部门、水利部门、交通运输部门联合会商，滚动分析雨情、水情、工情和险情。')
  FROM `region` r
  UNION ALL
  SELECT r.id, 1000 + r.id, 3, '三、响应措施',
         CONCAT('根据响应级别组织预警发布、人员转移、堤防巡查、排涝抢险和应急通信保障，调用冲锋舟、沙袋、应急照明设备、排涝泵站。')
  FROM `region` r
  UNION ALL
  SELECT r.id, 1000 + r.id, 4, '四、专项防御',
         CASE
           WHEN r.id IN (1,2,9,10,11,13,19,20,21) THEN '同步落实台风防御、海堤巡查和沿海危险区域人员转移。'
           WHEN r.id IN (17,23,24,25,26,29) THEN '强化山洪沟道巡查、地灾隐患点转移和监测预警联动。'
           ELSE '强化易涝点值守、重点地下空间防护和排水调度。'
         END
  FROM `region` r
) base
ORDER BY base.region_id, base.section_idx;

INSERT INTO `extraction_task` (`id`,`document_id`,`task_name`,`model_name`,`prompt_template`,`task_status`,`progress_percent`,`extracted_count`,`started_at`,`finished_at`,`error_message`,`create_by`,`create_time`,`update_time`)
SELECT
  6000 + r.id AS id,
  1000 + r.id AS document_id,
  CONCAT(r.region_name, '预案首轮抽取') AS task_name,
  '应急抽取助手-Mock' AS model_name,
  '请抽取主体、任务、资源、前置关系与跨部门协同链路' AS prompt_template,
  2 AS task_status,
  100 AS progress_percent,
  14 AS extracted_count,
  NOW(),
  NOW(),
  NULL,
  1 AS create_by,
  NOW(),
  NOW()
FROM `region` r
ORDER BY r.id;

INSERT INTO `extraction_task` (`id`,`document_id`,`task_name`,`model_name`,`prompt_template`,`task_status`,`progress_percent`,`extracted_count`,`started_at`,`finished_at`,`error_message`,`create_by`,`create_time`,`update_time`) VALUES
(7001,1011,'浙江省补充抽取任务','应急抽取助手-Mock','补充细化台风防御和应急通信资源',0,0,0,NULL,NULL,NULL,3,NOW(),NOW()),
(7002,1019,'广东省沿海资源增强抽取','应急抽取助手-Mock','补充沿海危险区域联动资源与转移链路',1,66,9,NOW(),NULL,NULL,3,NOW(),NOW()),
(7003,1023,'四川省山洪专项抽取重试','应急抽取助手-Mock','补充山洪沟道预警和地灾隐患转移链路',3,100,0,NOW(),NOW(),'原始预案段落中专题附录缺失，待人工补录后重试。',3,NOW(),NOW());

INSERT INTO `kg_entity` (`id`,`entity_name`,`entity_type_id`,`region_id`,`source_document_id`,`description`,`confidence`,`status`,`create_time`,`update_time`)
SELECT
  20000 + r.id * 100 + t.slot AS id,
  CASE WHEN t.use_region_prefix = 1 THEN CONCAT(r.region_name, t.entity_name) ELSE t.entity_name END AS entity_name,
  t.entity_type_id,
  r.id AS region_id,
  1000 + r.id AS source_document_id,
  CASE
    WHEN t.use_region_prefix = 1 THEN CONCAT(r.region_name, t.description)
    ELSE CONCAT(t.description, '（', r.region_name, '）')
  END AS description,
  ROUND(0.90 + MOD(r.id + t.slot, 6) * 0.01, 2) AS confidence,
  1 AS status,
  NOW(),
  NOW()
FROM `region` r
JOIN `tmp_entity_template` t
ORDER BY r.id, t.slot;

INSERT INTO `kg_relation` (`id`,`subject_entity_id`,`relation_type_id`,`object_entity_id`,`source_document_id`,`relation_desc`,`confidence`,`create_time`,`update_time`)
SELECT
  30000 + r.id * 100 + rel.slot AS id,
  20000 + r.id * 100 + rel.subject_slot AS subject_entity_id,
  rel.relation_type_id,
  20000 + r.id * 100 + rel.object_slot AS object_entity_id,
  1000 + r.id AS source_document_id,
  rel.relation_name AS relation_desc,
  ROUND(0.90 + MOD(r.id + rel.slot, 6) * 0.01, 2) AS confidence,
  NOW(),
  NOW()
FROM `region` r
JOIN `tmp_relation_template` rel
ORDER BY r.id, rel.slot;

INSERT INTO `kg_triple` (`id`,`subject_name`,`predicate_name`,`object_name`,`subject_entity_id`,`relation_id`,`object_entity_id`,`source_document_id`,`version_id`,`confidence`,`validation_status`,`create_time`,`update_time`)
SELECT
  40000 + r.id * 100 + rel.slot AS id,
  CASE WHEN subj.use_region_prefix = 1 THEN CONCAT(r.region_name, subj.entity_name) ELSE subj.entity_name END AS subject_name,
  rel.relation_name AS predicate_name,
  CASE WHEN obj.use_region_prefix = 1 THEN CONCAT(r.region_name, obj.entity_name) ELSE obj.entity_name END AS object_name,
  20000 + r.id * 100 + rel.subject_slot AS subject_entity_id,
  30000 + r.id * 100 + rel.slot AS relation_id,
  20000 + r.id * 100 + rel.object_slot AS object_entity_id,
  1000 + r.id AS source_document_id,
  2 AS version_id,
  ROUND(0.90 + MOD(r.id + rel.slot, 6) * 0.01, 2) AS confidence,
  CASE
    WHEN r.id IN (10,19,23) AND rel.slot IN (11,12) THEN 2
    ELSE 1
  END AS validation_status,
  NOW(),
  NOW()
FROM `region` r
JOIN `tmp_relation_template` rel
JOIN `tmp_entity_template` subj ON subj.slot = rel.subject_slot
JOIN `tmp_entity_template_obj` obj ON obj.slot = rel.object_slot
ORDER BY r.id, rel.slot;

INSERT INTO `knowledge_conflict` (`id`,`triple_id`,`conflict_type`,`conflict_desc`,`status`,`suggested_resolution`,`create_time`,`update_time`) VALUES
(8001,41011,'资源配置冲突','江苏省预案中夜间抢险照明资源与部分市级预案表述不一致。',0,'建议核验最新生效版省级预案，并统一照明资源目录命名。',NOW(),NOW()),
(8002,41912,'排涝职责边界冲突','广东省预案中排涝泵站调度主体在不同附件中存在职责边界差异。',1,'已确认由省级防汛指挥体系统筹、属地部门具体执行。',NOW(),NOW()),
(8003,42311,'山洪夜间巡查资源冲突','四川省山洪防御预案中夜间巡查资源调用方式与专题附录存在差异。',0,'建议补录专项附录后重新校验资源调用链。',NOW(),NOW()),
(8004,42312,'排涝资源适配冲突','四川省山洪场景是否直接调用排涝泵站存在适配争议。',0,'建议按流域洪水与山洪场景拆分资源规则。',NOW(),NOW());

INSERT INTO `knowledge_completion` (`id`,`document_id`,`completion_type`,`missing_subject`,`missing_predicate`,`missing_object`,`suggestion_content`,`status`,`apply_result`,`create_time`,`update_time`)
SELECT
  50000 + r.id * 10 + 1 AS id,
  1000 + r.id AS document_id,
  '知识补全' AS completion_type,
  CONCAT(r.region_name, '通信保障组') AS missing_subject,
  '负责' AS missing_predicate,
  '应急通信保障' AS missing_object,
  CONCAT('建议补充', r.region_name, '通信保障组与指挥体系、资源调度之间的职责关系。') AS suggestion_content,
  0 AS status,
  NULL AS apply_result,
  NOW(),
  NOW()
FROM `region` r
UNION ALL
SELECT
  50000 + r.id * 10 + 2 AS id,
  1000 + r.id AS document_id,
  '知识补全' AS completion_type,
  CONCAT(r.region_name, '防汛指挥部') AS missing_subject,
  '调用' AS missing_predicate,
  '无人机' AS missing_object,
  CONCAT('建议补充', r.region_name, '无人机巡查、灾情回传与重点部位巡检链路。') AS suggestion_content,
  CASE WHEN r.id IN (11,19,23) THEN 1 ELSE 0 END AS status,
  CASE WHEN r.id IN (11,19,23) THEN CONCAT('已生成补全三元组：', r.region_name, '防汛指挥部 - 调用 - 无人机') ELSE NULL END AS apply_result,
  NOW(),
  NOW()
FROM `region` r;

INSERT INTO `graph_version` (`id`,`version_name`,`version_no`,`source_desc`,`node_count`,`relation_count`,`triple_count`,`quality_score`,`published_status`,`create_time`,`update_time`) VALUES
(1,'样本验证版','v2026.03','基于首批 8 个省级预案的抽取样本形成基线图谱。',96,112,112,88.50,2,NOW(),NOW());

INSERT INTO `graph_version` (`id`,`version_name`,`version_no`,`source_desc`,`node_count`,`relation_count`,`triple_count`,`quality_score`,`published_status`,`create_time`,`update_time`)
SELECT
  2 AS id,
  '31省整合演示版' AS version_name,
  'v2026.04' AS version_no,
  CONCAT('覆盖 ', COUNT(*), ' 个省级单位预案，支持区域检索、知识问答、冲突检测、知识补全与 Neo4j 图查询同步。') AS source_desc,
  (SELECT COUNT(*) FROM `kg_entity`) AS node_count,
  (SELECT COUNT(*) FROM `kg_relation`) AS relation_count,
  (SELECT COUNT(*) FROM `kg_triple`) AS triple_count,
  96.20 AS quality_score,
  1 AS published_status,
  NOW(),
  NOW()
FROM `region`;

INSERT INTO `ai_call_record` (`id`,`biz_type`,`biz_id`,`provider_name`,`project_id`,`request_prompt`,`response_text`,`call_status`,`duration_ms`,`error_message`,`create_time`) VALUES
(1,'EXTRACTION',6011,'mock','emergency_plan_kg','抽取浙江省预案中的主体、任务、资源与关系链路。','已生成 14 条结构化三元组，并产出通信保障与无人机巡查补全建议。',1,118,NULL,NOW()),
(2,'EXTRACTION',6019,'mock','emergency_plan_kg','抽取广东省预案中的沿海台风防御与跨部门协同链路。','已生成 14 条三元组，识别沿海危险区域转移与排涝资源调用。',1,126,NULL,NOW()),
(3,'QA',1019,'mock','emergency_plan_kg','广东省预案中哪些主体负责预警发布与人员转移？','广东省防汛指挥部负责预警发布，广东省应急管理部门负责人员转移，并与气象、水利、交通运输部门协同。',1,104,NULL,NOW()),
(4,'REASONING',1023,'mock','emergency_plan_kg','四川省山洪预案中风险研判如何影响人员转移？','风险研判前置于预警发布，预警发布再前置于人员转移，形成典型的山洪转移推理链。',1,97,NULL,NOW()),
(5,'EXTRACTION',7003,'mock','emergency_plan_kg','重试四川省山洪专项抽取。','专题附录缺失，需补录后重新执行。',0,91,'示例失败记录',NOW());

INSERT INTO `sys_operation_log` (`id`,`module_name`,`operation_type`,`operator_name`,`operator_id`,`content`,`ip_address`,`create_time`) VALUES
(1,'认证中心','登录','系统管理员',1,'系统管理员 登录系统','127.0.0.1',NOW()),
(2,'预案管理','导入','数据维护员',3,'批量导入 31 个省级防汛预案并完成统一格式整理','127.0.0.1',NOW()),
(3,'预案管理','预处理','数据维护员',3,'完成 31 个省级预案分段，共生成 124 个文本分段','127.0.0.1',NOW()),
(4,'抽取任务','执行','数据维护员',3,'执行省级预案抽取任务，共生成 434 条图谱关系链路','127.0.0.1',NOW()),
(5,'知识补全','应用','知识分析员',2,'已应用浙江、广东、四川三地无人机巡查补全建议','127.0.0.1',NOW()),
(6,'图谱版本','创建','系统管理员',1,'创建图谱版本：31省整合演示版，并支持 Neo4j 同步','127.0.0.1',NOW());

DROP TEMPORARY TABLE IF EXISTS `tmp_relation_template`;
DROP TEMPORARY TABLE IF EXISTS `tmp_entity_template_obj`;
DROP TEMPORARY TABLE IF EXISTS `tmp_entity_template`;
