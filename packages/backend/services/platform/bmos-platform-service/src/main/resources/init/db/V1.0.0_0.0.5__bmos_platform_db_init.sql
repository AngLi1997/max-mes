alter table bp_dict_detail
    add `del_flag` bigint NOT NULL DEFAULT '0';

DROP INDEX `index_dict_id_value` ON bp_dict_detail;

CREATE UNIQUE INDEX index_dict_value_del ON bp_dict_detail (`dict_value`,`is_deleted`,`del_flag`,`dict_id`);