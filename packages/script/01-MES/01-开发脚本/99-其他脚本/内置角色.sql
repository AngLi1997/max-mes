# 内置系统管理员角色并授权 by lilong
REPLACE INTO bmos_platform.bp_role_type (id, role_type_name, parent_id, create_time, update_time, create_by, update_by, is_deleted) VALUES (100, '系统内置', 0, now(),now(), '1', '1', 0);
REPLACE INTO bmos_platform.bp_role (id, role_name, role_type_id, description, create_time, update_time, create_by, update_by, is_deleted) VALUES (1001, '系统管理员', 100, null, now(),now(), '1', '1', 0);
REPLACE INTO bmos_platform.bp_auth_role_menu (role_id, menu_id) select 1001, id from bmos_platform.bp_menu where 1=1;
REPLACE INTO bmos_platform.bp_role_menu (id, role_id, menu_id) select concat(id,1001), 1001, id from bmos_platform.bp_menu where 1=1;

SELECT 'bp_auth_role_menu' AS table_name, COUNT(*) AS count
FROM bp_auth_role_menu
WHERE role_id = 1001
UNION ALL
SELECT 'bp_role_menu' AS table_name, COUNT(*) AS count
FROM bp_role_menu
WHERE role_id = 1001;

delete from bmos_platform.bp_role_type where bmos_platform.bp_role_type.id = 100;
delete from bmos_platform.bp_role where  bmos_platform.bp_role.id = 1001;
delete from bmos_platform.bp_auth_role_menu where bmos_platform.bp_auth_role_menu.role_id = 1001;
delete from bmos_platform.bp_role_menu where bmos_platform.bp_role_menu.role_id =1001;



