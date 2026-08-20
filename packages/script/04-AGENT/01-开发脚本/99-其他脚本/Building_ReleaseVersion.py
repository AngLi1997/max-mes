import os
import shutil
import subprocess
import glob

def copy_directory(src_dir, dest_dir):
    try:
        # 检查源目录是否存在
        if not os.path.exists(src_dir):
            print(f"源目录 {src_dir} 不存在！")
            return

        if os.path.exists(dest_dir):
            shutil.rmtree(dest_dir)
            print(f"已删除目标目录 {dest_dir}")

        # 使用shutil.copytree()来复制目录及其所有内容
        shutil.copytree(src_dir, dest_dir)
        print(f"目录 {src_dir} 已成功复制到 {dest_dir}。")

    except Exception as e:
        print(f"复制过程中发生错误: {e}")

# 环境变量：project_type=HLYM;PYTHONUNBUFFERED=1;release_type=master;root_directory=/Users/a1/script/;source_release=V2.1.0;target_release=V2.1.1;workspace_ip=localhost;workspace_user=root;workspace_pwd=nopassword
release_type = os.getenv('release_type')
project_type = os.getenv('project_type')
source_release = os.getenv('source_release')
target_release = os.getenv('target_release')
root_directory = os.getenv('root_directory')
workspace_ip = os.getenv('workspace_ip')
workspace_user = os.getenv('workspace_user')
workspace_pwd = os.getenv('workspace_pwd')
system_type = os.getenv('system_type')

print('版本类型：' + release_type)
if release_type == 'project':
    print('项目类型：' + project_type)

print('基准版本：' + source_release)
print('目标版本：' + target_release)
print("请按任意键继续...")
input()
# 根据环境变量设置不同的目录和文件路径
if release_type == 'master':
    source_release_root = '02-标准版本'
    target_release_root = '02-标准版本'
    source_sql_root = '01-开发脚本'

source_release_directory = os.path.join('..', '..', source_release_root, source_release)
target_release_directory = os.path.join('..', '..', target_release_root, target_release)
print('基准版本目录：'+source_release_directory)
print('目标版本目录：'+target_release_directory)

# 定义其他目录和文件路径
roots = {
    'menu': '01-菜单权限',
    'config': '02-参数配置',
    'flow': '03-审批流程',
    'label': '04-标签配置',
    'schedule': '05-定时任务',
    'dict': '06-数据字典',
    'nacos': '07-配置中心'
}
for key, sub_path in roots.items():
    globals()[key] = os.path.join(source_sql_root , sub_path)

delete_sql_template = '0-delete.sql'
target_upgrade_root = os.path.join(target_release_directory,'upgrade')
for key in roots.keys():
    # 清除脚本
    delete_sql_var_name = key + '_delete_sql'
    globals()[delete_sql_var_name] = os.path.join(globals()[key], delete_sql_template)
    # 基准升级脚本
    source_var_upgrade_sql = key + '_upgrade_sql'
    globals()[source_var_upgrade_sql] = os.path.join(globals()[key], target_release + '.sql')
    # 目标脚本目录
    target_var_upgrade_sql = key + '.sql'
    globals()[f'target_{key}_upgrade_sql'] = os.path.join(target_upgrade_root,target_var_upgrade_sql)


# nacos_file_pattern = os.path.join(root_directory,system_type, nacos, target_release, '*.yaml')
nacos_file_pattern = os.path.join(root_directory,system_type, nacos, target_release)
# yaml_files = glob.glob(nacos_file_pattern)

# 整合数据
if os.path.exists(target_release_directory):
    shutil.rmtree(target_release_directory)
os.makedirs(target_release_directory)
os.makedirs(os.path.join(target_release_directory, 'upgrade'))
os.makedirs(os.path.join(target_release_directory, 'reset'))
os.makedirs(os.path.join(target_release_directory, 'target'))
os.makedirs(os.path.join(target_release_directory, 'target' , 'nacos'))

# 复制增量脚本
print("复制增量脚本")
for key in roots.keys():
    upgrade_file = os.path.join(root_directory, system_type, globals()[key], target_release + '.sql')
    target_file = os.path.join(target_release_directory, 'upgrade', key + '.sql')
    if os.path.exists(upgrade_file):
        shutil.copy(upgrade_file, target_file)
        print(f"Copied {upgrade_file} to {target_file}")
    else:
        print(f"Source file {upgrade_file} does not exist.")

# 构建数据脚本
merge_sql = os.path.join(target_release_directory, 'merge.sql')
with open('0-initClear.sql', 'r') as initClear_file:
    initClear_sql = initClear_file.read()
with open(merge_sql, 'w') as outfile:
    outfile.write(initClear_sql)
merge_source_sql = os.path.join(source_release_directory, 'target/init.sql')
with open(merge_source_sql, 'r') as source_init:
    init_sql = source_init.read()
