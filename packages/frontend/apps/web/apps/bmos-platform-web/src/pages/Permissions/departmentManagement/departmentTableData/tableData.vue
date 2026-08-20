<template>
  <div class="tableData">
    <!-- 描述列表 -->
    <Descriptions :title="t('部门信息')" bordered size="middle">
      <template #extra>
        <Space :size="spaceSize">
          <Button
            v-if="props.hasParent"
            v-hasAuth="inside === '1' ? 100030006000006 : 100030002000006"
            @click="assignRole">
            {{ t('分配角色') }}
          </Button>
          <Button v-hasAuth="inside === '1' ? 100030006000001 : 100030002000001" @click="addOrEdit('add')">
            {{ t('新增') }}
          </Button>
          <Button
            v-if="props.hasParent"
            v-hasAuth="inside === '1' ? 100030006000002 : 100030002000002"
            @click="addOrEdit('edit')">
            {{ t('编辑') }}
          </Button>
          <Button
            v-if="props.hasParent"
            v-hasAuth="inside === '1' ? 100030006000003 : 100030002000003"
            danger
            @click="deletedepartment">
            {{ t('删除') }}
          </Button>
        </Space>
      </template>
      <DescriptionsItem :label="t('上级部门')">
        {{ props.departmentInfo.up || '-' }}
      </DescriptionsItem>
      <DescriptionsItem :label="t('部门名称')">
        {{ props.departmentInfo.name || props.rootNode || '' }}
      </DescriptionsItem>
      <DescriptionsItem :label="t('备注')">
        {{ props.departmentInfo.remarks }}
      </DescriptionsItem>
    </Descriptions>
    <Divider />
    <div class="table">
      <BMTable
        ref="tableInstance"
        :data-request="loadData"
        :columns="columns"
        :search="search"
        row-key="userId"
        :headerTitle="t('人员信息')"
        :scroll="{ x: 844, y: 400 }"
        :showRefresh="false"
        :pagination="{
          pageSize: 20,
        }"
        :formProps="formProps">
        <template #toolbar>
          <Button
            v-if="props.hasParent"
            v-hasAuth="inside === '1' ? 100030006000004 : 100030002000004"
            type="primary"
            @click="assignPersonnel">
            {{ t('分配人员') }}
          </Button>
          <Button
            v-if="props.hasParent"
            v-hasAuth="inside === '1' ? 100030006000005 : 100030002000005"
            danger
            @click="removeAll">
            {{ t('全部移除') }}
          </Button>
        </template>
      </BMTable>
    </div>
  </div>
  <!-- 新增编辑框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps2"
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeMedium"
    @cancelModal="cancel"
    @okModal="ok"></BMModalForm>
  <!-- 分配角色弹框 -->
  <AssignRoleModal ref="AssignRoleRef" :deptId="deptId" :inside="inside" @updateTable="updata"></AssignRoleModal>
  <!-- 查看用户弹框 -->
  <ViewModal ref="viewModalRef" :rowData="rowData"></ViewModal>
  <!-- 部门内部管理及部门管理绑定角色弹框 -->
  <BindRole ref="bindRoleRef" :rowData="rowData" :deptId="deptId"></BindRole>
  <!-- 绑定工位弹框 -->
  <BindStationModal ref="BindStationModalRef" :rowId="rowId"></BindStationModal>
