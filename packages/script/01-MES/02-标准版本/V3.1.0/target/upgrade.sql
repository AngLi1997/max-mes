-- 使用数据库
USE bmos_platform;

-- 删除原LIMS菜单
DELETE FROM bmos_platform.bp_menu WHERE bmos_platform.bp_menu.id in (
'130010',
'130020',
'130030',
'130040'
);
DELETE FROM bmos_platform.bp_menu WHERE bmos_platform.bp_menu.parent_id in (
'130010',
'130020',
'130030',
'130040'
);
DELETE FROM bmos_platform.bp_menu WHERE bmos_platform.bp_menu.parent_id in (
'130010001',
'130010002',
'130010003',
'130010004',
'130010005',
'130010006',
'130010007',
'130010008',
'130010009',
'130020001',
'130020002',
'130020003',
'130020004',
'130020005',
'130020006',
'130020007',
'130020008',
'130020009',
'130020010',
'130030001',
'130030002',
'130030003',
'130030004',
'130040001',
'130040002',
'130040003',
'130040004'
);

-- 清除没角色、菜单的关联数据
DELETE FROM bmos_platform.bp_auth_role_menu WHERE role_id NOT IN (SELECT id FROM bmos_platform.bp_role );
DELETE FROM bmos_platform.bp_auth_role_menu WHERE menu_id NOT IN (SELECT id FROM bmos_platform.bp_menu );
DELETE FROM bmos_platform.bp_role_menu WHERE role_id NOT IN (SELECT id FROM bmos_platform.bp_role );
DELETE FROM bmos_platform.bp_role_menu WHERE menu_id NOT IN (SELECT id FROM bmos_platform.bp_menu );

