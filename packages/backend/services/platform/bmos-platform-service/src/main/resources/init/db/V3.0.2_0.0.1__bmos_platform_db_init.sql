### 修正bp_code_rule表的name字段去除英文统一中文
-- 修正lims请验单内置编号
UPDATE bp_code_rule SET name = '检验请验单内置编号' WHERE code = 'lims.inspect.order.code';

-- 修正MES物料件编号
UPDATE bp_code_rule SET name = '生产物料件编号' WHERE code = 'mes.storage.material.serial';

-- 修正WMS物料件编号
UPDATE bp_code_rule SET name = '仓储物料件编号' WHERE code = 'wms.inventory.serial';

### 修正bp_code_rule_version表的version字段格式统一为V大写开头的1.0格式）
-- 修正称量工单流水号版本格式
UPDATE bp_code_rule_version SET version = 'V1.0' WHERE rule_code = 'weigh.ticket.serial' AND version = '1';

-- 修正批记录编号规则版本格式（已经是V1格式，修正为V1.0）
UPDATE bp_code_rule_version SET version = 'V1.0' WHERE rule_code = 'batch.record.archive.serial' AND version = 'V1';

-- 修正批签发编号规则版本格式（已经是V1格式，修正为V1.0）
UPDATE bp_code_rule_version SET version = 'V1.0' WHERE rule_code = 'lot.release.serial' AND version = 'V1';

-- 修正数据集流水号版本格式
UPDATE bp_code_rule_version SET version = 'V1.0' WHERE rule_code = 'dataset.key.serial' AND version = '1';

-- 修正数据点流水号版本格式
UPDATE bp_code_rule_version SET version = 'V1.0' WHERE rule_code = 'dataset.point.key.serial' AND version = '1';

-- 修正称量任务编号版本格式
UPDATE bp_code_rule_version SET version = 'V1.0' WHERE rule_code = 'weigh.task.serial' AND version = '1';

-- 修正MES物料件编号版本格式
UPDATE bp_code_rule_version SET version = 'V1.0' WHERE rule_code = 'mes.storage.material.serial' AND version = 'v1.0';

-- 修正WMS物料件编号版本格式
UPDATE bp_code_rule_version SET version = 'V1.0' WHERE rule_code = 'wms.inventory.serial' AND version = 'v1.0';

-- 修正lims请验单内置编号版本格式
UPDATE bp_code_rule_version SET version = 'V1.0' WHERE rule_code = 'lims.inspect.order.code' AND version = 'v1.0';

-- 修正lims请验单内置编号版本格式（v1.1版本）
UPDATE bp_code_rule_version SET version = 'V1.1' WHERE rule_code = 'lims.inspect.order.code' AND version = 'v1.1'; 