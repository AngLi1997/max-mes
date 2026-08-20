@echo off
chcp 65001

::本机生成脚本环境变量release_type=master;project_type=master;source_release=V2.0.1;target_release=V2.0.2

set workspace_ip=localhost
set workspace_user=root
set workspace_pwd=nopassword

title=构建版本脚本

setlocal enabledelayedexpansion

echo 版本类型：%release_type%
if "%release_type%"=="project" (
    echo 项目类型：%project_type%
)
echo 基线版本：%source_release%
echo 目标版本：%target_release%
pause

::初始化数据版本类型
if "%release_type%"=="master" (
    set source_release_root=02-标准版本
    set target_release_root=02-标准版本
)
if "%release_type%"=="project" (
    if "%project_type%"=="HLYM" (
        set project_code=HLYM
        set project_root=01-华兰疫苗
    )
    if "%project_type%"=="KSKT" (
        set project_code=KSKT
        set project_root=02-康盛科泰
    )
    if "%project_type%"=="BELS" (
        set project_code=BELS
        set project_root=03-白俄罗斯
    )
    set source_release_root=03-项目版本\!project_root!
    set target_release_root=03-项目版本\!project_root!
)

set source_release_directory=..\..\%source_release_root%\%source_release%
set target_release_directory=..\..\%target_release_root%\%target_release%
set merge_sql=%target_release_directory%\merge.sql
echo 基线版本目录：%source_release_directory%
echo 目标版本目录：%target_release_directory%

::定义脚本常量
set menu_root=..\01-菜单权限
set config_root=..\02-参数配置
set flow_root=..\03-审批流程
set label_root=..\04-标签配置
set schedule_root=..\05-定时任务
set dict_root=..\06-数据字典
set nacos_root=..\07-配置中心

set menu_delete_sql=%menu_root%\0-delete.sql
set flow_delete_sql=%flow_root%\0-delete.sql
set config_delete_sql=%config_root%\0-delete.sql
set label_delete_sql=%label_root%\0-delete.sql
set schedule_delete_sql=%schedule_root%\0-delete.sql
set dict_delete_sql=%dict_root%\0-delete.sql

set source_menu_upgrade_sql=%menu_root%\%target_release%.sql
set source_flow_upgrade_sql=%flow_root%\%target_release%.sql
set source_config_upgrade_sql=%config_root%\%target_release%.sql
set source_label_upgrade_sql=%label_root%\%target_release%.sql
set source_schedule_upgrade_sql=%schedule_root%\%target_release%.sql
set source_dict_upgrade_sql=%dict_root%\%target_release%.sql
set source_nacos_file=%nacos_root%\%target_release%\*.yaml

if "%release_type%"=="project" (
    set source_menu_upgrade_sql=%menu_root%\project\%project_code%\%target_release%.sql
    set source_flow_upgrade_sql=%flow_root%\project\%project_code%\%target_release%.sql
    set source_config_upgrade_sql=%config_root%\project\%project_code%\%target_release%.sql
    set source_label_upgrade_sql=%label_root%\project\%project_code%\%target_release%.sql
    set source_schedule_upgrade_sql=%schedule_root%\project\%project_code%\%target_release%.sql
    set source_dict_upgrade_sql=%dict_root%\project\%project_code%\%target_release%.sql
    )

set target_upgrade_root=%target_release_directory%\upgrade
set target_menu_upgrade_sql=%target_upgrade_root%\menu.sql
set target_flow_upgrade_sql=%target_upgrade_root%\flow.sql
set target_config_upgrade_sql=%target_upgrade_root%\config.sql
set target_label_upgrade_sql=%target_upgrade_root%\label.sql
set target_schedule_upgrade_sql=%target_upgrade_root%\schedule.sql
set target_dict_upgrade_sql=%target_upgrade_root%\dict.sql

::整合数据
rd /s/Q %target_release_directory%
md %target_release_directory%
md %target_release_directory%\upgrade
md %target_release_directory%\reset
md %target_release_directory%\target
md %target_release_directory%\target\nacos

if exist %source_menu_upgrade_sql% (
    echo 复制增量脚本 %source_menu_upgrade_sql% to %target_menu_upgrade_sql%
    type %source_menu_upgrade_sql%>%target_menu_upgrade_sql%
)
if exist %source_config_upgrade_sql% (
    echo 复制增量脚本 %source_config_upgrade_sql% to %target_config_upgrade_sql%
    type %source_config_upgrade_sql%>%target_config_upgrade_sql%
)
if exist %source_flow_upgrade_sql% (
    echo 复制增量脚本 %source_flow_upgrade_sql% to %target_flow_upgrade_sql%
    type %source_flow_upgrade_sql%>%target_flow_upgrade_sql%
)
if exist %source_label_upgrade_sql% (
    echo 复制增量脚本 %source_label_upgrade_sql% to %target_label_upgrade_sql%
    type %source_label_upgrade_sql%>%target_label_upgrade_sql%
)
if exist %source_schedule_upgrade_sql% (
    echo 复制增量脚本 %source_schedule_upgrade_sql% to %target_schedule_upgrade_sql%
    type %source_schedule_upgrade_sql%>%target_schedule_upgrade_sql%
)
if exist %source_dict_upgrade_sql% (
    echo 复制增量脚本 %source_dict_upgrade_sql% to %target_dict_upgrade_sql%
    type %source_dict_upgrade_sql%>%target_dict_upgrade_sql%
)