-- 插入新菜单结构
-- 一级菜单
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010, '检验配置', '130010', 130, 0, 1, 0, null, 150110, now(), now(), '1', '1', 0, null, 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTgiIGhlaWdodD0iMTgiIHZpZXdCb3g9IjAgMCAxOCAxOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTEzLjY3NiAxLjM0QzE0LjE1NzcgMS4zNCAxNC41NDc2IDEuNzE1NzUgMTQuNTQ3NiAyLjE3ODY1QzE0LjU0NTQgMi4yOTA5NiAxNC41MjEyIDIuNDAxNzQgMTQuNDc2MiAyLjUwNDY3QzE0LjQzMTMgMi42MDc2MSAxNC4zNjY1IDIuNzAwNjcgMTQuMjg1NSAyLjc3ODU1QzE0LjIwNDYgMi44NTY0MyAxNC4xMDkxIDIuOTE3NiAxNC4wMDQ1IDIuOTU4NTdDMTMuODk5OSAyLjk5OTUzIDEzLjc4ODMgMy4wMTk0OSAxMy42NzYgMy4wMTczMUgxMy4wNzUyVjcuMDk4NjhMMTMuMjYzNyA3LjMzMTlDMTMuMTE2NyA3LjM3NDA3IDEyLjk3NTggNy40MzUwNCAxMi44NDQ0IDcuNTEzMjlMOS44NDU0OCA5LjMwMTMyQzkuMzExOSA5LjYxOTM1IDguOTc4NTYgMTAuMTk2NSA4Ljk3MDMxIDEwLjgxODRMOC45MjA4NCAxNC4zMDk3QzguOTEyNTkgMTQuOTQ2OSA5LjI0MzU4IDE1LjU0MDYgOS43OTcxOCAxNS44NTk4TDExLjE2OTQgMTYuNjUyNUgyLjk5NDlDMi40NzQyOCAxNi42NTI1IDEuOTcwMTQgMTYuNDgwNSAxLjU2NjEzIDE2LjE2MzdDMS4zMzY2OSAxNS45ODg2IDEuMTQ1MzIgMTUuNzY4NiAxLjAwMzY2IDE1LjUxNzFDMC44NjIwMDkgMTUuMjY1NiAwLjc3MzA0MyAxNC45ODc5IDAuNzQyMTg1IDE0LjcwMDlDMC43MTEzMjcgMTQuNDE0IDAuNzM5MjI1IDE0LjEyMzcgMC44MjQxNzggMTMuODQ3OUMwLjkwOTEzMSAxMy41NzIgMS4wNDkzNiAxMy4zMTY0IDEuMjM2MzIgMTMuMDk2NUw2LjMwODI5IDcuMDg4MDhWMy4wMTczMUg1LjQzMzEyQzUuMzIwODIgMy4wMTk0OSA1LjIwOTE4IDIuOTk5NTMgNS4xMDQ1OSAyLjk1ODU3QzUuMDAwMDEgMi45MTc2IDQuOTA0NTEgMi44NTY0MyA0LjgyMzU3IDIuNzc4NTVDNC43NDI2MyAyLjcwMDY3IDQuNjc3ODIgMi42MDc2MSA0LjYzMjg1IDIuNTA0NjdDNC41ODc4OCAyLjQwMTc0IDQuNTYzNjMgMi4yOTA5NiA0LjU2MTQ5IDIuMTc4NjVDNC41NjE0OSAxLjcxNTc1IDQuOTUxMzcgMS4zNCA1LjQzMzEyIDEuMzRIMTMuNjc4M0gxMy42NzZaTTE0LjAzNTIgOC41MTY4NUwxNi45ODU4IDEwLjIyMDFDMTcuMDc1MiAxMC4yNzIyIDE3LjE0OSAxMC4zNDczIDE3LjE5OTYgMTAuNDM3N0MxNy4yNTAxIDEwLjUyOCAxNy4yNzU1IDEwLjYzMDIgMTcuMjczMiAxMC43MzM2TDE3LjIyMzcgMTQuMjI0OUMxNy4yMjIxIDE0LjMzMDMgMTcuMTkzNSAxNC40MzM2IDE3LjE0MDcgMTQuNTI0OUMxNy4wODc5IDE0LjYxNjIgMTcuMDEyNyAxNC42OTI2IDE2LjkyMjIgMTQuNzQ2N0wxMy45MjMzIDE2LjUzNDdDMTMuODM0OCAxNi41ODg2IDEzLjczMzUgMTYuNjE3OCAxMy42MyAxNi42MTkyQzEzLjUyNjQgMTYuNjIwNyAxMy40MjQzIDE2LjU5NDMgMTMuMzM0NCAxNi41NDNMMTAuMzg0OSAxNC44Mzk3QzEwLjI5NTYgMTQuNzg3NiAxMC4yMjE3IDE0LjcxMjUgMTAuMTcxMiAxNC42MjIyQzEwLjEyMDcgMTQuNTMxOCAxMC4wOTUyIDE0LjQyOTYgMTAuMDk3NSAxNC4zMjYyTDEwLjE0NyAxMC44MzQ5QzEwLjE0OTQgMTAuNjIxNyAxMC4yNjQ4IDEwLjQyMjcgMTAuNDQ4NiAxMC4zMTMxTDEzLjQ0NzQgOC41MjUxQzEzLjUzNTkgOC40NzEyMSAxMy42MzcyIDguNDQyMDIgMTMuNzQwOCA4LjQ0MDU3QzEzLjg0NDQgOC40MzkxMiAxMy45NDY0IDguNDY1NDYgMTQuMDM2NCA4LjUxNjg1SDE0LjAzNTJaTTEzLjY4NTQgMTEuMDI0NkMxMi44ODA5IDExLjAyNDYgMTIuMjI4MyAxMS42OTgzIDEyLjIyODMgMTIuNTI5OUMxMi4yMjgzIDEzLjM2MTUgMTIuODgwOSAxNC4wMzUyIDEzLjY4NTQgMTQuMDM1MkMxNC40ODk5IDE0LjAzNTIgMTUuMTQyNCAxMy4zNjE1IDE1LjE0MjQgMTIuNTI5OUMxNS4xNDI0IDExLjY5ODMgMTQuNDg5OSAxMS4wMjQ2IDEzLjY4NTQgMTEuMDI0NloiIGZpbGw9IndoaXRlIi8+Cjwvc3ZnPgo=');

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130020, '检验管理', '130020', 130, 0, 1, 0, null, 150120, now(), now(), '1', '1', 0, null, 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTgiIGhlaWdodD0iMTgiIHZpZXdCb3g9IjAgMCAxOCAxOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGcgY2xpcC1wYXRoPSJ1cmwoI2NsaXAwXzUwMF8yNDgxNikiPgo8cmVjdCB4PSIwLjUiIHk9IjE1LjciIHdpZHRoPSIxNyIgaGVpZ2h0PSIxLjgiIHJ4PSIwLjkiIGZpbGw9IndoaXRlIi8+CjxwYXRoIGQ9Ik0xMS42NjEzIDAuNUMxMS45OTM5IDAuNTAwMDE0IDEyLjMxMzMgMC42MzE1MTYgMTIuNTQ5IDAuODY2MjExTDE1LjQ2ODkgMy43ODcxMUMxNS43MDM5IDQuMDIyNzggMTUuODM2IDQuMzQyMDEgMTUuODM2MSA0LjY3NDhDMTUuODM2MSA1LjAwNzc2IDE1LjcwNCA1LjMyNzcgMTUuNDY4OSA1LjU2MzQ4TDE0LjA5MTkgNi45NDA0M0MxNC4yMjU2IDcuMjI1NTYgMTQuMzAwOSA3LjU0Mzc2IDE0LjMwMDkgNy44Nzg5MUMxNC4zMDA4IDkuMTAwNzcgMTMuMzA1IDEwLjA5NTcgMTIuMDgzMSAxMC4wOTU3QzExLjc1NjUgMTAuMDk1MyAxMS40MzcxIDEwLjAyMzIgMTEuMTQ2NiA5Ljg4NzdMOS40OTUyNSAxMS41MzYxQzkuMzc4OTUgMTEuNjUyOSA5LjI0MDM0IDExLjc0NTYgOS4wODgwMyAxMS44MDg2QzguOTM1NjkgMTEuODcxNiA4Ljc3MjM5IDExLjkwMzkgOC42MDc1NiAxMS45MDMzVjExLjkwNDNDOC4yNzIwNSAxMS45MDQzIDcuOTU3MDYgMTEuNzc1MiA3LjcxOTg2IDExLjUzODFMNC43OTg5NyA4LjYxNzE5QzQuNTYzNjggOC4zODE1NyA0LjQzMTc4IDguMDYxNSA0LjQzMTc4IDcuNzI4NTJDNC40MzE5IDcuMzk1NzEgNC41NjM4MSA3LjA3NjMzIDQuNzk4OTcgNi44NDA4MkwxMC43NzM2IDAuODY2MjExQzExLjAwOTMgMC42MzE1MzcgMTEuMzI4NiAwLjUgMTEuNjYxMyAwLjVaTTEyLjIwMDMgNi41OTY2OEMxMS41Mzc2IDYuNTk2NjggMTEuMDAwMSA3LjEzNDEzIDExLjAwMDEgNy43OTY4OEMxMS4wMDAzIDguNDU5NDYgMTEuNTM3NyA4Ljk5NjA5IDEyLjIwMDMgOC45OTYwOUMxMi44NjI4IDguOTk1OTEgMTMuMzk5NCA4LjQ1OTM1IDEzLjM5OTYgNy43OTY4OEMxMy4zOTk2IDcuMTM0MjQgMTIuODYyOSA2LjU5Njg2IDEyLjIwMDMgNi41OTY2OFoiIGZpbGw9IndoaXRlIi8+CjxwYXRoIGQ9Ik0yLjY4MTE3IDguOTQzMzVDMi44ODE5IDguNzUzNzIgMy4xOTgzNiA4Ljc2MjczIDMuMzg3OTkgOC45NjM0Nkw3LjE2NDkxIDEyLjk2MTZDNy4zNTQ1NCAxMy4xNjIzIDcuMzQ1NTMgMTMuNDc4OCA3LjE0NDggMTMuNjY4NEw2LjEyNzEgMTQuNjI5OEM1LjkyNjM2IDE0LjgxOTQgNS42MDk5MSAxNC44MTA0IDUuNDIwMjggMTQuNjA5N0wxLjY0MzM2IDEwLjYxMTZDMS40NTM3MyAxMC40MTA4IDEuNDYyNzMgMTAuMDk0NCAxLjY2MzQ3IDkuOTA0NzVMMi42ODExNyA4Ljk0MzM1WiIgZmlsbD0id2hpdGUiLz4KPHBhdGggZD0iTTEzLjYwMzcgOC43MjE5N0MxMy42NDU0IDguNzUyMTcgMTMuNjg2MiA4Ljc4NDc3IDEzLjcyMzkgOC44MjA2MUwxMy43MzA3IDguODI2NDdMMTMuNzM2NiA4LjgzMjMyQzE0LjgyNjEgOS45Mzk0NyAxNS40MTc2IDExLjI0NzUgMTUuMzY4NCAxMi42MTQ2QzE1LjMyMjkgMTMuODc2OCAxNC43NTkxIDE1LjA3MTIgMTMuNzYzOSAxNi4xODFIMTAuOTk1M0MxMC45OTE2IDE2LjA3MjcgMTEuMDAxOSAxNS45NjM5IDExLjAyODUgMTUuODU3N0MxMS4wODYyIDE1LjYyODYgMTEuMjEyIDE1LjQyMjQgMTEuMzg5OSAxNS4yNjY5QzEyLjUwNDkgMTQuMjg2MyAxMy4wMTg2IDEzLjMzNCAxMy4wNDYxIDEyLjUzMTVWMTIuNTI5NkMxMy4wNzAyIDExLjg2OTkgMTIuNzgyNiAxMS4xNzUzIDEyLjA4MTMgMTAuNDYxMkwxMi4wNzU0IDEwLjQ1NTRMMTIuMDY5NiAxMC40NDg1QzExLjg2NTQgMTAuMjI3IDExLjc1NTcgOS45MzQyNyAxMS43NjI5IDkuNjMzMTFDMTEuNzY1NCA5LjUzMzQ2IDExLjc4MDggOS40MzUzNiAxMS44MDc4IDkuMzQxMTFDMTEuOTg1NCA5LjQyNzAyIDEyLjE4NDMgOS40NzY4NiAxMi4zOTQ4IDkuNDc2ODZDMTIuOTI2MSA5LjQ3NjY5IDEzLjM4MzYgOS4xNjgzOCAxMy42MDM3IDguNzIxOTdaIiBmaWxsPSJ3aGl0ZSIvPgo8L2c+CjxkZWZzPgo8Y2xpcFBhdGggaWQ9ImNsaXAwXzUwMF8yNDgxNiI+CjxyZWN0IHdpZHRoPSIxOCIgaGVpZ2h0PSIxOCIgZmlsbD0id2hpdGUiLz4KPC9jbGlwUGF0aD4KPC9kZWZzPgo8L3N2Zz4K');

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130030, '报告管理', '130030', 130, 0, 1, 0, null, 150130, now(), now(), '1', '1', 0, null, 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTgiIGhlaWdodD0iMTgiIHZpZXdCb3g9IjAgMCAxOCAxOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTQuMDQ5OCAyQzQuMDE3MDIgMi4xNjE1NCA0IDIuMzI4NzggNCAyLjVDNCAzLjg4MDcxIDUuMTE5MjkgNSA2LjUgNUgxMS41QzEyLjg4MDcgNSAxNCAzLjg4MDcxIDE0IDIuNUMxNCAyLjMyODc4IDEzLjk4MyAyLjE2MTU0IDEzLjk1MDIgMkgxNi4wNTI3QzE2LjU3NTkgMi4wMDAwNiAxNi45OTk5IDIuNDI0MTMgMTcgMi45NDcyN1YxNy4wNTI3QzE2Ljk5OTkgMTcuNTc1OSAxNi41NzU5IDE3Ljk5OTkgMTYuMDUyNyAxOEgxLjk0NzI3QzEuNDI0MTMgMTcuOTk5OSAxLjAwMDA2IDE3LjU3NTkgMSAxNy4wNTI3VjIuOTQ3MjdDMS4wMDAwNiAyLjQyNDEzIDEuNDI0MTMgMi4wMDAwNiAxLjk0NzI3IDJINC4wNDk4Wk0xMC4yNDYxIDYuODE4MzZDMTAuMDE3NiA2Ljc1NTI3IDkuNzc1NzIgNi44NjIwMyA5LjY2Nzk3IDcuMDczMjRDOS41NDMyNSA3LjMxNjY3IDkuMjkwMTMgNy40ODI0MiA5IDcuNDgyNDJDOC43MDk5NCA3LjQ4MjE4IDguNDU2NTYgNy4zMTY3OCA4LjMzMjAzIDcuMDczMjRDOC4yMzc3NiA2Ljg4ODQ2IDguMDQxNTcgNi43ODM3NSA3Ljg0MDgyIDYuODAyNzNMNy43NTM5MSA2LjgxODM2QzcuMDIzNzEgNy4wMjA2NCA2LjM2NTQ4IDcuMzk1OTYgNS44MjcxNSA3Ljg5NjQ4QzUuNjY2MDUgOC4wNDY3NCA1LjYyMjI5IDguMjg1NzYgNS43MTk3MyA4LjQ4MzRDNS43NjgzNSA4LjU4MjI3IDUuNzk1ODMgOC42OTQyNSA1Ljc5NTkgOC44MTM0OEM1Ljc5NTQ5IDkuMjI2ODIgNS40NjAzMiA5LjU2MzQ4IDUuMDQ2ODggOS41NjM0OEM1LjA0MzQ4IDkuNTYzNDIgNS4wMzk0NCA5LjU2MjU1IDUuMDM2MTMgOS41NjI1QzQuODQzMzIgOS41NjAwMyA0LjY2ODg4IDkuNjY4OTQgNC41ODM5OCA5LjgzNjkxTDQuNTUyNzMgOS45MTMwOUM0LjQxNDE4IDEwLjM1NDYgNC4zMzk4NyAxMC44MjQ4IDQuMzM5ODQgMTEuMzEwNUM0LjMzOTg5IDExLjYzMTIgNC4zNzI1NSAxMS45NDU3IDQuNDM0NTcgMTIuMjQ5QzQuNDg2MDcgMTIuNDk5MSA0LjcxODUgMTIuNjcxMyA0Ljk3MjY2IDEyLjY0NjVDNC45OTY5NiAxMi42NDQxIDUuMDIxOTEgMTIuNjQyNiA1LjA0Njg4IDEyLjY0MjZDNS40NjA1NyAxMi42NDI2IDUuNzk1OSAxMi45Nzc5IDUuNzk1OSAxMy4zOTE2QzUuNzk1NyAxMy41NzU2IDUuNzMwMDQgMTMuNzQzNyA1LjYyMDEyIDEzLjg3NEM1LjQ1NTc4IDE0LjA2OTQgNS40NjQ2OCAxNC4zNTc5IDUuNjQxNiAxNC41NDJDNi4xOTEzNSAxNS4xMTMyIDYuODg2NTQgMTUuNTQ1NiA3LjY2Nzk3IDE1Ljc3ODNDNy45MjgxNyAxNS44NTU4IDguMjAyODkgMTUuNzEwNCA4LjI4NjEzIDE1LjQ1MjFDOC4zODI3OCAxNS4xNDk4IDguNjY2OTcgMTQuOTMxOSA5IDE0LjkzMTZDOS4zMzMzMSAxNC45MzE2IDkuNjE3MTYgMTUuMTQ5NiA5LjcxMzg3IDE1LjQ1MjFDOS43OTY5IDE1LjcxMDQgMTAuMDcxOSAxNS44NTU0IDEwLjMzMiAxNS43NzgzQzExLjExMzUgMTUuNTQ1NiAxMS44MDk2IDE1LjExMzEgMTIuMzU5NCAxNC41NDJDMTIuNTM2MiAxNC4zNTc4IDEyLjU0NTQgMTQuMDY5MyAxMi4zODA5IDEzLjg3NEMxMi4yNzA5IDEzLjc0MzYgMTIuMjA1MyAxMy41NzU2IDEyLjIwNTEgMTMuMzkxNkMxMi4yMDUxIDEyLjk3OCAxMi41NDA2IDEyLjY0MjggMTIuOTU0MSAxMi42NDI2QzEyLjk3OTEgMTIuNjQyNiAxMy4wMDQgMTIuNjQ0MSAxMy4wMjgzIDEyLjY0NjVDMTMuMjgyNiAxMi42NzE1IDEzLjUxNDggMTIuNDk5MiAxMy41NjY0IDEyLjI0OUMxMy42Mjg0IDExLjk0NTggMTMuNjYxMSAxMS42MzEyIDEzLjY2MTEgMTEuMzEwNUMxMy42NjExIDEwLjgyNDYgMTMuNTg1OSAxMC4zNTQ3IDEzLjQ0NzMgOS45MTMwOUMxMy4zODExIDkuNzAyNDUgMTMuMTg0NiA5LjU1OTM1IDEyLjk2MzkgOS41NjI1QzEyLjk2MDggOS41NjI2IDEyLjk1NzMgOS41NjM0NyAxMi45NTQxIDkuNTYzNDhDMTIuNTQwOCA5LjU2MzI5IDEyLjIwNTUgOS4yMjY3MSAxMi4yMDUxIDguODEzNDhDMTIuMjA1MSA4LjY5NDMgMTIuMjMyNyA4LjU4MjI0IDEyLjI4MTIgOC40ODM0QzEyLjM3ODcgOC4yODU2NCAxMi4zMzUyIDguMDQ2NzIgMTIuMTczOCA3Ljg5NjQ4QzExLjYzNTMgNy4zOTU3MSAxMC45NzY3IDcuMDIwNiAxMC4yNDYxIDYuODE4MzZaTTEwLjMwNzYgNy44OTA2MkMxMC42NDkyIDguMDIxMjcgMTAuOTY2MSA4LjIwMTMgMTEuMjUgOC40MjI4NUMxMS4yMjEzIDguNTQ4NDMgMTEuMjA2MSA4LjY3OTczIDExLjIwNjEgOC44MTM0OEMxMS4yMDY0IDkuNjQ3OTkgMTEuNzkxOCAxMC4zNDYxIDEyLjU3NDIgMTAuNTE5NUMxMi42MzAyIDEwLjc3MzkgMTIuNjYyMSAxMS4wMzg2IDEyLjY2MjEgMTEuMzEwNUMxMi42NjIxIDExLjQzMjYgMTIuNjU1MiAxMS41NTQxIDEyLjY0MzYgMTEuNjcyOUMxMS44MjY2IDExLjgxOTUgMTEuMjA2MSAxMi41MzI0IDExLjIwNjEgMTMuMzkxNkMxMS4yMDYyIDEzLjY0NjYgMTEuMjYxOCAxMy44ODk3IDExLjM2MDQgMTQuMTA4NEMxMS4wODI5IDE0LjM0MjYgMTAuNzcwMSAxNC41MzU3IDEwLjQzMTYgMTQuNjc5N0MxMC4xMTU4IDE0LjIyODYgOS41OTMxOSAxMy45MzI2IDkgMTMuOTMyNkM4LjQwNjc0IDEzLjkzMjggNy44ODMwNSAxNC4yMjg0IDcuNTY3MzggMTQuNjc5N0M3LjIyOTE5IDE0LjUzNTYgNi45MTY5MSAxNC4zNDI2IDYuNjM5NjUgMTQuMTA4NEM2LjczODI1IDEzLjg4OTYgNi43OTQ4IDEzLjY0NjcgNi43OTQ5MiAxMy4zOTE2QzYuNzk0OTIgMTIuNTMxNyA2LjE3MzQ0IDExLjgxODcgNS4zNTU0NyAxMS42NzI5QzUuMzQzODQgMTEuNTU0MSA1LjMzNzkxIDExLjQzMjYgNS4zMzc4OSAxMS4zMTA1QzUuMzM3OTEgMTEuMDM4OCA1LjM2ODg5IDEwLjc3MzcgNS40MjQ4IDEwLjUxOTVDNi4yMDgwOCAxMC4zNDY4IDYuNzk0NTcgOS42NDg1OSA2Ljc5NDkyIDguODEzNDhDNi43OTQ4OSA4LjY3OTYzIDYuNzc4NzMgOC41NDg1MSA2Ljc1IDguNDIyODVDNy4wMzM0OSA4LjIwMTU5IDcuMzUwNDQgOC4wMjEyNyA3LjY5MTQxIDcuODkwNjJDOC4wMTExMyA4LjI1MTc5IDguNDc4NzMgOC40ODEyNiA5IDguNDgxNDVDOS41MjA5OSA4LjQ4MTQ1IDkuOTg3NzggOC4yNTEyOSAxMC4zMDc2IDcuODkwNjJaTTguOTk5MDIgOS41MDM5MUM4LjAwNTI5IDkuNTA0MDkgNy4xOTk1NyAxMC4zMSA3LjE5OTIyIDExLjMwMzdDNy4xOTk0IDEyLjI5NzYgOC4wMDUxOCAxMy4xMDMzIDguOTk5MDIgMTMuMTAzNUM5Ljk5Mjk2IDEzLjEwMzQgMTAuNzk4NyAxMi4yOTc2IDEwLjc5ODggMTEuMzAzN0MxMC43OTg1IDEwLjMwOTkgOS45OTI4NSA5LjUwMzk5IDguOTk5MDIgOS41MDM5MVpNOC45OTkwMiAxMC41MDM5QzkuNDQwNTcgMTAuNTA0IDkuNzk4NDggMTAuODYyMiA5Ljc5ODgzIDExLjMwMzdDOS43OTg2NSAxMS43NDUzIDkuNDQwNjcgMTIuMTAzNCA4Ljk5OTAyIDEyLjEwMzVDOC41NTc0NiAxMi4xMDMzIDguMTk5NCAxMS43NDUzIDguMTk5MjIgMTEuMzAzN0M4LjE5OTU3IDEwLjg2MjMgOC41NTc1NyAxMC41MDQxIDguOTk5MDIgMTAuNTAzOVpNMTEgMEMxMi4xMDQ2IDAgMTMgMC44OTU0MzEgMTMgMkMxMyAzLjEwNDU3IDEyLjEwNDYgNCAxMSA0SDdDNS44OTU0MyA0IDUgMy4xMDQ1NyA1IDJDNSAwLjg5NTQzMSA1Ljg5NTQzIDAgNyAwSDExWiIgZmlsbD0id2hpdGUiLz4KPC9zdmc+Cg==');

INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130040, '检验查询', '130040', 130, 0, 1, 0, null, 150140, now(), now(), '1', '1', 0, null, 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTgiIGhlaWdodD0iMTgiIHZpZXdCb3g9IjAgMCAxOCAxOCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTE0IDBDMTQuNTUyMyAxLjkzMjc2ZS0wNyAxNSAwLjQ0NzcxNSAxNSAxVjguMzkwNjJDMTQuMTM3MSA3LjgyNzg3IDEzLjEwNyA3LjUgMTIgNy41QzguOTYyNDUgNy41IDYuNSA5Ljk2MjQ1IDYuNSAxM0M2LjUgMTUuMjE5NyA3LjgxNTU2IDE3LjEzMSA5LjcwODk4IDE4SDJDMS40NDc3MiAxOCAxIDE3LjU1MjMgMSAxN1YxQzEgMC40NDc3MTUgMS40NDc3MiAyLjQxNTk2ZS0wOCAyIDBIMTRaTTEyIDlDMTQuMjA5MSA5IDE2IDEwLjc5MDkgMTYgMTNDMTYgMTMuNzQyMiAxNS43OTY2IDE0LjQzNjUgMTUuNDQ0MyAxNS4wMzIyTDE3LjAwMzkgMTYuNDQ0M0MxNy4zMTA1IDE2LjcyMjQgMTcuMzMzNCAxNy4xOTYgMTcuMDU1NyAxNy41MDI5QzE2Ljc3NzcgMTcuODA5OSAxNi4zMDQxIDE3LjgzMzYgMTUuOTk3MSAxNy41NTU3TDE0LjQ1MjEgMTYuMTU4MkMxMy43NzQ3IDE2LjY4NDkgMTIuOTI0NSAxNyAxMiAxN0M5Ljc5MDg3IDE3IDggMTUuMjA5MSA4IDEzQzggMTAuNzkwOSA5Ljc5MDg3IDkgMTIgOVpNMy43OTk4IDVDMy4zNTgxMyA1LjAwMDExIDMuMDAwMTEgNS4zNTgxMyAzIDUuNzk5OEMzIDYuMjQxNTcgMy4zNTgwNyA2LjU5OTUgMy43OTk4IDYuNTk5NjFINy4yMDAyQzcuNjQxOTMgNi41OTk1IDggNi4yNDE1NyA4IDUuNzk5OEM3Ljk5OTg5IDUuMzU4MTMgNy42NDE4NyA1LjAwMDExIDcuMjAwMiA1SDMuNzk5OFpNMy43OTk4IDJDMy4zNTgxMyAyLjAwMDExIDMuMDAwMTEgMi4zNTgxMyAzIDIuNzk5OEMzIDMuMjQxNTcgMy4zNTgwNyAzLjU5OTUgMy43OTk4IDMuNTk5NjFIMTAuMjAwMkMxMC42NDE5IDMuNTk5NSAxMSAzLjI0MTU3IDExIDIuNzk5OEMxMC45OTk5IDIuMzU4MTMgMTAuNjQxOSAyLjAwMDExIDEwLjIwMDIgMkgzLjc5OThaIiBmaWxsPSJ3aGl0ZSIvPgo8L3N2Zz4K');

