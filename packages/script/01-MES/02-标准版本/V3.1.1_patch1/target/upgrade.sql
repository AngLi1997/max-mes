-- WMS 物料件入库标签打印场景配置
-- 场景：物料入库后给物料件打印标签
-- 字段：物料名称、物料编码、规格、物料件号、物料件量、单位名称

INSERT INTO bmos_platform.bp_tag_scene (id, tag_scene_name, tag_scene_desc, tag_type_id, data_source_service_name,
                                        data_source_interface, qr_code_field, sort, create_time, update_time, create_by,
                                        update_by, is_deleted)
VALUES (130020006, 'WMS物料件标签', 'WMS物料件入库标签打印', 7, 'bmos-wms-service', '/api/app/wms/inventory/printTag',
        'id', 9110, '2026-04-17 00:00:00', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020006001, 130020006, 'cargoName', '物料名称', 'String', '氯化钠', '2026-04-17 00:00:00', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020006002, 130020006, 'cargoCode', '物料编码', 'String', 'WH030101', '2026-04-17 00:00:00', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020006003, 130020006, 'specification', '规格', 'String', '500g/袋', '2026-04-17 00:00:00', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020006004, 130020006, 'inventoryNo', '物料件号', 'String', 'INV20260001', '2026-04-17 00:00:00', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020006005, 130020006, 'initQuantity', '物料件量', 'String', '100.000', '2026-04-17 00:00:00', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time,
                                              update_time, create_by, update_by, is_deleted)
VALUES (130020006006, 130020006, 'unit', '单位名称', 'String', '袋', '2026-04-17 00:00:00', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_instance (id, tag_name, tag_type_id, tag_scene_id, tag_define_id, config_fields,
                                           is_enable, create_time, update_time, create_by, update_by, is_deleted)
VALUES (130020006, 'WMS物料件标签', 7, 130020006, 1,
        '[{"label":"物料名称","defineField":"field1","dataSourceField":"cargoName","consumeValue":null},{"label":"物料编码","defineField":"field2","dataSourceField":"cargoCode","consumeValue":null},{"label":"物料件号","defineField":"field3","dataSourceField":"inventoryNo","consumeValue":null},{"label":"物料件量","defineField":"field4","dataSourceField":"initQuantity","consumeValue":null},{"label":"单位名称","defineField":"field5","dataSourceField":"unit","consumeValue":null}]',
        'TRUE', '2026-04-17 00:00:00', null, null, null, 0);

UPDATE bmos_platform.bp_tag_define
SET preview_html = '<!DOCTYPE html\n  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">\n<html xmlns="http://www.w3.org/1999/xhtml" lang="en">\n\n<head>\n  <meta charset="UTF-8" />\n  <meta name="viewport"\n    content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />\n  <meta http-equiv="X-UA-Compatible" content="ie=edge" />\n  <title>Document</title>\n\n  <style>\n    * {\n      margin: 0;\n      padding: 0;\n    }\n\n    .fields-item {\n      margin-bottom: 4px;\n    }\n\n    #tag {\n      width: 560px;\n      height: 320px;\n      box-sizing: border-box;\n      background-color: #f8f8f8;\n      border: 1px solid #e1e1e1;\n      border-radius: 10px;\n      overflow: hidden;\n      display: flex;\n      justify-content: center;\n      font-size: 20px;\n    }\n  </style>\n</head>\n\n<body>\n  <div id="tag">\n    <div style="float: left; width: 300px; padding: 20px 20px; box-sizing: border-box">\n      <div id="field1" class="fields-item"></div>\n      <div id="field2" class="fields-item"></div>\n      <div id="field3" class="fields-item"></div>\n      <div id="field4" class="fields-item"></div>\n      <div id="field5" class="fields-item"></div>\n      <div id="field6" class="fields-item"></div>\n      <div id="field7" class="fields-item"></div>\n      <div id="field8" class="fields-item"></div>\n      <div id="field9" class="fields-item"></div>\n    </div>\n    <div style="float: right; width: 256px; padding: 50px 20px; box-sizing: border-box">\n      <img id="qrCode" width="200" height="200" src="" />\n    </div>\n  </div>\n</body>\n\n</html>'
WHERE id = 1;
