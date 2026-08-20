<!-- 楼宇管理 -->
<template>
  <BMPageComponent
    ref="tableInstance"
    :treeData="treeData"
    :titles="[t('楼层列表')]"
    :showAllAddIcon="hasPermission('200020001000001')"
    :showAction="hasPermission('200020001000001') || hasPermission('200020001000002')"
    :fieldNames="{
      title: 'showName',
      key: 'id',
    }"
    :actionList="actionList"
    :selectedKeys="treeSelectedKeys"
    :isSelects="[false, false]"
    :rowSelections="rowSelections"
    :rowKeys="['id']"
    :treeField="{
      field: {
        tenementId: 'id',
      },
    }"
    :default-selected-keys="['all']"
    :requests="[loadData as any]"
    :columns="[columns]"
    :search="[true]"
    :formProps="[
      {
        actionColOptions: {
          // span: 12,
        },
        showAdvancedButton: false,
      },
      {},
    ]"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <Button v-hasAuth="200020001000004" type="primary" @click="handleFloor(treeNode, 'add')">
        {{ t('新增楼层') }}
      </Button>
    </template>
  </BMPageComponent>
  <!-- 树新增编辑弹框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="treeOpen"
    :title="treeTitle"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    @okModal="okModal"></BMModalForm>
  <!-- 新增编楼层辑弹框 -->
  <BMModalForm
    ref="floorModalFormRef"
    v-model:open="floorOpen"
    :title="floorTitle"
    :formProps="floorFormProps"
    wrapClassName="modalSizeMedium"
    @okModal="floorOkModal"></BMModalForm>
</template>

