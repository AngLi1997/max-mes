ALTER TABLE lm_stability_scheme_parameter
    ADD COLUMN is_executable TINYINT(1) DEFAULT 1 COMMENT '是否可执行（0-否，1-是）' AFTER is_reportable;
