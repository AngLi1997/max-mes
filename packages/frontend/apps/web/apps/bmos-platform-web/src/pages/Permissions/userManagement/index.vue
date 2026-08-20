<template>
  <div class="container" style="padding: 16px; background-color: #fff">
    <BMTable
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      :scroll="{ x: 1200, y: 400 }"
      :showRefresh="false"
      :formProps="formProps">
      <template #toolbar>
        <Button v-hasAuth="100030001000001" type="primary" @click="lookOrEdit('11', 'add')">
          {{ t('新增') }}
        </Button>
        <!-- 按钮权限待加 -->
        <Button @click="() => ImportModalRef.openModal()">
          {{ t('导入') }}
        </Button>
        <Dropdown :trigger="['click']">
          <Button>
            {{ t('导出') }}
          </Button>
          <template #overlay>
            <Menu>
              <MenuItem key="1" @click="export1('screen')">{{ t('导出筛选数据') }}</MenuItem>
              <MenuItem key="2" @click="export1('currentPage')">{{ t('导出当前页数据') }}</MenuItem>
            </Menu>
          </template>
        </Dropdown>
      </template>
    </BMTable>
    <!-- 新增编辑查看弹框组件 -->
    <ManagingUsers
      ref="managingUsersRef"
      :rowData="rowData"
      :showTitle="popTitle"
      @loadData="updateData"></ManagingUsers>
    <!-- 绑定角色弹框组件 -->
    <BindRole ref="bindRoleRef" @updateData="updateData"></BindRole>
    <!-- 分配部门弹框组件 -->
    <AllocationDepartment ref="allocationDepartmentRef" @updateData="updateData"></AllocationDepartment>
    <!-- 重置密码成功弹框 -->
    <RePassWord ref="rePassWordRef"></RePassWord>
    <!-- 修改密码 -->
    <changePassword :id="id" ref="changePasswordRef" :userId="userId"></changePassword>
    <!-- 查看-弹框 -->
    <ViewModal ref="viewModalRef" :rowData="rowData"></ViewModal>
    <!-- 导入-弹框 -->
    <ImportModal
      ref="ImportModalRef"
      :downloadTemplate="reqUserDownloadTemplate"
      :importFile="reqUserImport"
      @updateTable="updateData"></ImportModal>
  </div>
</template>

