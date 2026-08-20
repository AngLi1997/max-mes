@echo off
chcp 65001

title=复制远程数据库到本地

::远程数据库服务器IP地址
set /p rdsIP=远程数据库服务器IP地址：
::远程数据库服务器用户
set rdsUser=root
::远程数据库服务器用户密码
set rdsPassword=Isysc0re123

::本地数据库服务器用户
set ldsUser=root
::本地数据库服务器用户密码
set ldsPassword=nopassword

::要复制的数据库的名称
set dbName=bmos_mes bmos_platform bmos_lims bmos_wms bmos_scheduler

echo\
echo %dbName%
echo\

mysqldump -h %rdsIP% -P 3306 -u%rdsUser% -p%rdsPassword% --compress  --verbose --force -RE --add-drop-database --databases %dbName% | mysql -u%ldsUser% -p%ldsPassword%

echo\
exit