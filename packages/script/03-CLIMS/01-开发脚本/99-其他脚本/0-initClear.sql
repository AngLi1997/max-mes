# 清除菜单权限
truncate table bmos_platform.bp_menu;
# 清除参数配置
truncate table bmos_platform.bp_business_parameter;
# 清除标签配置
truncate table bmos_platform.bp_tag_define;
truncate table bmos_platform.bp_tag_scene_field;
truncate table bmos_platform.bp_tag_instance;
truncate table bmos_platform.bp_tag_scene;
truncate table bmos_platform.bp_tag_type;
# # 清除定时任务
# truncate table bmos_scheduler.xxl_job_user;
# truncate table bmos_scheduler.xxl_job_group;
# truncate table bmos_scheduler.xxl_job_info;
# truncate table bmos_scheduler.xxl_job_lock;
# 清除数据字典
truncate table bmos_platform.bp_dict;
truncate table bmos_platform.bp_dict_detail;