with open(merge_sql,'a') as  outfile:
    outfile.write('\n')
    outfile.write(init_sql)
    for key in roots.keys():
        target_upgrade_sql = os.path.join(target_release_directory, 'upgrade', key + '.sql')
        if os.path.exists(target_upgrade_sql):
            with open(target_upgrade_sql, 'r') as infile:
                outfile.write(infile.read())

# 执行数据脚本
mysql_command = [
    'mysql', '-h', workspace_ip, '-u', workspace_user, '-p' + workspace_pwd,
]
try:
    with open(merge_sql, 'r') as sql_file:
        subprocess.run(mysql_command, stdin=sql_file, check=True)
        print("执行数据脚本")
except subprocess.CalledProcessError as e:
    print(f"An error occurred: {e}")

# 导出数据
tables = {
    'menu': ['bp_menu', 'bmos_platform'],
    'flow': ['bm_flow_audit_category', 'bmos_mes'],
    'config': ['bp_business_parameter', 'bmos_platform'],
    'label': ['bp_tag_define', 'bp_tag_scene_field', 'bp_tag_instance', 'bp_tag_scene', 'bp_tag_type', 'bmos_platform'],
    'schedule': ['xxl_job_user', 'xxl_job_group', 'xxl_job_info', 'xxl_job_lock', 'bmos_scheduler'],
    'dict': ['bp_dict', 'bp_dict_detail', 'bmos_platform']
}

# 复制重置脚本
os.makedirs(os.path.join(target_release_directory, 'reset'), exist_ok=True)
for key in roots.keys():
    delete_file = os.path.join(root_directory, system_type, globals()[key],  '0-delete.sql')
    reset_file = os.path.join(target_release_directory, 'reset', key + '.sql')
    if os.path.exists(delete_file):
        shutil.copy(delete_file, reset_file)
        print(f"Copied {delete_file} to {reset_file}")
    else:
        print(f"Source file {delete_file} does not exist.")

# 导出数据并生成重置脚本
for key, values in tables.items():
    if isinstance(values, list):
        tables_lists = ' '.join(values[:-1]).split()
        db_name = values[-1]
    else:
        tables_lists = values
        db_name = 'bmos_platform'

    dump_command = ['mysqldump', db_name]+ [tables_list for  tables_list in tables_lists]+ ['--skip-add-drop-table','--skip-create-options','--add-locks','--extended-insert','--disable-keys','--complete-insert','--lock-tables','--no-create-info','--user='+workspace_user,'-p' + workspace_pwd,'-h', workspace_ip]
    dump_file = os.path.join(target_release_directory, 'reset', key + '.sql')
    with open(dump_file, 'a') as outfile:
        # print(dump_command)
        subprocess.run(dump_command, stdout=outfile)
    print(f"Data dumped for {key} into {dump_file}")

# 整合升级脚本
upgrade_sql_files = glob.glob(os.path.join(target_release_directory, 'upgrade', '*.sql'))
upgrade_sql_path = os.path.join(target_release_directory, 'target', 'upgrade.sql')
with open(upgrade_sql_path, 'w') as outfile:
    for sql_file in upgrade_sql_files:
        with open(sql_file, 'r') as infile:
            shutil.copyfileobj(infile, outfile)
print(f"整合升级脚本 to {upgrade_sql_path}")

# 整合重置脚本
reset_sql_files = glob.glob(os.path.join(target_release_directory, 'reset', '*.sql'))
reset_sql_path = os.path.join(target_release_directory, 'target', 'reset.sql')
with open(reset_sql_path, 'w') as outfile:
    for sql_file in reset_sql_files:
        with open(sql_file, 'r') as infile:
            shutil.copyfileobj(infile, outfile)
print(f"整合重置脚本 to {reset_sql_path}")

# 整合初始化脚本
init_sql_path = os.path.join(target_release_directory, 'target', 'init.sql')
with open(init_sql_path, 'w') as init_file:
    with open('0-initClear.sql', 'r') as clear_script:
        init_file.write(clear_script.read())
    with open(reset_sql_path, 'r') as reset_script:
        init_file.write(reset_script.read())
print(f"整合初始化脚本 to {init_sql_path}")

# 整合nacos配置
nacos_target_path = os.path.join(target_release_directory, 'target', 'nacos')
copy_directory(nacos_file_pattern, nacos_target_path)
# for yaml_file in yaml_files:
#     target_file_path = os.path.join(nacos_target_path, os.path.basename(yaml_file))
#     shutil.copy2(yaml_file, target_file_path)
print(f"整合nacos配置文件 to {nacos_target_path}")

# 整合readme
readme_src_path = 'readme.txt'
readme_target_path = os.path.join(target_release_directory, 'readme.txt')
shutil.copy(readme_src_path, readme_target_path)

# 打印完成信息
print(f'已成功完成对 {source_release} 升级为 {target_release} 的构建！')
