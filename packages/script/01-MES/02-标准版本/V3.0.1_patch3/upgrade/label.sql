
UPDATE bmos_platform.bp_tag_define SET preview_html = '
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
 <head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
  <meta http-equiv="X-UA-Compatible" content="ie=edge" />
  <title>Document</title>
 <style>
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }

    #tag {
        width: 640px;
        height: 800px;
        box-sizing: border-box;
        border: 1px solid #e1e1e1;
        border-radius: 6px;
        padding: 8px;
    }

    /* 文本区域 */
    .content-area {
        width: 100%;
        /* 限制最大高度为总高度减去二维码区域的高度 */
        height: 540px;  /* 800 - 240 - 20(间距) */
        padding: 0 8px;
        overflow: hidden; /* 防止内容溢出 */
    }

    .fields-item {
        font-size: 28px;
        line-height: 1.2;
        font-family: "SimSun", "宋体", serif;
        white-space: normal;
        word-break: break-all;
        margin-bottom: 2px;
    }

    #field1 {
        font-size: 28px;
    }

    /* 二维码区域 */
    .qr-area {
        width: 240px;        /* 30mm @ 203dpi */
        height: 240px;       /* 30mm @ 203dpi */
        margin: 10px auto 0; /* 增加上边距，确保与文字区域分开 */
    }

    #qrCode {
        width: 100%;
        height: 100%;
    }
</style>
 </head>
 <body>
  <div id="tag">
    <!-- 文本区域容器 -->
    <div class="text-container">
      <!-- 文本内容 -->
      <div class="content-area">
        <div id="field1" class="fields-item">
        </div>
        <div id="field2" class="fields-item">
        </div>
        <div id="field3" class="fields-item">
        </div>
        <div id="field4" class="fields-item">
        </div>
        <div id="field5" class="fields-item">
        </div>
        <div id="field6" class="fields-item">
        </div>
        <div id="field7" class="fields-item">
        </div>
        <div id="field8" class="fields-item">
        </div>
        <div id="field9" class="fields-item">
        </div>
        <div id="field10" class="fields-item">
        </div>
        <div id="field11" class="fields-item">
        </div>
        <div id="field12" class="fields-item">
        </div>
        <div id="field13" class="fields-item">
        </div>
      </div>
    </div>

    <!-- 预留空间 -->
    <div class="reserved-space"></div>

    <!-- 二维码区域 -->
    <div class="qr-area">
      <img id="qrCode" src="" />
    </div>
  </div>
 </body>
</html>
' WHERE id = 1;

UPDATE bmos_platform.bp_tag_define SET tag_height = 100 WHERE id = 1;

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001012022, 121001012, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001013022, 121001013, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002016022, 121002016, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002017022, 121002017, 'weighDate', '称量日期', 'String', '2024-02-02', now(), null, null, null, 0);


UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"品种品名：","defineField":"field1","dataSourceField":"extProductName","consumeValue":null},{"label":"物料名称：","defineField":"field2","dataSourceField":"materialName","consumeValue":null},{"label":"需求用途：","defineField":"field3","dataSourceField":"requirementUsage","consumeValue":null},{"label":"筛目：","defineField":"field4","dataSourceField":"requirementSieveMesh","consumeValue":null},{"label":"批号：","defineField":"field5","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"序列号：","defineField":"field7","dataSourceField":"weighSerialNo","consumeValue":null},{"label":"皮重：","defineField":"field8","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field9","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field10","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field11","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field12","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field13","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121001012;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"品种品名：","defineField":"field1","dataSourceField":"extProductName","consumeValue":null},{"label":"物料名称：","defineField":"field2","dataSourceField":"materialName","consumeValue":null},{"label":"需求用途：","defineField":"field3","dataSourceField":"requirementUsage","consumeValue":null},{"label":"筛目：","defineField":"field4","dataSourceField":"requirementSieveMesh","consumeValue":null},{"label":"批号：","defineField":"field5","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"序列号：","defineField":"field7","dataSourceField":"weighSerialNo","consumeValue":null},{"label":"皮重：","defineField":"field8","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field9","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field10","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field11","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field12","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field13","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121001013;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"品种品名：","defineField":"field1","dataSourceField":"extProductName","consumeValue":null},{"label":"物料名称：","defineField":"field2","dataSourceField":"materialName","consumeValue":null},{"label":"需求用途：","defineField":"field3","dataSourceField":"requirementUsage","consumeValue":null},{"label":"筛目：","defineField":"field4","dataSourceField":"requirementSieveMesh","consumeValue":null},{"label":"批号：","defineField":"field5","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"序列号：","defineField":"field7","dataSourceField":"weighSerialNo","consumeValue":null},{"label":"皮重：","defineField":"field8","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field9","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field10","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field11","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field12","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field13","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121002016;
UPDATE bmos_platform.bp_tag_instance SET config_fields = '[{"label":"品种品名：","defineField":"field1","dataSourceField":"extProductName","consumeValue":null},{"label":"物料名称：","defineField":"field2","dataSourceField":"materialName","consumeValue":null},{"label":"需求用途：","defineField":"field3","dataSourceField":"requirementUsage","consumeValue":null},{"label":"筛目：","defineField":"field4","dataSourceField":"requirementSieveMesh","consumeValue":null},{"label":"批号：","defineField":"field5","dataSourceField":"materialBatchNo","consumeValue":null},{"label":"有效期：","defineField":"field6","dataSourceField":"expiredDate","consumeValue":null},{"label":"序列号：","defineField":"field7","dataSourceField":"weighSerialNo","consumeValue":null},{"label":"皮重：","defineField":"field8","dataSourceField":"tareWeightWithUnit","consumeValue":null},{"label":"净重：","defineField":"field9","dataSourceField":"netWeightWithUnit","consumeValue":null},{"label":"毛重：","defineField":"field10","dataSourceField":"grossWeightWithUnit","consumeValue":null},{"label":"操作人：","defineField":"field11","dataSourceField":"weigherName","consumeValue":null},{"label":"复核人：","defineField":"field12","dataSourceField":"reCheckerName","consumeValue":null},{"label":"日期：","defineField":"field13","dataSourceField":"weighDate","consumeValue":null}]' WHERE id = 121002017;

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001012023, 121001012, 'requirementUsage', '需求用途', 'String', '配液使用', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121001013023, 121001013, 'requirementUsage', '需求用途', 'String', '配液使用', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002016023, 121002016, 'requirementUsage', '需求用途', 'String', '配液使用', now(), null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (121002017023, 121002017, 'requirementUsage', '需求用途', 'String', '配液使用', now(), null, null, null, 0);
