# 清除联环项目非必要权限 by lilong 250716
DELETE FROM bmos_platform.bp_menu WHERE bmos_platform.bp_menu.id in (
'121020002000003',
'121020002000004',
'121020002000005',
'121020002000006',
'121020002000011',
'121020002000012',
'121020002000014',
'121020002000015',
'121020002000017',
'121020002000018',
'121020002000019'
);


# 清除没角色、菜单的关联数据
DELETE FROM bmos_platform.bp_auth_role_menu WHERE role_id NOT IN (SELECT id FROM bmos_platform.bp_role );
DELETE FROM bmos_platform.bp_auth_role_menu WHERE menu_id NOT IN (SELECT id FROM bmos_platform.bp_menu );
DELETE FROM bmos_platform.bp_role_menu WHERE role_id NOT IN (SELECT id FROM bmos_platform.bp_role );
DELETE FROM bmos_platform.bp_role_menu WHERE menu_id NOT IN (SELECT id FROM bmos_platform.bp_menu );

