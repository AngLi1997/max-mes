# 操作规程生效状态变更定时任务 by zhangruoyu 240603
INSERT INTO bmos_scheduler.xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
VALUES (5, 1, '操作规程状态修改', now(), now(), 'admin', '', 'CRON', '0 5 0 ? * *', 'DO_NOTHING', 'FIRST', 'updateOperateRuleVersion', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 1, 0, now());

# 设备占用心跳释放默认启用 by lilong 240710
UPDATE bmos_scheduler.xxl_job_info SET trigger_status = 1 WHERE id = 4;

# 更新操作规程状态修改cron表达式 by renjinguang 240712
UPDATE bmos_scheduler.xxl_job_info SET schedule_conf = '0 5 0 * * ?' WHERE id = 5;