<script lang="tsx" setup>
  import type { DataRequestFn, TableInstance } from '@bmos/components';
  import { BMTable, TableColumn } from '@bmos/components';
  import ManagingUsers from '../userManagement/bulletBox/managingUsers.vue';
  import BindRole from '../userManagement/bulletBox/bindRole.vue';
  import AllocationDepartment from '../userManagement/bulletBox/allocationDepartment.vue';
  import RePassWord from '../userManagement/bulletBox/rePassWord.vue';
  import changePassword from '../userManagement/bulletBox/changeUserPassWord.vue';
  import ViewModal from '../userManagement/bulletBox/viewModal.vue';
  import ImportModal from '@/components/ImportModal/index.vue';
  import { reactive, ref, createVNode } from 'vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, message, Button, Dropdown, Menu, MenuItem } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { encrypt } from '@bmos/utils';
  import { fileStreamDownload } from '@bmos/utils';
  import {
    userPage,
    editUser,
    relateRoleData,
    relateDeptData,
    rePassWord,
    startStop,
    reqUserDownloadTemplate,
    reqUserImport,
    reqUserExport,
  } from '@/api/Permissions/userManagement';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();

  const tableInstance = ref<TableInstance>();
  const popTitle = ref('');
  const viewModalRef = ref<any>();
  const ImportModalRef = ref<any>();
  const formProps = reactive<any>({
    actionColOptions: {},
    baseColProps: {
      span: 6,
    },
    showAdvancedButtonBadge: false,
  });
  const managingUsersRef = ref();
  const bindRoleRef = ref<any>();
  const allocationDepartmentRef = ref();
  const rePassWordRef = ref();
  const changePasswordRef = ref();
  const rowData = ref<any>({});
  const userId = ref(); //传给修改密码弹框
  const id = ref(); //传给修改密码弹框(0715改)
  const queryParams = ref<any>(); //存查询过的参数

  const loadData: DataRequestFn = async (params): Promise<any> => {
    queryParams.value = params;
    return userPage(params);
  };
  // 刷新数据
  const updateData = () => {
    tableInstance.value?.fetchData();
  };
  // 切换开关
  const changeSwitch = (val: any) => {
    let title = val.state ? t('是否停用该用户') : t('是否启用该用户');
    let content = val.state
      ? t('停用后，该用户的角色信息、部门信息都将解绑。')
      : t('启用后，可为用户配置角色信息、部门信息。');
    Modal.confirm({
      title,
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content,
      okText: t('确定'),
      cancelText: t('取消'),
      onOk() {
        confirmChangeSwitch(val);
      },
    });
  };
  const confirmChangeSwitch = async (row: any) => {
    // 若最先为开启1
    if (row.state) {
      const data = { userId: row.userId, id: row.id, state: 0 };
      await startStop(data);
      message.success(t('停用成功'));
    } else {
      const data = { userId: row.userId, id: row.id, state: 1 };
      await startStop(data);
      message.success(t('启用成功'));
    }
    updateData();
  };
  const resetPassword = (val: any) => {
    Modal.confirm({
      title: t('是否重置用户密码'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('密码重置后需用户重新登录激活。'),
      okText: t('重置'),
      cancelText: t('取消'),
      onOk() {
        handleReset(val);
      },
    });
  };
  const handleReset = async (row: any) => {
    rePassWordRef.value.sixNum = Math.floor(Math.random() * (999999 - 100000 + 1) + 100000) + '';
    rePassWordRef.value.name = row.userName;
    const data = {
      userId: row.id,
      password: encrypt(rePassWordRef.value.sixNum),
    };
    try {
      const res: any = await rePassWord(data);
      if (res.code === 0) {
        message.success(t('重置成功'));
        updateData();
        rePassWordRef.value.openModal();
        return;
      }
      message.error(t(res.message));
    } catch (error: any) {
      message.error(error.message);
    }

    //
  };
  // 解锁
  const unLock = (val: any) => {
    Modal.confirm({
      title: t('是否解锁用户密码'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('解锁后，用户可重新登录账号。'),
      okText: t('解锁'),
      cancelText: t('取消'),
      onOk() {
        handleUnLock(val);
      },
    });
  };
  const handleUnLock = async (row: any) => {
    const data = { id: row.id, status: 1, lock: true };
    await editUser(data);
    message.success(t('解锁成功'));
    updateData();
  };
  // 状态样式
  const style = {
    width: '7px',
    height: '7px',
    borderRadius: '50%',
    marginRight: '8px',
  };
  const colorList: any = {
    0: {
      color: 'rgb(255,154,47)',
    },
    1: {
      color: 'rgb(89,191,120)',
    },
    2: {
      color: 'rgb(255,86,51)',
    },
    3: {
      color: 'rgb(255,86,51)',
    },
  };

  // 绑定角色
  const bindRole = async (row: any) => {
    bindRoleRef.value.openModal();
    bindRoleRef.value.rowId = row.userId;
    const res = await relateRoleData({ userId: row.userId });
    bindRoleRef.value.checkedKeys = res.data;
    bindRoleRef.value.expandedKeys = res.data;
    if (res.data.length === 0) {
      bindRoleRef.value.expandedKeys = ['0'];
    }
  };
  // 分配部门
  const allocationDepartment = async (row: any) => {
    allocationDepartmentRef.value.openModal();
    allocationDepartmentRef.value.rowId = row.userId;
    const res = await relateDeptData({ userId: row.userId });
    allocationDepartmentRef.value.checkedKeys = res.data;
    allocationDepartmentRef.value.expandedKeys = res.data;
    if (res.data.length === 0) {
      allocationDepartmentRef.value.expandedKeys = [allocationDepartmentRef.value.treeProps.treeData[0].id];
    }
  };
  // 数据项类型
  const columns: TableColumn[] = [
    {
      title: t('用户名称'),
      align: 'left',
      dataIndex: 'userName',
      resizable: true,
      formItemProps: {
        order: 0,
      },
    },
    {
      title: t('用户账号'),
      align: 'left',
      dataIndex: 'loginName',
      resizable: true,
      formItemProps: {
        order: 1,
      },
    },
    {
      title: t('邮箱'),
      align: 'left',
      dataIndex: 'email',
      hideInSearch: true,
      hideInTable: true,
      resizable: true,
    },
    {
      title: t('性别'),
      align: 'left',
      dataIndex: 'gender',
      resizable: true,
      formItemProps: {
        component: 'Select',
        order: 5,
        componentProps: () => ({
          options: [
            {
              label: t('男'),
              value: 0,
            },
            {
              label: t('女'),
              value: 1,
            },
          ],
        }),
      },
      customRender: ({ record }) => [t('男'), t('女')][record.gender],
    },
    {
      title: t('手机号'),
      align: 'left',
      dataIndex: 'phone',
      resizable: true,
      formItemProps: {
        order: 2,
      },
    },
    {
      title: t('状态'),
      align: 'left',
      dataIndex: 'status',
      resizable: true,
      formItemProps: {
        component: 'Select',
        order: 4,
        componentProps: () => ({
          options: [
            {
              label: t('待激活'),
              value: 0,
            },
            {
              label: t('已激活'),
              value: 1,
            },
            {
              label: t('密码过期'),
              value: 2,
            },
            {
              label: t('密码锁定'),
              value: 3,
            },
          ],
        }),
      },
      customRender: ({ record }) => (
        <div
          style='display: flex;
       align-items: center;'>
          <div
            style={{
              ...style,
              backgroundColor: colorList[record.status]?.color,
            }}></div>
          <div style={{ color: colorList[record.status]?.color }}>
            {[t('待激活'), t('已激活'), t('密码过期'), t('密码锁定')][record.status]}
          </div>
        </div>
      ),
    },
    {
      title: t('启停'),
      align: 'left',
      dataIndex: 'state',
      width: 120,
      resizable: true,
      formItemProps: {
        component: 'Select',
        order: 3,
        componentProps: () => ({
          options: [
            {
              label: t('停用'),
              value: 0,
            },
            {
              label: t('启用'),
              value: 1,
            },
          ],
        }),
      },
      customRender: ({ record }) => (
        <a-switch
          disabled={!hasPermission('100030001000008')}
          checked={record.state == 1 ? true : false}
          onClick={() => changeSwitch(record)}></a-switch>
      ),
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      hideInSearch: true,
      width: 280,
      key: 'ACTION',
      resizable: true,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow: record.state !== 0 && hasPermission('100030001000004'),
          onClick: () => {
            lookOrEdit(record, 'look');
          },
        },
        {
          label: t('编辑'),
          ifShow: !record.state && hasPermission('100030001000010'),
          onClick: () => {
            lookOrEdit(record, 'edit');
          },
        },
        {
          label: t('绑定角色'),
          ifShow: record.state !== 0 && hasPermission('100030001000005'),
          onClick: () => {
            bindRole(record);
          },
        },
        {
          label: t('分配部门'),
          ifShow: record.state !== 0 && hasPermission('100030001000006'),
          onClick: () => {
            allocationDepartment(record);
          },
        },
        {
          label: t('重置密码'),
          ifShow: record.state !== 0 && hasPermission('100030001000007'),
          onClick: () => {
            resetPassword(record);
          },
        },
        {
          label: t('修改密码'),
          ifShow: record.state !== 0 && hasPermission('100030001000011'),
          onClick: () => {
            changeUserPassword(record);
          },
        },
        {
          label: t('解锁'),
          ifShow: record.status == 3 && hasPermission('100030001000009'),
          onClick: () => {
            unLock(record);
          },
        },
      ],
    },
  ];
  const lookOrEdit = async (val: any, type: String) => {
    if (type === 'add') {
      popTitle.value = t('新增用户');
      rowData.value = val;
      managingUsersRef.value.formProps.initialValues = {};
      managingUsersRef.value.resetForm();
      managingUsersRef.value.formProps.disabled = false;
      managingUsersRef.value.formProps.schemas[1].componentProps.disabled = false;
      managingUsersRef.value.openModal();
    }
    if (type === 'look') {
      rowData.value = val;
      viewModalRef.value.openModal();
    }
    if (type === 'edit') {
      rowData.value = val;
      popTitle.value = t('编辑用户');
      managingUsersRef.value.formProps.initialValues = val;
      managingUsersRef.value.formProps.disabled = false;
      managingUsersRef.value.formProps.schemas[1].componentProps.disabled = true;
      managingUsersRef.value.openModal();
    }
  };
  // 用户管理操作栏里修改密码
  const changeUserPassword = async (row: any) => {
    userId.value = row.userId;
    id.value = row.id;
    changePasswordRef.value.showModal();
  };
  // 截取
  const getContentBetweenChars = (str: any) => {
    return decodeURI(str.match(/filename=(\S*).xlsx/)[1]);
  };
  // 导出筛选数据/导出当前页数据
  const export1 = async (type: any) => {
    const temp = tableInstance.value?.getQueryFormRef()?.getFormValues();
    const data = type === 'screen' ? temp : queryParams.value;
    const data2 = { ...data, isFlay: type === 'screen' ? true : false };
    try {
      const res: any = await reqUserExport(data2);
      const fileName: any = getContentBetweenChars(res.headers['content-disposition']);
      fileStreamDownload(res.data, fileName);
    } catch (error: any) {
      message.error(error.message);
    }
  };
</script>
<style lang="less" scoped>
  .container {
    height: 100%;
    display: flex;
    flex-direction: column;
  }
  .assignPersonnel .bmos-search-tree {
    width: 100%;
  }
</style>
