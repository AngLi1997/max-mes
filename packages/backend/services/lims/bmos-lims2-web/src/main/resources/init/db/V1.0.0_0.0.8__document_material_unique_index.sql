-- 请验单模板-检品 关系唯一性与索引优化
-- 性能索引（如后续需要按检品或模板反查）
create index idx_document_material_cfg on lm_document_material (config_id);
create index idx_document_material_prod on lm_document_material (product_id);

