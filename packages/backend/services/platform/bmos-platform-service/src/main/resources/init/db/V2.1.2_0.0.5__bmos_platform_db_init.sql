update bp_equipment_tag_property set required=0 where code='CONTAINER_WEIGHT_004' and tag_id=21;
update  bp_equipment_tag_property set is_deleted=1 where code='CONTENT_WEIGHT_006' and tag_id=21;
update  bp_equipment_tag_property set is_deleted=1 where code='CONTENT_VOLUME_005' and tag_id=21;
insert into bp_equipment_tag_property(id,code,name,embed,property_type,required,tag_id,create_by,update_by,create_time,update_time,is_deleted)
    value(44,'CONTAINER_WEIGHT_UNIT_013','皮重单位',1,2,0,21,1,1,now(),now(),0);

