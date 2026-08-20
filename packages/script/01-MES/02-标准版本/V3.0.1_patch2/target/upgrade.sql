
UPDATE bmos_platform.bp_tag_define SET preview_html = '
<!doctype html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport"
    content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Document</title>
</head>

<body>
  <style>
    * {
      margin: 0;
      padding: 0;
    }

    .fields-item {
      margin-bottom: 0px;
    }

    #tag {
      width: 640px;
      height: 480px;
      box-sizing: border-box;
      background-color: #f8f8f8;
      border: 1px solid #e1e1e1;
      border-radius: 6px;
      overflow: hidden;
      display: flex;
      justify-content: center;
      font-size: 24px;
      font-family: "Times New Roman", serif;
      letter-spacing: -1px;
    }
  </style>
  <div id="tag">
    <div style="float: left; width: 440px; padding: 10px 10px; box-sizing: border-box">
      <div id="field1" class="fields-item"></div>
      <div id="field2" class="fields-item"></div>
      <div id="field3" class="fields-item"></div>
      <div id="field4" class="fields-item"></div>
      <div id="field5" class="fields-item"></div>
      <div id="field6" class="fields-item"></div>
      <div id="field7" class="fields-item"></div>
      <div id="field8" class="fields-item"></div>
      <div id="field9" class="fields-item"></div>
      <div id="field10" class="fields-item"></div>
      <div id="field11" class="fields-item"></div>
      <div id="field12" class="fields-item"></div>
      <div id="field13" class="fields-item"></div>
    </div>
    <div style="float: right; width: 200px; padding: 10px; box-sizing: border-box; position: relative; height: 460px;">
      <img id="qrCode" width="160" height="160" src=""
        style="position: absolute; top: 50%; left: 50%; margin-top: -80px; margin-left: -80px;" />
    </div>
  </div>
</body>

</html>
' WHERE id = 1;



UPDATE bmos_platform.bp_tag_define SET preview_html = '
<!doctype html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport"
    content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Document</title>
</head>

<body>
  <style>
    * {
      margin: 0;
      padding: 0;
    }

    .fields-item {
      margin-bottom: 0px;
    }

    #tag {
      width: 640px;
      height: 480px;
      box-sizing: border-box;
      background-color: #f8f8f8;
      border: 1px solid #e1e1e1;
      border-radius: 6px;
      overflow: hidden;
      display: flex;
      justify-content: center;
      font-size: 24px;
    }
  </style>
  <div id="tag">
    <div style="float: left; width: 440px; padding: 10px 10px; box-sizing: border-box">
      <div id="field1" class="fields-item"></div>
      <div id="field2" class="fields-item"></div>
      <div id="field3" class="fields-item"></div>
      <div id="field4" class="fields-item"></div>
      <div id="field5" class="fields-item"></div>
      <div id="field6" class="fields-item"></div>
      <div id="field7" class="fields-item"></div>
      <div id="field8" class="fields-item"></div>
      <div id="field9" class="fields-item"></div>
      <div id="field10" class="fields-item"></div>
      <div id="field11" class="fields-item"></div>
      <div id="field12" class="fields-item"></div>
      <div id="field13" class="fields-item"></div>
    </div>
    <div style="float: right; width: 200px; padding: 10px; box-sizing: border-box; position: relative; height: 460px;">
      <img id="qrCode" width="160" height="160" src=""
        style="position: absolute; top: 50%; left: 50%; margin-top: -80px; margin-left: -80px;" />
    </div>
  </div>
</body>

</html>
' WHERE id = 1;

UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"品种品名：","defineField":"field1","dataSourceField":"requirementProductName","consumeValue":null},{"label":"物料名称：","defineField":"field2","dataSourceField":"materialName","consumeValue":null},{"label":"需求用途：","defineField":"field3","dataSourceField":"requirementUsage","consumeValue":null},{"label":"筛目：","defineField":"field4","dataSourceField":"requirementSieveMesh","consumeValue":null},{"label":"批号：","defineField":"field5","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"序列号：","defineField":"field7","dataSourceField":"weighSerialNo","consumeValue":null},{"label":"皮重：","defineField":"field8","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field9","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field10","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field11","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field12","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field13","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121001014;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"品种品名：","defineField":"field1","dataSourceField":"requirementProductName","consumeValue":null},{"label":"物料名称：","defineField":"field2","dataSourceField":"materialName","consumeValue":null},{"label":"需求用途：","defineField":"field3","dataSourceField":"requirementUsage","consumeValue":null},{"label":"筛目：","defineField":"field4","dataSourceField":"requirementSieveMesh","consumeValue":null},{"label":"批号：","defineField":"field5","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"序列号：","defineField":"field7","dataSourceField":"weighSerialNo","consumeValue":null},{"label":"皮重：","defineField":"field8","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field9","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field10","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field11","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field12","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field13","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121001015;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"品种品名：","defineField":"field1","dataSourceField":"requirementProductName","consumeValue":null},{"label":"物料名称：","defineField":"field2","dataSourceField":"materialName","consumeValue":null},{"label":"需求用途：","defineField":"field3","dataSourceField":"requirementUsage","consumeValue":null},{"label":"筛目：","defineField":"field4","dataSourceField":"requirementSieveMesh","consumeValue":null},{"label":"批号：","defineField":"field5","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"序列号：","defineField":"field7","dataSourceField":"weighSerialNo","consumeValue":null},{"label":"皮重：","defineField":"field8","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field9","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field10","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field11","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field12","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field13","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121001016;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"物料名称：","defineField":"field1","dataSourceField":"materialName","consumeValue":null},{"label":"批号：","defineField":"field2","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field3","dataSourceField":"expiredDate","consumeValue":null},{"label":"物料件号：","defineField":"field4","dataSourceField":"materialNo","consumeValue":null},{"label":"皮重：","defineField":"field5","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field6","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field7","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field8","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field9","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field10","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121001017;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"物料名称：","defineField":"field1","dataSourceField":"materialName","consumeValue":null},{"label":"批号：","defineField":"field2","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field3","dataSourceField":"expiredDate","consumeValue":null},{"label":"物料件号：","defineField":"field4","dataSourceField":"materialNo","consumeValue":null},{"label":"皮重：","defineField":"field5","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field6","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field7","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field8","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field9","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field10","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121001018;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"物料名称：","defineField":"field1","dataSourceField":"materialName","consumeValue":null},{"label":"批号：","defineField":"field2","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field3","dataSourceField":"expiredDate","consumeValue":null},{"label":"物料件号：","defineField":"field4","dataSourceField":"materialNo","consumeValue":null},{"label":"皮重：","defineField":"field5","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field6","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field7","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field8","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field9","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field10","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121001019;

UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"品种品名：","defineField":"field1","dataSourceField":"requirementProductName","consumeValue":null},{"label":"物料名称：","defineField":"field2","dataSourceField":"materialName","consumeValue":null},{"label":"需求用途：","defineField":"field3","dataSourceField":"requirementUsage","consumeValue":null},{"label":"筛目：","defineField":"field4","dataSourceField":"requirementSieveMesh","consumeValue":null},{"label":"批号：","defineField":"field5","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"序列号：","defineField":"field7","dataSourceField":"weighSerialNo","consumeValue":null},{"label":"皮重：","defineField":"field8","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field9","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field10","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field11","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field12","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field13","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121002018;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"品种品名：","defineField":"field1","dataSourceField":"requirementProductName","consumeValue":null},{"label":"物料名称：","defineField":"field2","dataSourceField":"materialName","consumeValue":null},{"label":"需求用途：","defineField":"field3","dataSourceField":"requirementUsage","consumeValue":null},{"label":"筛目：","defineField":"field4","dataSourceField":"requirementSieveMesh","consumeValue":null},{"label":"批号：","defineField":"field5","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"序列号：","defineField":"field7","dataSourceField":"weighSerialNo","consumeValue":null},{"label":"皮重：","defineField":"field8","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field9","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field10","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field11","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field12","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field13","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121002019;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"品种品名：","defineField":"field1","dataSourceField":"requirementProductName","consumeValue":null},{"label":"物料名称：","defineField":"field2","dataSourceField":"materialName","consumeValue":null},{"label":"需求用途：","defineField":"field3","dataSourceField":"requirementUsage","consumeValue":null},{"label":"筛目：","defineField":"field4","dataSourceField":"requirementSieveMesh","consumeValue":null},{"label":"批号：","defineField":"field5","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"序列号：","defineField":"field7","dataSourceField":"weighSerialNo","consumeValue":null},{"label":"皮重：","defineField":"field8","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field9","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field10","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field11","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field12","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field13","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121002020;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"物料名称：","defineField":"field1","dataSourceField":"materialName","consumeValue":null},{"label":"批号：","defineField":"field2","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field3","dataSourceField":"expiredDate","consumeValue":null},{"label":"物料件号：","defineField":"field4","dataSourceField":"materialNo","consumeValue":null},{"label":"皮重：","defineField":"field5","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field6","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field7","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field8","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field9","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field10","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121002021;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"物料名称：","defineField":"field1","dataSourceField":"materialName","consumeValue":null},{"label":"批号：","defineField":"field2","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field3","dataSourceField":"expiredDate","consumeValue":null},{"label":"物料件号：","defineField":"field4","dataSourceField":"materialNo","consumeValue":null},{"label":"皮重：","defineField":"field5","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field6","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field7","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field8","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field9","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field10","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121002022;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"物料名称：","defineField":"field1","dataSourceField":"materialName","consumeValue":null},{"label":"批号：","defineField":"field2","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field3","dataSourceField":"expiredDate","consumeValue":null},{"label":"物料件号：","defineField":"field4","dataSourceField":"materialNo","consumeValue":null},{"label":"皮重：","defineField":"field5","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field6","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field7","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field8","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field9","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field10","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121002023;


INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001014024, 121001014, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001015024, 121001015, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001016024, 121001016, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001017022, 121001017, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001018022, 121001018, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001019022, 121001019, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002018024, 121002018, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002019024, 121002019, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002020024, 121002020, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002021022, 121002021, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002022022, 121002022, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002023022, 121002023, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