</template>
<script lang="tsx" setup>
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import type { DataRequestFn, FormProps, TableInstance, TableColumn } from '@bmos/components';
  import { BMTable, ModalFormInstance, BMModalForm } from '@bmos/components';
  import { reactive, ref, createVNode } from 'vue';
  import { Modal, message, Descriptions, DescriptionsItem, Button, Space } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import ViewModal from '@/pages/Permissions/userManagement/bulletBox/viewModal.vue';
  import BindRole from './components/BindRole.vue';
  import BindStationModal from './components/BindStationModal.vue';
  import AssignRoleModal from './components/AssignRole.vue';

  import {
    relateUserDelAll,
    deleteDepartment,
    relateUserData,
    assignPerson,
    relateUserDel,
    addDepartment,
    editDepartment,
    validateDept,
  } from '@/api/Permissions/departmentManagement';

  import { usePermissionStore } from '@/stores/permission';
  import { sso } from '@bmos/messager';
  const { getUserInfo } = sso;

  const { hasPermission } = usePermissionStore();
  const props = defineProps({
    deptId: {
      type: [Number, String],
      default: '',
    },
    departmentInfo: {
      type: Object,
      default: () => {},
    },
    childrenLength: {
      type: [Number, String],
      default: '0',
    },
    upDepartmentInfo: {
      type: Array,
      default: () => [],
    },
    upUpDepartmentInfo: {
      type: Array,
      default: () => [],
    },
    // 判断该部门是否有parent
    hasParent: {
      type: Boolean,
      default: false,
    },
    //parentCode
    parentCode: {
      type: String,
      default: '',
    },
    // 存佰墨思下面第一级的部门id
    firstDeptId: {
      type: String,
      default: '',
    },
    // 根节点名称
    rootNode: {
      type: String,
      default: '',
    },
    //'1'为部门内部管理,'2'为部门管理
    inside: {
      type: String,
      default: '2',
    },
    showTable: {
      type: [String, Number],
      default: 0,
    },
  });
  const emit = defineEmits(['assignPersonnel', 'getTreeData', 'showButton', 'getTreeData2']);

  const spaceSize = ref(16);
  const search = ref(false);
  const tableInstance = ref<TableInstance>();
  const open = ref<boolean | undefined>(false);
  const title = ref<String>('');
  const modalFormRef = ref<ModalFormInstance>();
  const viewModalRef = ref<any>();
  const rowData = ref<any>({});
  const bindRoleRef = ref<any>();
  const BindStationModalRef = ref<any>();
  const AssignRoleRef = ref<any>();
  const rowId = ref<any>();
  // 部门名称唯一验证
  const validatorUnique = async (_rule: any, value: string) => {
    if (!value) {
      return Promise.reject(t('请输入部门名称'));
    }
    if (!/^.{0,255}$/.test(value)) {
      return Promise.reject(t('部门名称长度不能超过255'));
    } else if (title.value == t('编辑部门')) {
      const res = await validateDept({
        deptName: value,
        id: props.deptId === 'all' ? undefined : props.deptId,
      });
      if (res.data == true) {
        return Promise.reject(t('部门名称不能重复!'));
      } else {
        return Promise.resolve();
      }
    }
    // 新增部门时校验部门是否存在-不传id:deptId
    else if (title.value == t('新增部门')) {
      const res = await validateDept({
        deptName: value,
      });
      if (res.data == true) {
        return Promise.reject(t('部门名称不能重复!'));
      } else {
        return Promise.resolve();
      }
    } else {
      return Promise.resolve();
    }
  };

  // 表格formProps
  const formProps = reactive<Partial<FormProps>>({
    actionColOptions: {
      span: 16,
    },
    showAdvancedButton: false,
  });
  // 新增编辑弹窗formProps
  const formProps2 = reactive<any>({
    initialValues: {
      //默认值
      // name: '',
      // test: '',
    },
    labelCol: { span: 5 },
    wrapperCol: { span: 18 },
    schemas: [
      {
        field: 'parentId',
        component: 'Select',
        label: t('上级部门'),
        required: true,
        componentProps: {
          options: [],
          disabled: true,
        },
      },
      {
        field: 'deptName',
        component: 'Input',
        label: t('部门名称'),
        rules: [{ required: true, validator: validatorUnique, trigger: 'blur' }],
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
        required: false,
        componentProps: {
          maxlength: 255,
        },
      },
    ],
  });
  const loadData: DataRequestFn = async (params: any) => {
    const data = {
      ...params,
      deptId: props.firstDeptId !== '' ? props.firstDeptId : props.deptId,
    };
    if (props.inside === '1') {
      if ((data.hiddenTable && data.hiddenTable === true) || props.showTable == 0) {
        //表格展示空
        return {
          data: {
            total: 0,
          },
        };
      } else {
        return relateUserData({ ...data, hiddenTable: undefined });
      }
    } else {
      return relateUserData(data);
    }
  };
  // 分配人员按钮
  const assignPersonnel = async () => {
    const data = { deptId: props.deptId, userName: '' };
    const res = await assignPerson(data);
    const treeData = res.data || [];
    let tempTreeData;
    if (res.data.length === 0) {
      // 如果没有对应数据,则不在树外层加全部
      tempTreeData = treeData;
    } else {
      tempTreeData = [
        {
          name: t('全部'),
          userId: '0',
          children: treeData,
        },
      ];
    }
    emit('assignPersonnel', tempTreeData);
  };

  // 新增
  const addOrEdit = (type: String) => {
    open.value = true;
    title.value = type == 'add' ? t('新增部门') : t('编辑部门');
    if (type == 'add') {
      formProps2.initialValues = {};
      if (props.upDepartmentInfo.length !== 0) {
        formProps2.schemas[0].componentProps.options = props.upDepartmentInfo;
        formProps2.initialValues.parentId = props.upDepartmentInfo[0].value;
      } else {
        let data = [{ label: props.rootNode, value: 'all' }];
        formProps2.schemas[0].componentProps.options = data;
        formProps2.initialValues.parentId = data[0].value;
      }
    } else if (type == 'edit') {
      formProps2.schemas[0].componentProps.options = props.upUpDepartmentInfo;
      formProps2.initialValues = {
        parentId: props.upUpDepartmentInfo[0].value,
        deptName: props.departmentInfo.name,
        remark: props.departmentInfo.remarks,
      };
    }
  };
  // 分配角色
  const assignRole = () => {
    AssignRoleRef.value.openModal();
  };
  // 重置新增编辑弹框
  const resetting = () => {
    formProps2.initialValues = {};
  };
  // 取消
  const cancel = () => {
    modalFormRef.value?.resetForm();
    resetting();
  };
  // 确定
  const ok = async () => {
    if (title.value == t('新增部门')) {
      try {
        const data: any = await modalFormRef.value?.validate();
        await addDepartment({
          ...data,
          parentCode: props.parentCode || '0',
          parentId: data.parentId === 'all' ? 0 : data.parentId,
        });
        message.success(t('新增成功'));
        const infoData = {
          up: props.departmentInfo.up,
          name: props.departmentInfo.name,
          remarks: props.departmentInfo.remarks,
        };
        // 重新渲染树和部门信息
        emit('getTreeData', infoData);
        open.value = false;
      } catch (error: any) {
        error.message && message.error(error.message);
      }
    } else if (title.value == t('编辑部门')) {
      try {
        let data = await modalFormRef.value?.validate();
        data = { ...data, id: props.deptId };
        await editDepartment(data);
        open.value = false;
        updata();
        const infoData = {
          up: props.departmentInfo.up,
          name: data.deptName,
          remarks: data.remark,
        };
        // 重新渲染树和部门信息
        emit('getTreeData', infoData);
        message.success(t('编辑成功'));
      } catch (error: any) {
        error.message && message.error(error.message);
      }
    }
  };
  // 删除部门
  const deletedepartment = () => {
    if (props.deptId == '' || !props.deptId) {
      return message.warning(t('请先选择部门'));
    }
    Modal.confirm({
      title: t('是否删除部门信息'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('部门信息删除后无法恢复，是否删除?'),
      okText: t('确认'),
      cancelText: t('取消'),
      onOk() {
        handleClickDelete();
      },
    });
  };
  // 删除部门确定(限制：所需删除的部门节点下无用户且无子部门)11.18改逻辑
  const handleClickDelete = async () => {
    const data = { id: props.deptId };
    try {
      const res: any = await deleteDepartment(data);
      if (res.code === 0) {
        if (props.inside === '2') {
          emit('getTreeData');
          // 刷新表格数据
          updata();
          emit('showButton', false);
          message.success('删除成功');
          return;
        }
        if (props.inside === '1') {
          emit('getTreeData2');
          return;
        }
      }
      Modal.confirm({
        title: t('无法删除部门信息'),
        icon: createVNode(ExclamationCircleOutlined),
        closable: true,
        content: t(res.message),
        okText: t('确认'),
        cancelText: t('取消'),
        onOk() {},
      });
    } catch (error) {
      Modal.confirm({
        title: t('无法删除部门信息'),
        icon: createVNode(ExclamationCircleOutlined),
        closable: true,
        content: t('该部门下还有用户或子部门信息'),
        okText: t('确认'),
        cancelText: t('取消'),
        onOk() {},
      });
    }
  };
  // 刷新表格数据(不带参数)
  const updata = () => {
    tableInstance.value?.fetchData();
  };
  // 刷新表格数据(带参数)
  const upDepartmentdata = () => {
    tableInstance.value?.fetchData();
  };
  // 全部移除(部门人员)
  const removeAll = () => {
    Modal.confirm({
      title: t('是否删除所有人员信息'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('删除后所有人员将不再是该部门的直属员工'),
      okText: t('确认'),
      cancelText: t('取消'),
      onOk() {
        handleRemoveAll();
      },
    });
  };
  const handleRemoveAll = async () => {
    if (tableInstance.value?.tableData.length == 0) {
      return Modal.confirm({
        title: t('提示'),
        icon: createVNode(ExclamationCircleOutlined),
        closable: true,
        content: t('该部门下已无人员'),
        okText: t('确认'),
        cancelText: t('取消'),
        onOk() {},
      });
    }
    const temp = tableInstance.value?.tableData?.map((item: any) => item.userId);
    const data = { deptId: props.deptId };
    await relateUserDelAll(data);
    // 重新渲染树和部门信息
    message.success(t('全部移除成功'));
    if (props.inside === '1' && temp?.includes(getUserInfo().userId)) {
      emit('getTreeData2');
    } else {
      updata();
    }
  };
  defineExpose({
    updata,
    upDepartmentdata,
    loadData,
    tableInstance,
    formProps2,
  });

  // 数据项类型
  const columns: TableColumn[] = [
    {
      title: t('用户名称'),
      dataIndex: 'userName',
      hideInSearch: true,
      resizable: true,
      formItemProps: {
        defaultValue: '',
        required: false,
      },
    },
    {
      title: t('用户账号'),
      dataIndex: 'loginName',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('性别'),
      align: 'left',
      dataIndex: 'gender',
      hideInSearch: true,
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
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      hideInSearch: true,
      width: 200,
      key: 'ACTION',
      resizable: true,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow:
            props.hasParent &&
            (props.inside === '1' ? hasPermission('100030006000009') : hasPermission('100030002000009')),
          onClick: () => {
            rowData.value = record;
            viewModalRef.value.openModal();
          },
        },
        {
          label: t('绑定角色'),
          ifShow:
            props.hasParent &&
            (props.inside === '1' ? hasPermission('100030006000007') : hasPermission('100030002000007')),
          onClick: async () => {
            rowData.value = record;
            bindRoleRef.value.openModal();
          },
        },
        {
          label: t('绑定工位'),
          ifShow:
            props.hasParent &&
            (props.inside === '1' ? hasPermission('100030006000008') : hasPermission('100030002000008')),
          onClick: () => {
            rowId.value = record.userId;
            BindStationModalRef.value.openModal();
          },
        },
        {
          label: t('移除'),
          danger: true,
          ifShow:
            props.hasParent && props.inside === '1'
              ? hasPermission('100030006000005')
              : hasPermission('100030002000005'),

          onClick: () => {
            remove(record);
          },
        },
      ],
    },
  ];
  // 单个移除
  const remove = (val: any) => {
    Modal.confirm({
      title: t('是否删除人员信息'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('删除后该人员将不再是部门的直属员工'),
      okText: t('确认'),
      cancelText: t('取消'),
      onOk() {
        handleRemove(val);
      },
    });
  };
  const handleRemove = async (row: any) => {
    const data = { userId: row.userId, deptId: props.deptId };
    await relateUserDel(data); //单个移除
    message.success(t('移除成功'));
    if (props.inside === '1' && row.userId === getUserInfo().userId) {
      //自己删自己
      emit('getTreeData2');
    } else {
      updata();
    }
  };
</script>
<style scoped lang="less">
  .tableData {
    padding: 16px;
    display: flex;
    flex-direction: column;
    height: 100%;
    .table {
      flex: 1;
      overflow-y: hidden;
    }
  }
  :deep(.plat-descriptions .plat-descriptions-view) {
    border-radius: 0px;
  }
  :deep(.plat-descriptions .plat-descriptions-header) {
    padding-left: 15px;
  }

  :deep(.bmos-table .bmos-tool-bar-title) {
    font-weight: 700;
    color: #18191a;
  }
  :deep(.bmos-tool-bar) {
    padding: 16px 0px 16px 16px;
  }

  :deep(.plat-divider-horizontal) {
    margin: 18px 0 0 0;
  }
</style>
