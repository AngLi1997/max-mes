alter table bp_acquisition_point
    add acquisition_platform varchar(255) null comment '数采平台' after data_point_name;

update bp_acquisition_point set acquisition_platform = 'supCon' where acquisition_platform is null