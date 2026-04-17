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

INSERT INTO `plan_document` (`id`,`title`,`region_id`,`plan_year`,`plan_type`,`source_url`,`file_id`,`file_name`,`file_path`,`content`,`preprocess_status`,`extraction_status`,`publish_org`,`approval_date`,`version_no`,`summary`,`create_by`,`create_time`,`update_time`) VALUES
(1,'北京市防汛防台风应急预案',1,2026,'防汛应急预案','https://example.com/beijing',NULL,NULL,NULL,'一、总则
北京市防汛指挥部负责统一指挥预警发布、险情研判和应急响应。
二、组织体系
市应急管理局会同市气象局、市水务局开展会商。
三、处置措施
遇到强降雨时组织人员转移，调用冲锋舟、沙袋和应急照明设备。',1,3,'北京市应急管理局','2026-02-10','V1.0','覆盖预警发布、人员转移、物资调度等关键环节。',1,NOW(),NOW()),
(2,'天津市城市防汛应急预案',2,2026,'防汛应急预案','https://example.com/tianjin',NULL,NULL,NULL,'一、工作原则
天津市防汛指挥部负责统筹指挥。
二、职责分工
气象局负责监测预报，水务局负责堤防巡查。
三、保障措施
组织抢险队伍开展人员疏散与物资调配。',1,3,'天津市应急管理局','2026-02-12','V1.1','突出城市内涝防控与联动会商机制。',1,NOW(),NOW()),
(3,'河北省防汛抗旱应急预案',3,2026,'防汛应急预案','https://example.com/hebei',NULL,NULL,NULL,'一、总则
河北省防汛抗旱指挥部负责统筹防汛抗旱工作。
二、监测预警
气象部门与水利部门协同开展风险研判和预警发布。
三、应急处置
需要时转移危险区群众，启用应急物资仓库。',1,3,'河北省应急管理厅','2026-02-20','V1.0','适用于汛期强降雨、山洪灾害和内涝场景。',1,NOW(),NOW()),
(4,'江苏省防汛防旱应急预案',10,2026,'防汛应急预案','https://example.com/jiangsu',NULL,NULL,NULL,'一、组织指挥
江苏省防汛指挥部负责统一调度。
二、响应机制
响应前先完成风险研判和预警发布。
三、资源保障
调用应急通信设备、冲锋舟和排涝泵站。',1,3,'江苏省应急管理厅','2026-03-01','V2.0','强化风险研判前置与资源调度效率。',1,NOW(),NOW()),
(5,'浙江省防汛防台抗旱应急预案',11,2026,'防汛应急预案','https://example.com/zhejiang',1,'浙江省防汛应急预案.txt','/demo/files/zhejiang_plan.txt','一、总则
浙江省防汛防台抗旱指挥部负责统一领导和指挥。
二、会商研判
气象局、水利厅、应急管理厅协同开展会商。
三、应急响应
组织预警发布、人员转移、应急通信保障和物资调度。',1,3,'浙江省应急管理厅','2026-03-05','V2.1','适用于台风、强降雨、山洪及城市内涝场景。',1,NOW(),NOW()),
(6,'安徽省防汛应急预案',12,2026,'防汛应急预案','https://example.com/anhui',NULL,NULL,NULL,'一、组织体系
安徽省防汛抗旱指挥部负责总体协调。
二、预警研判
气象部门和水文部门联合研判。
三、处置措施
调用沙袋、照明设备和救援队伍开展抢险。',1,1,'安徽省应急管理厅','2026-03-08','V1.2','突出洪水与内涝双场景协同。',1,NOW(),NOW()),
(7,'湖北省防汛抗洪应急预案',17,2026,'防汛应急预案','https://example.com/hubei',NULL,NULL,NULL,'一、总则
湖北省防汛抗旱指挥部负责组织指挥。
二、职责
水利部门负责堤防险情处置，应急部门负责救援力量协调。
三、响应
组织巡查、转移群众和启用物资仓库。',1,1,'湖北省应急管理厅','2026-03-11','V1.0','适用于流域洪水和堤防险情。',1,NOW(),NOW()),
(8,'广东省防汛防风应急预案',19,2026,'防汛应急预案','https://example.com/guangdong',2,'广东省防汛应急预案.txt','/demo/files/guangdong_plan.txt','一、总则
广东省防总负责统一指挥。
二、会商联动
气象局、海洋部门和应急管理厅协同会商。
三、处置措施
组织沿海危险区域人员转移并调用通信和照明设备。',1,3,'广东省应急管理厅','2026-03-15','V2.0','强调台风叠加暴雨的跨部门联动机制。',1,NOW(),NOW()),
(9,'四川省山洪灾害防御应急预案',23,2026,'山洪防御预案','https://example.com/sichuan',NULL,NULL,NULL,'一、总则
四川省山洪灾害防御指挥部负责统筹。
二、预警发布
自然资源、气象、水利部门联合发布山洪预警。
三、应急响应
组织转移避险、物资调配与通信保障。',1,2,'四川省应急管理厅','2026-03-18','V1.0','突出山洪地灾联动处置。',1,NOW(),NOW()),
(10,'广西壮族自治区防汛应急预案',20,2026,'防汛应急预案','https://example.com/guangxi',NULL,NULL,NULL,'一、总则
自治区防汛抗旱指挥部负责统一领导。
二、应急响应
先开展风险研判，再组织预警发布和群众转移。
三、保障措施
调用冲锋舟、编织袋、应急发电设备。',1,4,'广西壮族自治区应急管理厅','2026-03-21','V1.0','当前处于抽取失败示例状态，便于答辩演示。',1,NOW(),NOW());

INSERT INTO `plan_section` (`id`,`document_id`,`section_no`,`section_title`,`section_level`,`section_content`,`word_count`,`create_time`,`update_time`) VALUES
(1,1,'S001','一、总则',1,'北京市防汛指挥部负责统一指挥预警发布、险情研判和应急响应。',33,NOW(),NOW()),
(2,1,'S002','二、组织体系',1,'市应急管理局会同市气象局、市水务局开展会商。',24,NOW(),NOW()),
(3,1,'S003','三、处置措施',1,'遇到强降雨时组织人员转移，调用冲锋舟、沙袋和应急照明设备。',32,NOW(),NOW()),
(4,5,'S001','一、总则',1,'浙江省防汛防台抗旱指挥部负责统一领导和指挥。',24,NOW(),NOW()),
(5,5,'S002','二、会商研判',1,'气象局、水利厅、应急管理厅协同开展会商。',22,NOW(),NOW()),
(6,5,'S003','三、应急响应',1,'组织预警发布、人员转移、应急通信保障和物资调度。',28,NOW(),NOW()),
(7,8,'S001','一、总则',1,'广东省防总负责统一指挥。',12,NOW(),NOW()),
(8,8,'S002','二、会商联动',1,'气象局、海洋部门和应急管理厅协同会商。',21,NOW(),NOW()),
(9,8,'S003','三、处置措施',1,'组织沿海危险区域人员转移并调用通信和照明设备。',27,NOW(),NOW()),
(10,10,'S001','一、总则',1,'自治区防汛抗旱指挥部负责统一领导。',18,NOW(),NOW());

INSERT INTO `extraction_task` (`id`,`document_id`,`task_name`,`model_name`,`prompt_template`,`task_status`,`progress_percent`,`extracted_count`,`started_at`,`finished_at`,`error_message`,`create_by`,`create_time`,`update_time`) VALUES
(1,1,'北京预案首轮抽取','应急抽取助手-Mock','请抽取主体、任务、资源与关系三元组',2,100,6,NOW(),NOW(),NULL,1,NOW(),NOW()),
(2,5,'浙江预案首轮抽取','应急抽取助手-Mock','请抽取主体、任务、资源与关系三元组',2,100,7,NOW(),NOW(),NULL,1,NOW(),NOW()),
(3,8,'广东预案跨部门抽取','应急抽取助手-Mock','强调沿海台风与暴雨联动主体抽取',2,100,6,NOW(),NOW(),NULL,1,NOW(),NOW()),
(4,9,'四川山洪预案抽取','应急抽取助手-Mock','突出山洪预警、转移避险和通信保障',1,55,3,NOW(),NULL,NULL,3,NOW(),NOW()),
(5,10,'广西预案失败示例','应急抽取助手-Mock','执行抽取并展示失败状态',3,100,0,NOW(),NOW(),'示例：原始文本缺少可识别结构，需人工补录正文。',3,NOW(),NOW()),
(6,6,'安徽预案待执行任务','应急抽取助手-Mock','待执行的补充抽取任务',0,0,0,NULL,NULL,NULL,3,NOW(),NOW());

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

INSERT INTO `kg_entity` (`id`,`entity_name`,`entity_type_id`,`region_id`,`source_document_id`,`description`,`confidence`,`status`,`create_time`,`update_time`) VALUES
(1,'北京市防汛指挥部',1,1,1,'负责北京市预警发布与应急响应',0.92,1,NOW(),NOW()),
(2,'预警发布',2,1,1,'应急预警任务',0.95,1,NOW(),NOW()),
(3,'人员转移',2,1,1,'群众转移避险任务',0.93,1,NOW(),NOW()),
(4,'冲锋舟',3,1,1,'水上救援装备',0.90,1,NOW(),NOW()),
(5,'浙江省防汛防台抗旱指挥部',1,11,5,'浙江省防汛防台统一指挥主体',0.94,1,NOW(),NOW()),
(6,'浙江省应急管理厅',1,11,5,'浙江省应急管理主体',0.91,1,NOW(),NOW()),
(7,'防汛物资仓库',3,11,5,'物资调配节点',0.88,1,NOW(),NOW()),
(8,'广东省防总',1,19,8,'广东省防汛防风总指挥主体',0.93,1,NOW(),NOW()),
(9,'应急通信保障',2,19,8,'通信保障任务',0.89,1,NOW(),NOW()),
(10,'应急照明设备',3,19,8,'夜间救援设备',0.87,1,NOW(),NOW()),
(11,'风险研判',2,10,4,'处置前置任务',0.92,1,NOW(),NOW()),
(12,'江苏省防汛指挥部',1,10,4,'江苏省统一调度主体',0.90,1,NOW(),NOW());

INSERT INTO `kg_relation` (`id`,`subject_entity_id`,`relation_type_id`,`object_entity_id`,`source_document_id`,`relation_desc`,`confidence`,`create_time`,`update_time`) VALUES
(1,1,1,2,1,'负责',0.92,NOW(),NOW()),
(2,1,1,3,1,'负责',0.91,NOW(),NOW()),
(3,3,3,4,1,'调用',0.90,NOW(),NOW()),
(4,5,1,2,5,'负责',0.94,NOW(),NOW()),
(5,6,1,3,5,'负责',0.91,NOW(),NOW()),
(6,6,3,7,5,'调用',0.88,NOW(),NOW()),
(7,8,1,2,8,'负责',0.93,NOW(),NOW()),
(8,8,1,9,8,'负责',0.89,NOW(),NOW()),
(9,9,3,10,8,'调用',0.87,NOW(),NOW()),
(10,11,4,2,4,'前置',0.92,NOW(),NOW()),
(11,12,1,11,4,'负责',0.90,NOW(),NOW()),
(12,12,3,10,4,'调用',0.86,NOW(),NOW());

INSERT INTO `kg_triple` (`id`,`subject_name`,`predicate_name`,`object_name`,`subject_entity_id`,`relation_id`,`object_entity_id`,`source_document_id`,`version_id`,`confidence`,`validation_status`,`create_time`,`update_time`) VALUES
(1,'北京市防汛指挥部','负责','预警发布',1,1,2,1,2,0.92,1,NOW(),NOW()),
(2,'北京市防汛指挥部','负责','人员转移',1,2,3,1,2,0.91,1,NOW(),NOW()),
(3,'人员转移','调用','冲锋舟',3,3,4,1,2,0.90,1,NOW(),NOW()),
(4,'浙江省防汛防台抗旱指挥部','负责','预警发布',5,4,2,5,2,0.94,1,NOW(),NOW()),
(5,'浙江省应急管理厅','负责','人员转移',6,5,3,5,2,0.91,1,NOW(),NOW()),
(6,'浙江省应急管理厅','调用','防汛物资仓库',6,6,7,5,2,0.88,1,NOW(),NOW()),
(7,'广东省防总','负责','预警发布',8,7,2,8,2,0.93,1,NOW(),NOW()),
(8,'广东省防总','负责','应急通信保障',8,8,9,8,2,0.89,1,NOW(),NOW()),
(9,'应急通信保障','调用','应急照明设备',9,9,10,8,2,0.87,1,NOW(),NOW()),
(10,'风险研判','前置','预警发布',11,10,2,4,1,0.92,1,NOW(),NOW()),
(11,'江苏省防汛指挥部','负责','风险研判',12,11,11,4,1,0.90,1,NOW(),NOW()),
(12,'江苏省防汛指挥部','调用','应急照明设备',12,12,10,4,1,0.86,2,NOW(),NOW());

INSERT INTO `knowledge_conflict` (`id`,`triple_id`,`conflict_type`,`conflict_desc`,`status`,`suggested_resolution`,`create_time`,`update_time`) VALUES
(1,12,'资源冲突','江苏省防汛指挥部在不同版本预案中对照明资源调用对象存在差异。',0,'建议核验最新生效版预案，并结合地市物资目录统一客体名称。',NOW(),NOW()),
(2,8,'职责边界冲突','广东省防总与地方应急通信保障组在通信保障职责边界表述不一致。',1,'已通过人工确认保留省级总指挥部统筹、专项保障组执行的双层结构。',NOW(),NOW());

INSERT INTO `knowledge_completion` (`id`,`document_id`,`completion_type`,`missing_subject`,`missing_predicate`,`missing_object`,`suggestion_content`,`status`,`apply_result`,`create_time`,`update_time`) VALUES
(1,6,'知识补全','安徽省通信保障组','负责','应急通信保障','建议补充通信保障主体，完善夜间救援和跨区域联动保障。',0,NULL,NOW(),NOW()),
(2,7,'知识补全','湖北省防汛抗旱指挥部','调用','应急照明设备','建议补充夜间巡堤与抢险照明资源。',0,NULL,NOW(),NOW()),
(3,8,'知识补全','广东省防总','调用','应急救援直升机','建议补充沿海孤岛和高风险区域空中救援资源。',1,'已生成补全三元组：广东省防总 - 调用 - 应急救援直升机',NOW(),NOW());

INSERT INTO `graph_version` (`id`,`version_name`,`version_no`,`source_desc`,`node_count`,`relation_count`,`triple_count`,`quality_score`,`published_status`,`create_time`,`update_time`) VALUES
(1,'开题阶段图谱基线版','v2026.03', '基于首批 4 份预案抽取结果生成',8,8,8,88.50,2,NOW(),NOW()),
(2,'答辩演示版','v2026.04', '结合 10 份预案演示数据、冲突检测与补全结果生成',12,12,12,93.00,1,NOW(),NOW());

INSERT INTO `ai_call_record` (`id`,`biz_type`,`biz_id`,`provider_name`,`project_id`,`request_prompt`,`response_text`,`call_status`,`duration_ms`,`error_message`,`create_time`) VALUES
(1,'EXTRACTION',1,'mock','emergency_plan_kg','请抽取北京市预案中的主体、任务、资源与关系。','已生成 6 条示例三元组。',1,118,NULL,NOW()),
(2,'QA',8,'mock','emergency_plan_kg','广东预案中哪些主体负责通信保障？','广东省防总负责统筹通信保障任务，并可调用照明设备。',1,103,NULL,NOW()),
(3,'EXTRACTION',5,'mock','emergency_plan_kg','执行广西预案抽取。','原始文本结构不足，建议补录正文后重试。',0,96,'示例失败记录',NOW());

INSERT INTO `sys_operation_log` (`id`,`module_name`,`operation_type`,`operator_name`,`operator_id`,`content`,`ip_address`,`create_time`) VALUES
(1,'认证中心','登录','系统管理员',1,'系统管理员 登录系统','127.0.0.1',NOW()),
(2,'预案管理','新增','数据维护员',3,'新增预案：浙江省防汛防台抗旱应急预案','127.0.0.1',NOW()),
(3,'预案管理','预处理','数据维护员',3,'完成预案分段：浙江省防汛防台抗旱应急预案，共 3 段','127.0.0.1',NOW()),
(4,'抽取任务','执行','数据维护员',3,'执行抽取任务：浙江预案首轮抽取，生成三元组 7 条','127.0.0.1',NOW()),
(5,'知识补全','应用','知识分析员',2,'已生成补全三元组：广东省防总 - 调用 - 应急救援直升机','127.0.0.1',NOW()),
(6,'图谱版本','创建','系统管理员',1,'创建图谱版本：答辩演示版','127.0.0.1',NOW());