-- 检验配置子菜单
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010001, '检品管理', '130010001', 130010, 0, 1, 0, null, 150110110, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010002, '分析项管理', '130010002', 130010, 0, 1, 0, null, 150110120, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010003, '检验项目管理', '130010003', 130010, 0, 1, 0, null, 150110130, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010004, '实验包管理', '130010004', 130010, 0, 1, 0, null, 150110140, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010005, '请验单配置', '130010005', 130010, 0, 1, 0, null, 150110150, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010006, '班组管理', '130010006', 130010, 0, 1, 0, null, 150110160, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010007, '流程配置', '130010007', 130010, 0, 1, 0, null, 150110170, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010008, '检验方案', '130010008', 130010, 0, 1, 0, null, 150110180, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130010009, '检验方案审批', '130010009', 130010, 0, 1, 0, null, 150110190, now(), now(), '1', '1', 0, null, null);

-- 检验管理子菜单
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130020001, '请验确认', '130020001', 130020, 0, 1, 0, null, 150120110, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130020002, '取样登记', '130020002', 130020, 0, 1, 0, null, 150120120, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130020003, '样品接收', '130020003', 130020, 0, 1, 0, null, 150120130, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130020004, '分样', '130020004', 130020, 0, 1, 0, null, 150120140, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130020005, '领样', '130020005', 130020, 0, 1, 0, null, 150120150, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130020006, '任务分配', '130020006', 130020, 0, 1, 0, null, 150120160, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130020007, '检验录入', '130020007', 130020, 0, 1, 0, null, 150120170, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130020008, '检验复核', '130020008', 130020, 0, 1, 0, null, 150120180, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130020009, '检验审核', '130020009', 130020, 0, 1, 0, null, 150120190, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130020010, '样品处理', '130020010', 130020, 0, 1, 0, null, 150120200, now(), now(), '1', '1', 0, null, null);