<script setup lang="tsx">
  import { ref, onMounted, createVNode } from 'vue';
  import { message, Button, Modal, Switch } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import type { TableColumn, ModalFormType, Recordable, TableActionType } from '@bmos/components';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { BMModalForm, BMPageComponent } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';
  import {
    reqTenementTree, //获取树数据
    reqTenementSave, //树新增
    reqTenementUpdate, //树修改
    reqTenementDelete, //树删除
    reqTenementFloorEnable, //启停楼层
    reqTenementFloorPage, //获取表格数据
    reqTenementFloorSave, //楼层新增
    reqTenementFloorUpdate, //楼层修改
  } from '@/services';
  const { hasPermission } = usePermissionStore();

  const tableInstance = ref<any>();
  const treeOpen = ref<boolean>(false);
  const floorOpen = ref<boolean>(false);
  const treeSelectedKeys = ref<string[]>(['all']);
  const modalFormRef = ref<any>();
  const floorModalFormRef = ref<any>();
  const treeTitle = ref<string>(t('新建楼栋'));
  const floorTitle = ref<string>(t('新增楼层'));

  const treeData = ref<any[]>([]);
  const formData = ref<any>();
  const type = ref<string>('');
  // 多选
  const rowSelections = reactive([null, null]);
  // 行数据
  const formProps = reactive<any>({
    initialValues: {},
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('楼栋名称'),
        required: true,
      },
      {
        field: 'code',
        component: 'Input',
        label: t('楼栋编码'),
        required: true,
        componentProps: {
          disabled: false,
        },
      },
    ],
  });
  // 楼层数据
  const floorFormProps = reactive<any>({
    initialValues: {},
    schemas: [
      {
        field: 'tenementId',
        component: 'TreeSelect',
        label: t('所属楼栋'),
        required: true,
        componentProps: () => {
          return {
            disabled: type.value === 'edit' ? true : false,
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            treeData: treeData.value,
          };
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('楼层名称'),
        required: true,
      },
      {
        field: 'code',
        component: 'Input',
        label: t('楼层编码'),
        required: true,
        componentProps: {
          disabled: false,
        },
      },
    ],
  });
  const actionList = [
    {
      title: t('编辑楼栋'),
      action: 'editNode',
      ifShow: () => {
        return hasPermission('200020001000002');
      },
    },
    {
      title: t('删除楼栋'),
      action: 'deleteNode',
      ifShow: () => {
        return hasPermission('200020001000003');
      },
    },
  ];
  // 表格项
  const columns: TableColumn[] = [
    {
      title: t('楼层名称'),
      dataIndex: 'name',
      resizable: true,
    },
    {
      title: t('楼层编码'),
      dataIndex: 'code',
      resizable: true,
    },
    {
      title: t('操作人'),
      dataIndex: 'createByName',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('操作时间'),
      dataIndex: 'createTime',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('启停状态'),
      align: 'left',
      dataIndex: 'status',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: () => ({
          options: [
            {
              label: t('启用'),
              value: true,
            },
            {
              label: t('停用'),
              value: false,
            },
          ],
        }),
      },
    },
    {
      title: t('启停'),
      dataIndex: 'status',
      fixed: 'right',
      width: 76,
      resizable: true,
      hideInSearch: true,
      customRender: (col: any) => {
        const { record, tableAction } = col;
        const enable = record?.status.value === 'ENABLE';
        return (
          <Switch
            v-hasAuth='200020001000006'
            checked={enable}
            onChange={checked => {
              changeState(record, checked as boolean, tableAction);
            }}
          />
        );
      },
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
          label: t('编辑'),
          ifShow: hasPermission('200020001000005'),
          onClick: () => {
            handleFloor(record, 'edit');
          },
        },
      ],
    },
  ];
  // 获取表格数据
  const loadData = async (params: any) => {
    const { moduleId }: any = params;
    if (moduleId === 'all') {
      delete params.moduleId;
    }
    return await reqTenementFloorPage(params as any);
  };
  // 新增树节点
  const addItem = (_node: any) => {
    treeTitle.value = t('新增楼栋');
    formProps.initialValues = {};
    treeOpen.value = true;
  };
  // 编辑树节点
  const editNodeFn = (node: any) => {
    treeTitle.value = t('编辑楼栋');
    formProps.initialValues = {
      id: node.id,
      name: node.name,
      code: node.code,
    };
    treeOpen.value = true;
  };
  // 删除树节点
  const deleteNodeFn = (node: any) => {
    Modal.confirm({
      title: t('是否删除该楼栋'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('楼栋删除后无法恢复，是否删除'),
      okText: t('确认'),
      cancelText: t('取消'),
      onOk: async () => {
        try {
          await reqTenementDelete(node.id);
          message.success(t('删除成功'));
          getTreeData();
          if (node.selected) {
            treeSelectedKeys.value = ['all'];
          }
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
    });
  };
  //树点击事件
  const handleTreeAction = (action: any, node: any) => {
    if (action.action === 'ADD') {
      addItem(node);
    }
    if (action.action === 'editNode') {
      editNodeFn(node);
    }
    if (action.action === 'deleteNode') {
      deleteNodeFn(node);
    }
  };
  // 弹框确定
  const okModal = (instance: ModalFormType) => {
    instance.validate().then(async params => {
      const data: any = { ...params };
      try {
        if (data.id) {
          // 编辑保存
          const { id, name, code } = data;
          const params2 = {
            id,
            name,
            code,
          };
          await reqTenementUpdate(params2 as any);
        } else {
          // 新增保存
          const data2 = {
            ...data,
          };
          await reqTenementSave(data2);
        }
        message.success(t(data.id ? t('编辑成功') : t('新增成功')));
        treeOpen.value = false;
        await getTreeData();
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };
  // 楼层弹窗确定
  const floorOkModal = (instance: ModalFormType) => {
    instance.validate().then(async params => {
      const data: any = { ...params };
      try {
        if (data.id) {
          // 编辑保存
          console.log(data);
          const { id, name, code, tenementId } = data;
          const params2 = {
            id,
            name,
            code,
            tenementId,
          };
          await reqTenementFloorUpdate(params2 as any);
        } else {
          // 新增保存
          const data2 = {
            ...data,
            parentId: formData.value.id,
          };
          await reqTenementFloorSave(data2);
        }
        message.success(t(data.id ? t('编辑成功') : t('新增成功')));
        floorOpen.value = false;
        updateTable();
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };
  // 操作楼层
  const handleFloor = (formData1: any, type1: any) => {
    formData.value = formData1;
    type.value = type1;
    floorTitle.value = type1 === 'add' ? t('新增楼层') : t('编辑楼层');
    floorFormProps.initialValues =
      type1 === 'add'
        ? {
            tenementId: formData1.id,
          }
        : formData1;
    floorOpen.value = true;
  };
  // 启停
  const changeState = async (record: Recordable, checked: boolean, tableAction: TableActionType) => {
    const title = checked ? t('是否启用此楼层') : t('是否停用此楼层');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}`,
      onOk: async () => {
        try {
          await reqTenementFloorEnable({
            id: record.id,
          });
          message.success(checked ? t('启用成功') : t('停用成功'));
          tableAction.fetchData();
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
      onCancel() {},
    });
  };
  //刷新列表
  const updateTable = () => {
    tableInstance.value?.fetchData();
  };
  //获取所有节点
  const getTreeData = async () => {
    try {
      const res: any = await reqTenementTree();
      const data = (res.data || []).map(item => {
        return {
          ...item,
          showName: `${item.code}-${item.name}`,
        };
      });
      treeData.value = [
        {
          name: t('全部'),
          showName: t('全部'),
          key: 'all',
          id: 'all',
          children: data,
        },
      ];
    } catch (error) {
      console.log(error);
    }
  };
  onMounted(async () => {
    await getTreeData();
  });
</script>

<style scoped></style>
