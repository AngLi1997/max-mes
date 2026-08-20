-- 数据修复
delete from bp_role_menu where is_deleted = 1;
-- 数据修复
delete t1
from bp_role_menu as t1 left join bp_role_menu as t2 on t1.role_id = t2.role_id and t1.menu_id = t2.menu_id where t1.id <> t2.id;
-- 表结构修改
alter table bp_role_menu
drop column id;

alter table bp_role_menu
drop column create_time;

alter table bp_role_menu
drop column update_time;

alter table bp_role_menu
drop column create_by;

alter table bp_role_menu
drop column update_by;

alter table bp_role_menu
drop column is_deleted;

alter table bp_role_menu
    add primary key (role_id, menu_id);