-- 报告管理子菜单
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130030001, '报告数据点配置', '130030001', 130030, 0, 1, 0, null, 150130110, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130030002, '报告配置', '130030002', 130030, 0, 1, 0, null, 150130120, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130030003, '报告生成', '130030003', 130030, 0, 1, 0, null, 150130130, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130030004, '报告审批', '130030004', 130030, 0, 1, 0, null, 150130140, now(), now(), '1', '1', 0, null, null);

-- 检验查询子菜单
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130040001, '检验详情', '130040001', 130040, 0, 1, 0, null, 150140110, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130040002, '检项查询', '130040002', 130040, 0, 1, 0, null, 150140120, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130040003, '趋势查询', '130040003', 130040, 0, 1, 0, null, 150140130, now(), now(), '1', '1', 0, null, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias, icon) VALUES (130040004, '样品台账', '130040004', 130040, 0, 1, 0, null, 150140140, now(), now(), '1', '1', 0, null, null);

-- 按钮权限配置
-- 检验配置子菜单按钮权限
-- 检品管理按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010001000001, '新增检品分类', '130010001000001', 130010001, 0, 0, 0, null, 130010001110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010001000002, '删除检品分类', '130010001000002', 130010001, 0, 0, 0, null, 130010001120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010001000003, '新增', '130010001000003', 130010001, 0, 0, 0, null, 130010001130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010001000004, '同步', '130010001000004', 130010001, 0, 0, 0, null, 130010001140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010001000005, '编辑', '130010001000005', 130010001, 0, 0, 0, null, 130010001150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010001000006, '删除', '130010001000006', 130010001, 0, 0, 0, null, 130010001160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010001000007, '启停', '130010001000007', 130010001, 0, 0, 0, null, 130010001170, now(), now(), '1', '1', 0, null);

