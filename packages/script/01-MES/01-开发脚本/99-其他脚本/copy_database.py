import subprocess

# 设置字符编码为UTF-8
import locale
locale.setlocale(locale.LC_ALL, 'en_US.UTF-8')

# 读取远程数据库服务器IP地址
rdsIP = input("远程数据库服务器IP地址: ")

# 远程数据库服务器用户
rdsUser = 'root'

# 远程数据库服务器用户密码，使用getpass模块安全地获取密码
rdsPassword = 'Isysc0re123'

# 本地数据库服务器用户
ldsUser = 'root'

# 本地数据库服务器用户密码
ldsPassword = 'nopassword'

# 要复制的数据库的名称
dbNames = "bmos_mes bmos_platform bmos_lims bmos_wms bmos_scheduler".split()

# 使用mysqldump导出远程数据库，并导入到本地数据库
command = [
    'mysqldump',
    '-h', rdsIP,
    '-P', '3306',
    '-u', rdsUser,
    '-p' + rdsPassword,  # 注意：-p后不能有空格
    '--compress',
    '--verbose',
    '--force',
    '-R',
    '-E',
    '--single-transaction',
    '--add-drop-database',
    '--databases'
]+ [dbName for dbName in dbNames]

# 执行导出命令，并将输出导入到本地数据库
with subprocess.Popen(command, stdout=subprocess.PIPE) as process:
    output, _ = process.communicate()
    if process.returncode == 0:
        # 如果导出成功，将导出的数据导入到本地数据库
        importCmd = [
            'mysql',
            '-u', ldsUser,
            '-p' + ldsPassword,  # 注意：-p后不能有空格
        ]
        with subprocess.Popen(importCmd, stdin=subprocess.PIPE) as importProcess:
            importProcess.communicate(input=output)
            if importProcess.returncode == 0:
                print("\n数据库复制完成")
            else:
                print("\n数据库导入失败")
    else:
        print("\n数据库导出失败")