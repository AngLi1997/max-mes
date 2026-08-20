alter table bp_equipment_info
    add acquisition_platform varchar(255) null after category_id;


update bp_equipment_info set acquisition_platform = 'supCon' where acquisition_platform is null