-- 分析项管理按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010002000001, '新增', '130010002000001', 130010002, 0, 0, 0, null, 130010002110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010002000002, '编辑', '130010002000002', 130010002, 0, 0, 0, null, 130010002120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010002000003, '删除', '130010002000003', 130010002, 0, 0, 0, null, 130010002130, now(), now(), '1', '1', 0, null);

-- 检验项目管理按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010003000001, '新增', '130010003000001', 130010003, 0, 0, 0, null, 130010003110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010003000002, '编辑', '130010003000002', 130010003, 0, 0, 0, null, 130010003120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010003000003, '删除', '130010003000003', 130010003, 0, 0, 0, null, 130010003130, now(), now(), '1', '1', 0, null);

-- 实验包管理按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010004000001, '新增', '130010004000001', 130010004, 0, 0, 0, null, 130010004110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010004000002, '编辑', '130010004000002', 130010004, 0, 0, 0, null, 130010004120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010004000003, '删除', '130010004000003', 130010004, 0, 0, 0, null, 130010004130, now(), now(), '1', '1', 0, null);

-- 请验单配置按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010005000001, '新增', '130010005000001', 130010005, 0, 0, 0, null, 130010005110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010005000002, '编辑', '130010005000002', 130010005, 0, 0, 0, null, 130010005120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010005000003, '删除', '130010005000003', 130010005, 0, 0, 0, null, 130010005130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010005000004, '启停', '130010005000004', 130010005, 0, 0, 0, null, 130010005140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010005000005, '绑定检品', '130010005000005', 130010005, 0, 0, 0, null, 130010005150, now(), now(), '1', '1', 0, null);

-- 班组管理按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010006000001, '新增', '130010006000001', 130010006, 0, 0, 0, null, 130010006110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010006000002, '编辑', '130010006000002', 130010006, 0, 0, 0, null, 130010006120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010006000003, '人员分配', '130010006000003', 130010006, 0, 0, 0, null, 130010006130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010006000004, '数据权限', '130010006000004', 130010006, 0, 0, 0, null, 130010006140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010006000005, '启停', '130010006000005', 130010006, 0, 0, 0, null, 130010006150, now(), now(), '1', '1', 0, null);

-- 流程配置按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010007000001, '新增流程', '130010007000001', 130010007, 0, 0, 0, null, 130010007110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010007000002, '新增版本', '130010007000002', 130010007, 0, 0, 0, null, 130010007120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010007000003, '编辑', '130010007000003', 130010007, 0, 0, 0, null, 130010007130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010007000004, '启用', '130010007000004', 130010007, 0, 0, 0, null, 130010007140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010007000005, '停用', '130010007000005', 130010007, 0, 0, 0, null, 130010007150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010007000006, '历史', '130010007000006', 130010007, 0, 0, 0, null, 130010007160, now(), now(), '1', '1', 0, null);

-- 检验方案按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010008000001, '新增方案', '130010008000001', 130010008, 0, 0, 0, null, 130010008110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010008000002, '新增版本', '130010008000002', 130010008, 0, 0, 0, null, 130010008120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010008000003, '复制方案', '130010008000003', 130010008, 0, 0, 0, null, 130010008130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010008000004, '编辑', '130010008000004', 130010008, 0, 0, 0, null, 130010008140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010008000005, '启用', '130010008000005', 130010008, 0, 0, 0, null, 130010008150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010008000006, '停用', '130010008000006', 130010008, 0, 0, 0, null, 130010008160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010008000007, '审批进度', '130010008000007', 130010008, 0, 0, 0, null, 130010008170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010008000008, '历史', '130010008000008', 130010008, 0, 0, 0, null, 130010008180, now(), now(), '1', '1', 0, null);

