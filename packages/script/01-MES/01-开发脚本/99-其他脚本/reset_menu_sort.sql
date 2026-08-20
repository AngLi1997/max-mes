# 重置菜一级单顺序脚本
set @i := 100;
update bmos_platform.bp_menu
set sort = (@i := @i + 10)
where parent_id = 0
order by sort;

# 重置子级菜单顺序脚本
DROP PROCEDURE IF EXISTS init_leaf_menu_sequence;
delimiter $$
create procedure init_leaf_menu_sequence()
begin
    declare i int default 0;
    declare j int default 0;
    declare n int default 0;
    declare m int default 0;
    declare p_id bigint default 0;
    declare p_sort int default 0;
    declare p_leaf_id bigint default 0;
    declare p_leaf_sort int default 0;
    # 重置二级菜单顺序脚本
    set i = 0;
    select count(*) from bmos_platform.bp_menu where parent_id = 0 into n;
    while i < n
        do
            select id from bmos_platform.bp_menu where parent_id = 0 order by sort limit i,1 into p_id;
            select sort from bmos_platform.bp_menu where id = p_id into p_sort;
            set @seed = 100;
            update bmos_platform.bp_menu
            set sort = concat(p_sort, @seed := @seed + 10)
            where parent_id = p_id
            order by sort;
            # 重置三级菜单顺序脚本
            set j = 0;
            select count(*) from bmos_platform.bp_menu where parent_id = p_id into m;
            while j < m
                do
                    select id from bmos_platform.bp_menu where parent_id = p_id order by sort limit j,1 into p_leaf_id;
                    select sort from bmos_platform.bp_menu where id = p_leaf_id into p_leaf_sort;
                    set @seed = 100;
                    update bmos_platform.bp_menu
                    set sort = concat(p_leaf_sort, @seed := @seed + 10)
                    where parent_id = p_leaf_id
                    order by sort;
                    set j = j + 1;
                end while;
            set i = i + 1;
        end while;
end$$
delimiter $$
delimiter ;
call init_leaf_menu_sequence();
DROP PROCEDURE IF EXISTS init_leaf_menu_sequence;