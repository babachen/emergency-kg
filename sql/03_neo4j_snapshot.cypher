// 独立导入脚本：将 31 省演示图谱直接导入 Neo4j
// 执行位置：Neo4j Browser 或 cypher-shell
// 注意：会清空当前 EmergencyEntity / EMERGENCY_RELATION 图数据

MATCH (n:EmergencyEntity) DETACH DELETE n;

CREATE INDEX emergency_entity_name IF NOT EXISTS FOR (n:EmergencyEntity) ON (n.entityName);
CREATE INDEX emergency_relation_version IF NOT EXISTS FOR ()-[r:EMERGENCY_RELATION]-() ON (r.versionId);

WITH
[
  {id: 1, name: '北京市', title: '北京市防汛防台风应急预案'},
  {id: 2, name: '天津市', title: '天津市防汛防台风应急预案'},
  {id: 3, name: '河北省', title: '河北省防汛应急预案'},
  {id: 4, name: '山西省', title: '山西省防汛应急预案'},
  {id: 5, name: '内蒙古自治区', title: '内蒙古自治区防汛应急预案'},
  {id: 6, name: '辽宁省', title: '辽宁省防汛应急预案'},
  {id: 7, name: '吉林省', title: '吉林省防汛应急预案'},
  {id: 8, name: '黑龙江省', title: '黑龙江省防汛应急预案'},
  {id: 9, name: '上海市', title: '上海市防汛防台风应急预案'},
  {id: 10, name: '江苏省', title: '江苏省防汛防台风应急预案'},
  {id: 11, name: '浙江省', title: '浙江省防汛防台风应急预案'},
  {id: 12, name: '安徽省', title: '安徽省防汛应急预案'},
  {id: 13, name: '福建省', title: '福建省防汛防台风应急预案'},
  {id: 14, name: '江西省', title: '江西省防汛应急预案'},
  {id: 15, name: '山东省', title: '山东省防汛应急预案'},
  {id: 16, name: '河南省', title: '河南省防汛应急预案'},
  {id: 17, name: '湖北省', title: '湖北省山洪灾害防御应急预案'},
  {id: 18, name: '湖南省', title: '湖南省防汛应急预案'},
  {id: 19, name: '广东省', title: '广东省防汛防台风应急预案'},
  {id: 20, name: '广西壮族自治区', title: '广西壮族自治区防汛防台风应急预案'},
  {id: 21, name: '海南省', title: '海南省防汛防台风应急预案'},
  {id: 22, name: '重庆市', title: '重庆市防汛应急预案'},
  {id: 23, name: '四川省', title: '四川省山洪灾害防御应急预案'},
  {id: 24, name: '贵州省', title: '贵州省山洪灾害防御应急预案'},
  {id: 25, name: '云南省', title: '云南省山洪灾害防御应急预案'},
  {id: 26, name: '西藏自治区', title: '西藏自治区山洪灾害防御应急预案'},
  {id: 27, name: '陕西省', title: '陕西省防汛应急预案'},
  {id: 28, name: '甘肃省', title: '甘肃省防汛应急预案'},
  {id: 29, name: '青海省', title: '青海省山洪灾害防御应急预案'},
  {id: 30, name: '宁夏回族自治区', title: '宁夏回族自治区防汛应急预案'},
  {id: 31, name: '新疆维吾尔自治区', title: '新疆维吾尔自治区防汛应急预案'}
] AS regions,
[
  {slot: 1, entityName: '防汛指挥部', category: '机构', useRegionPrefix: true},
  {slot: 2, entityName: '应急管理部门', category: '机构', useRegionPrefix: true},
  {slot: 3, entityName: '气象部门', category: '机构', useRegionPrefix: true},
  {slot: 4, entityName: '水利部门', category: '机构', useRegionPrefix: true},
  {slot: 5, entityName: '交通运输部门', category: '机构', useRegionPrefix: true},
  {slot: 6, entityName: '风险研判', category: '任务', useRegionPrefix: false},
  {slot: 7, entityName: '预警发布', category: '任务', useRegionPrefix: false},
  {slot: 8, entityName: '人员转移', category: '任务', useRegionPrefix: false},
  {slot: 9, entityName: '冲锋舟', category: '资源', useRegionPrefix: false},
  {slot: 10, entityName: '沙袋', category: '资源', useRegionPrefix: false},
  {slot: 11, entityName: '应急照明设备', category: '资源', useRegionPrefix: false},
  {slot: 12, entityName: '排涝泵站', category: '资源', useRegionPrefix: false},
  {slot: 13, entityName: '应急通信设备', category: '资源', useRegionPrefix: false},
  {slot: 14, entityName: '无人机', category: '资源', useRegionPrefix: false}
] AS entityTemplates,
[
  {slot: 1, subjectSlot: 1, predicate: '负责', objectSlot: 6},
  {slot: 2, subjectSlot: 1, predicate: '负责', objectSlot: 7},
  {slot: 3, subjectSlot: 2, predicate: '负责', objectSlot: 8},
  {slot: 4, subjectSlot: 3, predicate: '协同', objectSlot: 1},
  {slot: 5, subjectSlot: 4, predicate: '协同', objectSlot: 1},
  {slot: 6, subjectSlot: 5, predicate: '协同', objectSlot: 2},
  {slot: 7, subjectSlot: 6, predicate: '前置', objectSlot: 7},
  {slot: 8, subjectSlot: 7, predicate: '前置', objectSlot: 8},
  {slot: 9, subjectSlot: 8, predicate: '调用', objectSlot: 9},
  {slot: 10, subjectSlot: 2, predicate: '调用', objectSlot: 10},
  {slot: 11, subjectSlot: 1, predicate: '调用', objectSlot: 11},
  {slot: 12, subjectSlot: 2, predicate: '调用', objectSlot: 12},
  {slot: 13, subjectSlot: 2, predicate: '调用', objectSlot: 13},
  {slot: 14, subjectSlot: 1, predicate: '调用', objectSlot: 14}
] AS relationTemplates
UNWIND regions AS region
UNWIND relationTemplates AS relation
WITH region, relation, entityTemplates,
     head([item IN entityTemplates WHERE item.slot = relation.subjectSlot]) AS subjectTpl,
     head([item IN entityTemplates WHERE item.slot = relation.objectSlot]) AS objectTpl
WITH region, relation, subjectTpl, objectTpl,
     CASE WHEN subjectTpl.useRegionPrefix THEN region.name + subjectTpl.entityName ELSE subjectTpl.entityName END AS subjectName,
     CASE WHEN objectTpl.useRegionPrefix THEN region.name + objectTpl.entityName ELSE objectTpl.entityName END AS objectName
MERGE (subject:EmergencyEntity {entityName: subjectName})
SET subject.category = subjectTpl.category,
    subject.versionId = 2,
    subject.updatedAt = datetime()
MERGE (object:EmergencyEntity {entityName: objectName})
SET object.category = objectTpl.category,
    object.versionId = 2,
    object.updatedAt = datetime()
MERGE (subject)-[edge:EMERGENCY_RELATION {tripleId: 40000 + region.id * 100 + relation.slot}]->(object)
SET edge.predicate = relation.predicate,
    edge.regionId = region.id,
    edge.regionName = region.name,
    edge.documentId = 1000 + region.id,
    edge.documentTitle = region.title,
    edge.versionId = 2,
    edge.updatedAt = datetime();

MATCH (n:EmergencyEntity)
WITH count(n) AS nodeCount
MATCH ()-[r:EMERGENCY_RELATION]->()
RETURN nodeCount, count(r) AS relationCount;