-- 检验方案审批按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010009000001, '处理', '130010009000001', 130010009, 0, 0, 0, null, 130010009110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130010009000002, '审批进度', '130010009000002', 130010009, 0, 0, 0, null, 130010009120, now(), now(), '1', '1', 0, null);

-- 检验管理子菜单按钮权限
-- 请验确认按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020001000001, '发起请验', '130020001000001', 130020001, 0, 0, 0, null, 130020001110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020001000002, '批量确认', '130020001000002', 130020001, 0, 0, 0, null, 130020001120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020001000003, '确认', '130020001000003', 130020001, 0, 0, 0, null, 130020001130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020001000004, '编辑', '130020001000004', 130020001, 0, 0, 0, null, 130020001140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020001000005, '打印', '130020001000005', 130020001, 0, 0, 0, null, 130020001150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020001000006, '终止', '130020001000006', 130020001, 0, 0, 0, null, 130020001160, now(), now(), '1', '1', 0, null);

-- 取样登记按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020002000001, '取样', '130020002000001', 130020002, 0, 0, 0, null, 130020002110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020002000002, '终止', '130020002000002', 130020002, 0, 0, 0, null, 130020002120, now(), now(), '1', '1', 0, null);

-- 样品接收按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020003000001, '接收', '130020003000001', 130020003, 0, 0, 0, null, 130020003110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020003000002, '终止', '130020003000002', 130020003, 0, 0, 0, null, 130020003120, now(), now(), '1', '1', 0, null);

-- 分样按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020004000001, '分样', '130020004000001', 130020004, 0, 0, 0, null, 130020004110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020004000002, '终止', '130020004000002', 130020004, 0, 0, 0, null, 130020004120, now(), now(), '1', '1', 0, null);

-- 领样按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020005000001, '领取', '130020005000001', 130020005, 0, 0, 0, null, 130020005110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020005000002, '批量领取', '130020005000002', 130020005, 0, 0, 0, null, 130020005120, now(), now(), '1', '1', 0, null);

-- 任务分配按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020006000001, '批量分配', '130020006000001', 130020006, 0, 0, 0, null, 130020006110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020006000002, '批量领取', '130020006000002', 130020006, 0, 0, 0, null, 130020006120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020006000003, '批量退回', '130020006000003', 130020006, 0, 0, 0, null, 130020006130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020006000004, '分配', '130020006000004', 130020006, 0, 0, 0, null, 130020006140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020006000005, '领取', '130020006000005', 130020006, 0, 0, 0, null, 130020006150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020006000006, '退回', '130020006000006', 130020006, 0, 0, 0, null, 130020006160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020006000007, '审批', '130020006000007', 130020006, 0, 0, 0, null, 130020006170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020006000008, '历史', '130020006000008', 130020006, 0, 0, 0, null, 130020006180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020006000009, '终止', '130020006000009', 130020006, 0, 0, 0, null, 130020006190, now(), now(), '1', '1', 0, null);

-- 检验录入按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020007000001, '录入', '130020007000001', 130020007, 0, 0, 0, null, 130020007110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020007000002, '修改', '130020007000002', 130020007, 0, 0, 0, null, 130020007120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020007000003, '历史', '130020007000003', 130020007, 0, 0, 0, null, 130020007130, now(), now(), '1', '1', 0, null);

-- 检验复核按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020008000001, '复核', '130020008000001', 130020008, 0, 0, 0, null, 130020008110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020008000002, '历史', '130020008000002', 130020008, 0, 0, 0, null, 130020008120, now(), now(), '1', '1', 0, null);

-- 检验审核按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020009000001, '审核处理', '130020009000001', 130020009, 0, 0, 0, null, 130020009110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020009000002, '审核进度', '130020009000002', 130020009, 0, 0, 0, null, 130020009120, now(), now(), '1', '1', 0, null);

-- 样品处理按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020010000001, '批量回收', '130020010000001', 130020010, 0, 0, 0, null, 130020010110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020010000002, '批量处理', '130020010000002', 130020010, 0, 0, 0, null, 130020010120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020010000003, '回收', '130020010000003', 130020010, 0, 0, 0, null, 130020010130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130020010000004, '处理', '130020010000004', 130020010, 0, 0, 0, null, 130020010140, now(), now(), '1', '1', 0, null);

-- 报告管理子菜单按钮权限
-- 报告数据点配置按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030001000001, '数据点配置', '130030001000001', 130030001, 0, 0, 0, null, 130030001110, now(), now(), '1', '1', 0, null);

-- 报告配置按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030002000001, '新增模版', '130030002000001', 130030002, 0, 0, 0, null, 130030002110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030002000002, '新增版本', '130030002000002', 130030002, 0, 0, 0, null, 130030002120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030002000003, '数据权限', '130030002000003', 130030002, 0, 0, 0, null, 130030002130, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030002000004, '绑定检验方案', '130030002000004', 130030002, 0, 0, 0, null, 130030002140, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030002000005, '上传', '130030002000005', 130030002, 0, 0, 0, null, 130030002150, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030002000006, '下载', '130030002000006', 130030002, 0, 0, 0, null, 130030002160, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030002000007, '确认', '130030002000007', 130030002, 0, 0, 0, null, 130030002170, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030002000008, '验证', '130030002000008', 130030002, 0, 0, 0, null, 130030002180, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030002000009, '历史', '130030002000009', 130030002, 0, 0, 0, null, 130030002190, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030002000010, '作废', '130030002000010', 130030002, 0, 0, 0, null, 130030002200, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030002000011, '设为默认', '130030002000011', 130030002, 0, 0, 0, null, 130030002210, now(), now(), '1', '1', 0, null);

-- 报告生成按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030003000001, '版本管理', '130030003000001', 130030003, 0, 0, 0, null, 130030003110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030003000002, '下载', '130030003000002', 130030003, 0, 0, 0, null, 130030003120, now(), now(), '1', '1', 0, null);

-- 报告审批按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030004000001, '审批处理', '130030004000001', 130030004, 0, 0, 0, null, 130030004110, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030004000002, '下载', '130030004000002', 130030004, 0, 0, 0, null, 130030004120, now(), now(), '1', '1', 0, null);
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130030004000003, '审批进度', '130030004000003', 130030004, 0, 0, 0, null, 130030004130, now(), now(), '1', '1', 0, null);

