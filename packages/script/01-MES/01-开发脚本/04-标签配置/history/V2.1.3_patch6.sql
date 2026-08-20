# 调整设备标签字段 by lilong 241125
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"设备名称：","defineField":"field1","dataSourceField":"equipmentName","consumeValue":null},{"label":"设备编号：","defineField":"field2","dataSourceField":"equipmentCode","consumeValue":null},{"label":"打印时间：","defineField":"field3","dataSourceField":"printDate","consumeValue":null},{"label":null,"defineField":"field4","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field5","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field6","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field7","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field8","dataSourceField":null,"consumeValue":null},{"label":null,"defineField":"field9","dataSourceField":null,"consumeValue":null}]' WHERE id = 160004001;
DELETE FROM bmos_platform.bp_tag_scene_field WHERE id IN (
'160004001003',
'160004001004',
'160004001005',
'160004001006'
);
