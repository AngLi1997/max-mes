-- 物料件表 物料件号添加唯一索引
ALTER TABLE bm_storage_material ADD UNIQUE INDEX idx_no_unique (no);