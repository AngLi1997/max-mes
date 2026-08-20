alter table bp_business_parameter
    add name varchar(64) null comment '参数名称' after code;

create table bp_dept_role
(
    dept_id     bigint            not null comment '部门id，关联到bp_dept表中的id',
    role_id     bigint            not null comment '角色id，关联到bp_role表中的id',
    constraint uk_dept_role
        unique (dept_id, role_id) comment '部门-角色唯一索引'
)
    comment '部门与角色之间的绑定关系' charset = utf8mb4;