::整合数据库脚本（按文件顺序）
echo 准备构建数据脚本...
echo 初始化脚本： 0-initClear.sql
copy 0-initClear.sql %merge_sql%
copy /b %merge_sql% + %source_release_directory%\target\init.sql %merge_sql%

echo 初始化基于标准版本：%source_release%的增量版本：%target_release%的脚本：%merge_sql%
if exist %target_menu_upgrade_sql% (
    copy /b %merge_sql% + %target_menu_upgrade_sql% %merge_sql%
)
if exist %target_flow_upgrade_sql% (
    copy /b %merge_sql% + %target_flow_upgrade_sql% %merge_sql%
)
if exist %target_config_upgrade_sql% (
    copy /b %merge_sql% + %target_config_upgrade_sql% %merge_sql%
)
if exist %target_label_upgrade_sql% (
    copy /b %merge_sql% + %target_label_upgrade_sql% %merge_sql%
)
if exist %target_schedule_upgrade_sql% (
    copy /b %merge_sql% + %target_schedule_upgrade_sql% %merge_sql%
)
if exist %target_dict_upgrade_sql% (
    copy /b %merge_sql% + %target_dict_upgrade_sql% %merge_sql%
)

::执行数据
echo 指定数据库： workspace_ip: %workspace_ip% workspace_user: %workspace_user% workspace_pwd: %workspace_pwd% 执行增量版本脚本: %merge_sql%
mysql -h%workspace_ip% -u%workspace_user% -p%workspace_pwd%  <%merge_sql%

::导出数据
set menu_tables=bp_menu
set flow_tables=bm_flow_audit_category
set config_tables=bp_business_parameter
set label_tables=bp_tag_define bp_tag_scene_field bp_tag_instance bp_tag_scene bp_tag_type
set schedule_tables=xxl_job_user xxl_job_group xxl_job_info xxl_job_lock
set dict_tables=bp_dict bp_dict_detail

echo 生成重置脚本... to %target_release_directory%\reset
copy %menu_delete_sql% %target_release_directory%\reset\menu.sql
copy %flow_delete_sql% %target_release_directory%\reset\flow.sql
copy %config_delete_sql% %target_release_directory%\reset\config.sql
copy %label_delete_sql% %target_release_directory%\reset\label.sql
copy %schedule_delete_sql% %target_release_directory%\reset\schedule.sql
copy %dict_delete_sql% %target_release_directory%\reset\dict.sql
mysqldump -h%workspace_ip% -u%workspace_user% -p%workspace_pwd% --compress  -RE --no-create-info --databases bmos_platform --tables %menu_tables%  >> %target_release_directory%\reset\menu.sql
mysqldump -h%workspace_ip% -u%workspace_user% -p%workspace_pwd% --compress  -RE --no-create-info --databases bmos_mes --tables %flow_tables% >> %target_release_directory%\reset\flow.sql
mysqldump -h%workspace_ip% -u%workspace_user% -p%workspace_pwd% --compress  -RE --no-create-info --databases bmos_platform --tables %config_tables% >> %target_release_directory%\reset\config.sql
mysqldump -h%workspace_ip% -u%workspace_user% -p%workspace_pwd% --compress  -RE --no-create-info --databases bmos_platform --tables %label_tables% >> %target_release_directory%\reset\label.sql
mysqldump -h%workspace_ip% -u%workspace_user% -p%workspace_pwd% --compress  -RE --no-create-info --databases bmos_scheduler --tables %schedule_tables% >> %target_release_directory%\reset\schedule.sql
mysqldump -h%workspace_ip% -u%workspace_user% -p%workspace_pwd% --compress  -RE --no-create-info --databases bmos_platform --tables %dict_tables% >> %target_release_directory%\reset\dict.sql

echo 整合升级脚本... to %target_release_directory%\target\upgrade.sql
copy /b %target_release_directory%\upgrade\*.sql %target_release_directory%\target\upgrade.sql
echo 整合重置脚本... to %target_release_directory%\target\reset.sql
copy /b %target_release_directory%\reset\*.sql %target_release_directory%\target\reset.sql
echo 整合初始化脚本... to %target_release_directory%\target\init.sql
type 0-initClear.sql>%target_release_directory%\target\init.sql
type %target_release_directory%\target\reset.sql>>%target_release_directory%\target\init.sql
echo 整合nacos配置 to %target_release_directory%\target\nacos目录
copy  %source_nacos_file% %target_release_directory%\target\nacos\
copy "readme.txt" "%target_release_directory%\readme.txt"

echo 已成功完成对 %source_release% 升级为 %target_release% 的构建 ！
echo\
exit
