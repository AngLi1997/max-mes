REPLACE INTO bmos_scheduler.xxl_job_group (id, app_name, title, address_type, address_list, update_time) VALUES (5, 'bmos-lims2-service', '实验室信息管理系统', 0, '', '2026-04-09 11:32:08');

REPLACE INTO bmos_scheduler.xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time) VALUES (12, 5, '稳定性考察计划定时任务', '2026-04-09 11:36:28', '2026-04-09 11:36:52', 'admin', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'stabilityTriggerDueTimepointTasks', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2026-04-09 11:36:28', '', 1, 0, 1775754000000);


