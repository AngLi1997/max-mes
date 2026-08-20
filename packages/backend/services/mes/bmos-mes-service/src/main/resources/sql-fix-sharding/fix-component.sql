-- 组件拆表后数据迁移
INSERT INTO `bm_batch_record_component_detail` SELECT
                                                   id,
                                                   component_detail,
                                                   formula_field,
                                                   formula_config,
                                                   create_time,
                                                   update_time,
                                                   create_by,
                                                   update_by,
                                                   is_deleted
FROM
    bm_batch_record_component
WHERE
        is_deleted = 0
  AND (!ISNULL( component_detail )
    OR !ISNULL( formula_field )
    OR !ISNULL( formula_config ));

    -- 记录项

    insert into bm_batch_record_parse select id,
    file_content,
    docx_header,
    docx_footer,
    create_time,
    update_time,
    create_by,
    update_by,
    is_deleted
     from bm_batch_record_item