INSERT INTO bmos_platform.bp_tag_define (id, barcode_format, tag_style, tag_width, tag_height, cmd, cmd_type, preview_html, max_field_size, create_time, update_time, create_by, update_by, is_deleted) VALUES (2, 'CODE_128', '一维码', 80, 60, '^XA
^CI28^FS
^CW1,E:SIMSUN.FNT^FS
^PW560^FS
^LL320^FS
^LH0,16^FS
^FO20,0^XGR:LOGO.GRF,1,1^FS
^IDR:LOGO.GRF
^FO20,58^A1,24,24^FD${field1}^FS
^FO20,85^A1,24,24^FD${field2}^FS
^FO20,112^A1,24,24^FD${field3}^FS
^FO20,139^A1,24,24^FD${field4}^FS
^FO20,166^A1,24,24^FD${field5}^FS
^FO20,193^A1,24,24^FD${field6}^FS
^FO20,220^A1,24,24^FD${field7}^FS
^FO20,247^A1,24,24^FD${field8}^FS
^FO20,274^A1,24,24^FD${field9}^FS
^FO320,50
^BQN,2,5
^FD${barcode}^FS
^XZ', 'ZPL', '<!doctype html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport"
    content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Document</title>
  <style>
    * {
      margin: 0;
      padding: 0;
    }

    .fields-item {
      margin-bottom: 0px;
    }

    #tag {
      width: 540px;
      height: 440px;
      box-sizing: border-box;
      background-color: #f8f8f8;
      border: 1px solid #e1e1e1;
      border-radius: 6px;
      overflow: hidden;
      display: flex;
      justify-content: flex-start;
      font-size: 30px;
      flex-direction: column;
    }
  </style>
</head>

<body>
  <div id="tag">
    <div style=" width: 100%; padding: 20px 20px 0; box-sizing: border-box">
      <div id="field1" class="fields-item"></div>
      <div id="field2" class="fields-item"></div>
      <div id="field3" class="fields-item"></div>
      <div id="field4" class="fields-item"></div>
    </div>
    <div style=" width: 540px; padding: 20px 20px; box-sizing: border-box;display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;">
      <img id="qrCode" width="500" height="130" src="" />
      <div id="qrCodeString" class="fields-item"></div>
    </div>
  </div>
</body>

</html>
', 10, '2024-06-12 16:09:06', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene (id, tag_scene_name, tag_scene_desc, tag_type_id, data_source_service_name, data_source_interface, qr_code_field, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002, '样品信息打印标签', '样品信息打印标签', 7, 'bmos-lims2-service', '/api/app/lims2/sample/printTag', 'sampleNo', 6110, '2025-09-22 10:35:08', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002001, 130020002, 'materialName', '检品名称', 'String', '氯化钠', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002002, 130020002, 'materialCode', '检品编码', 'String', '0001', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002003, 130020002, 'fullMaterialName', '检品信息', 'String', '0001-氯化钠', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002004, 130020002, 'orderNo', '检验单号', 'String', 'Inspect2025092201', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002005, 130020002, 'materialSpec', '检品规格', 'String', '500g/袋', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002006, 130020002, 'batchNo', '批号', 'String', '20250901', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002007, 130020002, 'sampleNo', '样品编号', 'String', 'Sample20250901', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002008, 130020002, 'inspectItemName', '检验项目名称', 'String', '理化检测', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002009, 130020002, 'inspectItemCode', '检验项目编码', 'String', '01', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002010, 130020002, 'inspectItemInfo', '检验项目信息', 'String', '01-理化检测', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002011, 130020002, 'planQuantityWithUnit', '计划取样量', 'String', '100g', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002012, 130020002, 'quantityWithUnit', '样品数量', 'String', '100g', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002013, 130020002, 'samplerName', '取样人', 'String', '张三', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002014, 130020002, 'samplingTime', '取样时间', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null, null, null, 0);


INSERT INTO bmos_platform.bp_tag_instance (id, tag_name, tag_type_id, tag_scene_id, tag_define_id, config_fields, is_enable, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002, '样品信息打印标签', 7, 130020002, 2, '[{"label":"","defineField":"field1","dataSourceField":"fullMaterialName","consumeValue":null},{"label":"","defineField":"field2","dataSourceField":"batchNo","consumeValue":null},{"label":"","defineField":"field3","dataSourceField":"inspectItemName","consumeValue":null},{"label":"","defineField":"field4","dataSourceField":"planQuantityWithUnit","consumeValue":null}]', 'TRUE', '2025-09-22 11:19:01', '2025-09-22 14:22:24', null, '888888888888888873', 0);


INSERT INTO bmos_platform.bp_tag_type (id, tag_type_name, tag_type_desc, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (7, '样品标签', '检验样品的标签', 7, '2025-09-22 10:17:24', null, null, null, 0);

