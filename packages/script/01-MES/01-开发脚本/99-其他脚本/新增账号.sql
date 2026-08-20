use bmos_platform;
# 内置系统管理员角色拥有所有权限；
REPLACE INTO bmos_platform.bp_role_type (id, role_type_name, parent_id, create_time, update_time, create_by, update_by, is_deleted) VALUES (100, '系统内置', 0, now(),now(), '1', '1', 0);
REPLACE INTO bmos_platform.bp_role (id, role_name, role_type_id, description, create_time, update_time, create_by, update_by, is_deleted) VALUES (1001, '系统管理员', 100, null, now(),now(), '1', '1', 0);
REPLACE INTO bmos_platform.bp_auth_role_menu (role_id, menu_id) select 1001, id from bmos_platform.bp_menu where 1=1;
REPLACE INTO bmos_platform.bp_role_menu (role_id, menu_id) select 1001, id from bmos_platform.bp_menu where 1=1;

# 清除没角色、菜单的关联数据
DELETE FROM bmos_platform.bp_auth_role_menu WHERE role_id NOT IN (SELECT id FROM bmos_platform.bp_role );
DELETE FROM bmos_platform.bp_auth_role_menu WHERE menu_id NOT IN (SELECT id FROM bmos_platform.bp_menu );
DELETE FROM bmos_platform.bp_role_menu WHERE role_id NOT IN (SELECT id FROM bmos_platform.bp_role );
DELETE FROM bmos_platform.bp_role_menu WHERE menu_id NOT IN (SELECT id FROM bmos_platform.bp_menu );

# 清除无账号的部门关联数据
DELETE FROM bmos_platform.bp_user_dept WHERE bmos_platform.bp_user_dept.user_id NOT IN (SELECT bp_user.user_id FROM bmos_platform.bp_user );

# 生成账号
delimiter $$
drop procedure if exists init_user;
create procedure init_user()
begin
    declare user varchar(255) default 'lilong,lixiao,yxr,cdt';
    declare name varchar(255) default '李龙,李潇,俞晓荣,陈丹婷';
    declare i int default 0;
    declare n int default CHAR_LENGTH(user) - CHAR_LENGTH(REPLACE(user, ',', '')) + 1;
    set @id = 888;
    while i < n
        do
            set @user_temp = (SUBSTRING_INDEX(SUBSTRING_INDEX(user, ',', i + 1), ',', -1));
            set @name_temp = (SUBSTRING_INDEX(SUBSTRING_INDEX(name, ',', i + 1), ',', -1));
            delete from bmos_platform.bp_user where login_name = @user_temp;
            REPLACE INTO bmos_platform.bp_user (id, user_id, user_name, login_name, password, gender, valid_time, pwd_error_count, active_status, state)
            VALUES (@id - i, @id - i, @name_temp, @user_temp, 'c44e35b93479b82f', 1, 9999999999999, 0, 1, 1);
            REPLACE INTO bmos_platform.bp_user_role (id, user_id, role_id, is_deleted) SELECT @id - i, @id - i, 1001,0 ;
            REPLACE INTO bmos_platform.bp_user_dept (id, user_id,dept_id) select concat(id,@id - i), @id - i, id from  bmos_platform.bp_dept where 1=1;
            set i = i + 1;
        end while;
end $$;
delimiter ;
call init_user();
drop procedure if exists init_user;

# # admin账号
# INSERT INTO bmos_platform.bp_user (id, user_id, user_name, login_name, password, phone, email, gender, remark, valid_time, pwd_error_count, active_status, state, create_time, update_time, create_by, update_by, is_deleted) VALUES (1, '1', '管理员', 'admin', '1aadf96235e3f7bb7729488fd2e73b85', '', null, 0, null, 9999999999999, 0, 1, 1, '2023-11-13 19:00:54', '2024-04-15 14:25:12', '1', '1', 0);

# # 删除内置角色
# delete from bmos_platform.bp_role_type where bmos_platform.bp_role_type.id = 100;
# delete from bmos_platform.bp_role where bmos_platform.bp_role.id = 1001;
# delete from bmos_platform.bp_auth_role_menu where bmos_platform.bp_auth_role_menu.role_id = 1001;
# delete from bmos_platform.bp_role_menu where bmos_platform.bp_role_menu.role_id =1001;
# delete from bp_user_role where role_id =1001;
# # 删除内置账号
# delete from bp_user where id>1 and id < 888888888888888889;
# delete from bp_user_role where user_id >1 and user_id < 888888888888888889;

# dog,cat,lion,tiger,ckl,cl,dyl,dk,ll,ld,pzw,sp,xhx,yxq,zry,zj
# 京东,天猫,苏宁,国美,蔡康林,车路,戴亚伦,邓轲,李磊,鲁丁,庞志巍,粟攀,谢鸿翔,叶小倩,张若雨,钟杰

# UPDATE bmos_platform.bp_user SET password = 'c44e35b93479b82f' WHERE 1=1;
