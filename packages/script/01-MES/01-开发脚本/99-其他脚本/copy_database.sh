#!/bin/bash

# 设置字符编码为UTF-8
export LANG=en_US.UTF-8

echo "title=复制远程数据库到本地"

# 读取远程数据库服务器IP地址
read -p "远程数据库服务器IP地址: " rdsIP

# 远程数据库服务器用户
rdsUser=root

# 远程数据库服务器用户密码
rdsPassword=Isysc0re123

# 本地数据库服务器用户
ldsUser=root

# 本地数据库服务器用户密码
ldsPassword=nopassword

# 要复制的数据库的名称
dbName="bmos_mes bmos_platform bmos_lims bmos_wms bmos_scheduler"

echo
echo "$dbName"
echo

# 使用mysqldump导出远程数据库，并导入到本地数据库
sudo mysqldump -h "$rdsIP" -P 3306 -u"$rdsUser" -p"$rdsPassword" --compress --verbose --force -RE --add-drop-database --databases $dbName | mysql -u"$ldsUser" -p"$ldsPassword"

echo
echo "数据库复制完成"