-- 检验查询子菜单按钮权限
-- 检验详情按钮权限
INSERT INTO bmos_platform.bp_menu (id, name, code, parent_id, terminal_type, is_menu, is_outside, outside_url, sort, create_time, update_time, create_by, update_by, is_deleted, alias) VALUES (130040001000001, '终止', '130040001000001', 130040001, 0, 0, 0, null, 130040001110, now(), now(), '1', '1', 0, null);

-- 删除任务分配模块的终止按钮
DELETE FROM bmos_platform.bp_menu WHERE id = '130020006000009';
INSERT INTO bmos_platform.bp_tag_define (id, barcode_format, tag_style, tag_width, tag_height, cmd, cmd_type, preview_html, max_field_size, create_time, update_time, create_by, update_by, is_deleted) VALUES (2, 'CODE_128', '一维码', 80, 60, '^XA
^CI28^FS
^CW1,E:SIMSUN.FNT^FS
^PW560^FS
^LL320^FS
^LH0,16^FS
^FO20,0^XGR:LOGO.GRF,1,1^FS
^IDR:LOGO.GRF
^FO20,58^A1,24,24^FD${field1}^FS
^FO20,85^A1,24,24^FD${field2}^FS
^FO20,112^A1,24,24^FD${field3}^FS
^FO20,139^A1,24,24^FD${field4}^FS
^FO20,166^A1,24,24^FD${field5}^FS
^FO20,193^A1,24,24^FD${field6}^FS
^FO20,220^A1,24,24^FD${field7}^FS
^FO20,247^A1,24,24^FD${field8}^FS
^FO20,274^A1,24,24^FD${field9}^FS
^FO320,50
^BQN,2,5
^FD${barcode}^FS
^XZ', 'ZPL', '<!doctype html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport"
    content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Document</title>
  <style>
    * {
      margin: 0;
      padding: 0;
    }

    .fields-item {
      margin-bottom: 0px;
    }

    #tag {
      width: 540px;
      height: 440px;
      box-sizing: border-box;
      background-color: #f8f8f8;
      border: 1px solid #e1e1e1;
      border-radius: 6px;
      overflow: hidden;
      display: flex;
      justify-content: flex-start;
      font-size: 30px;
      flex-direction: column;
    }
  </style>
</head>

<body>
  <div id="tag">
    <div style=" width: 100%; padding: 20px 20px 0; box-sizing: border-box">
      <div id="field1" class="fields-item"></div>
      <div id="field2" class="fields-item"></div>
      <div id="field3" class="fields-item"></div>
      <div id="field4" class="fields-item"></div>
    </div>
    <div style=" width: 540px; padding: 20px 20px; box-sizing: border-box;display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;">
      <img id="qrCode" width="500" height="130" src="" />
      <div id="qrCodeString" class="fields-item"></div>
    </div>
  </div>
</body>

</html>
', 10, '2024-06-12 16:09:06', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene (id, tag_scene_name, tag_scene_desc, tag_type_id, data_source_service_name, data_source_interface, qr_code_field, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002, '样品信息打印标签', '样品信息打印标签', 7, 'bmos-lims2-service', '/api/app/lims2/sample/printTag', 'sampleNo', 6110, '2025-09-22 10:35:08', null, null, null, 0);

INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002001, 130020002, 'materialName', '检品名称', 'String', '氯化钠', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002002, 130020002, 'materialCode', '检品编码', 'String', '0001', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002003, 130020002, 'fullMaterialName', '检品信息', 'String', '0001-氯化钠', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002004, 130020002, 'orderNo', '检验单号', 'String', 'Inspect2025092201', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002005, 130020002, 'materialSpec', '检品规格', 'String', '500g/袋', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002006, 130020002, 'batchNo', '批号', 'String', '20250901', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002007, 130020002, 'sampleNo', '样品编号', 'String', 'Sample20250901', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002008, 130020002, 'inspectItemName', '检验项目名称', 'String', '理化检测', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002009, 130020002, 'inspectItemCode', '检验项目编码', 'String', '01', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002010, 130020002, 'inspectItemInfo', '检验项目信息', 'String', '01-理化检测', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002011, 130020002, 'planQuantityWithUnit', '计划取样量', 'String', '100g', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002012, 130020002, 'quantityWithUnit', '样品数量', 'String', '100g', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002013, 130020002, 'samplerName', '取样人', 'String', '张三', '2025-09-22 11:39:28', null, null, null, 0);
INSERT INTO bmos_platform.bp_tag_scene_field (id, tag_scene_id, field, label, type, example_value, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002014, 130020002, 'samplingTime', '取样时间', 'String', '2025-09-22 11:39:28', '2025-09-22 11:39:28', null, null, null, 0);


INSERT INTO bmos_platform.bp_tag_instance (id, tag_name, tag_type_id, tag_scene_id, tag_define_id, config_fields, is_enable, create_time, update_time, create_by, update_by, is_deleted) VALUES (130020002, '样品信息打印标签', 7, 130020002, 2, '[{"label":"","defineField":"field1","dataSourceField":"fullMaterialName","consumeValue":null},{"label":"","defineField":"field2","dataSourceField":"batchNo","consumeValue":null},{"label":"","defineField":"field3","dataSourceField":"inspectItemName","consumeValue":null},{"label":"","defineField":"field4","dataSourceField":"planQuantityWithUnit","consumeValue":null}]', 'TRUE', '2025-09-22 11:19:01', '2025-09-22 14:22:24', null, '888888888888888873', 0);


INSERT INTO bmos_platform.bp_tag_type (id, tag_type_name, tag_type_desc, sort, create_time, update_time, create_by, update_by, is_deleted) VALUES (7, '样品标签', '检验样品的标签', 7, '2025-09-22 10:17:24', null, null, null, 0);

INSERT INTO bmos_platform.bp_business_parameter (id, code, name, value, value_type, business_type, belong, description, sort, value_range, is_display, create_by, update_by, create_time, update_time, is_deleted) VALUES (130001, 'lims.conclusion.options', '结论判定选项', '[{"label":"不符合规定","value":false},{"label":"符合规定","value":true}]', 'JSON', 'BUSINESS', 'LIMS', '结论判定选项', 130010, '', 1, null, null, now(), now(), 0);