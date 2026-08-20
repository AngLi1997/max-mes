-- admin账户
INSERT INTO bmos_scheduler.xxl_job_user (id, username, password, role, permission) VALUES (1, 'admin', '0192023a7bbd73250516f069df18b500', 1, null);
-- 执行器
INSERT INTO bmos_scheduler.xxl_job_group (id, app_name, title, address_type, address_list, update_time) VALUES (1, 'bmos-mes', 'bmos-mes', 0, null, now());
INSERT INTO bmos_scheduler.xxl_job_group (id, app_name, title, address_type, address_list, update_time) VALUES (2, 'bmos-wms', 'bmos-wms', 0, null, now());
-- 定时器
INSERT INTO bmos_scheduler.xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES (1, 1, '刷新MES物料批次可用状态', now(), now(), 'admin', '', 'CRON', '0 0 0 * * ?', 'DO_NOTHING', 'FIRST', 'updateStorageBatchAvailable', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', now(), now(), '', 0, 0, 0);
INSERT INTO bmos_scheduler.xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES (2, 2, '刷新WMS货品批次可用状态', now(), now(), 'admin', '', 'CRON', '0 0 0 * * ?', 'DO_NOTHING', 'FIRST', 'refreshInventoryBatchAvailable', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', now(), now(), '', 0, 0, 0);
-- 锁
INSERT INTO bmos_scheduler.xxl_job_lock (lock_name) VALUES ('schedule_lock');

# 用户密码过期状态更改定时任务 by zhangruoyu 240416
INSERT INTO bmos_scheduler.xxl_job_group (id, app_name, title, address_type, address_list, update_time) VALUES (3, 'bmos-platform-service', '制药管理平台', 0, null, now());
INSERT INTO bmos_scheduler.xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES (3, 3, '刷新账户密码过期状态', now(), now(), 'admin', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'userPwdExpireValid', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', now(), now(),  '', 0, 0, 0);
UPDATE bmos_scheduler.xxl_job_group SET app_name = 'bmos-mes-service', title = '制造执行系统' WHERE id = 1;
UPDATE bmos_scheduler.xxl_job_group SET app_name = 'bmos-wms-service', title = '仓储管理系统' WHERE id = 2;

# 定时任务调整为默认启用 by zhangruoyu 240428
UPDATE bmos_scheduler.xxl_job_info SET trigger_status = 1 WHERE id = 1;
UPDATE bmos_scheduler.xxl_job_info SET trigger_status = 1 WHERE id = 2;
UPDATE bmos_scheduler.xxl_job_info SET trigger_status = 1 WHERE id = 3;

# 新增设备占用心跳释放定时任务
INSERT INTO bmos_scheduler.xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES (4, 3, '设备占用心跳释放', now(), now(), 'admin', '', 'CRON', '0/10 * * * * ?', 'DO_NOTHING', 'FIRST', 'equipmentHeart', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', now(), now(), '', 0, 0, 0);