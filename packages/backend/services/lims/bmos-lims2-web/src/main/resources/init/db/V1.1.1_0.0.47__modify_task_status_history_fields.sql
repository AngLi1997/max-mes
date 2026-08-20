-- 修改任务状态历史表字段
-- 1. 删除 operator_name 字段
-- 2. 重命名 reason -> comment
-- 3. 重命名 description -> remark
-- 4. 添加 node_name 字段

ALTER TABLE lm_task_status_history
    DROP COLUMN operator_name,
    CHANGE COLUMN reason comment VARCHAR(500) NULL COMMENT '评论/备注',
    CHANGE COLUMN description remark VARCHAR(500) NULL COMMENT '备注',
    ADD COLUMN node_name VARCHAR(100) NULL COMMENT '节点名称' AFTER operate_time;
