# 删除批签发功能菜单 by lilong 240306
DELETE FROM bmos_platform.bp_menu WHERE id IN (
'120040',
'120040001',
'120040002',
'120040003',
'120040005',
'120040006'   )and is_menu = 1;

