alter table lm_inspection_scheme_sampling modify sampling_amount varchar(50) null comment '取样量';

alter table lm_inspection_scheme_sampling modify sampling_unit varchar(20) null comment '取样单位';

alter table lm_inspection_scheme_sampling modify create_time datetime default (now()) null comment '创建时间';

