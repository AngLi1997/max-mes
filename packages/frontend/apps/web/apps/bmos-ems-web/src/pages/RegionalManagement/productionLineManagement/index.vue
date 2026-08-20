<!-- 产线管理 -->
<template>
  <BMPageComponent
    ref="tableInstance"
    :treeData="treeData"
    :titles="[t('产线列表')]"
    :showAction="
      hasPermission('160030001000008') || hasPermission('160030001000002') || hasPermission('160030001000003')
    "
    :fieldNames="{
      title: 'showName',
      key: 'id',
    }"
    :actionList="actionList"
    :selectedKeys="treeSelectedKeys"
    :rowKeys="['userId']"
    :treeField="{
      field: {
        moduleId: 'id',
      },
    }"
    :default-selected-keys="['all']"
    :requests="[loadData as any]"
    :columns="[columns]"
    :search="[true]"
    :formProps="[
      {
        actionColOptions: {
          span: 6,
        },
        showAdvancedButton: false,
      },
      {},
    ]"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <!-- 新增编辑-产线弹框 -->
      <ProductionLineModal
        ref="ProductionLineModalRef"
        :rowId="rowId"
        :type="type"
        :formData="formData"
        @updateTable="updateTable"></ProductionLineModal>
      <!-- 查看-产线弹框 -->
      <ViewModal ref="ViewModalRef" :rowId="rowId"></ViewModal>
      <!-- 绑定房间弹框 -->
      <BindRoomModal
        ref="BindRoomModalRef"
        :roomIdList="roomIdList"
        :rowId="rowId"
        @updateTable="updateTable"></BindRoomModal>
      <!-- 绑定工位弹框 -->
      <BindStationModal
        ref="BindStationModalRef"
        :rowId="rowId"
        :stationIdList="stationIdList"
        @updateTable="updateTable"></BindStationModal>
      <PermissionModal
        v-model:permissionOpen="permissionModalOpen"
        :processId="firstRowData?.id"
        @ok="savePermission" />
      <Button v-hasAuth="160030001000004" type="primary" @click="handleProductionLine(treeNode, 'add')">
        {{ t('新增产线') }}
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
</template>

<script setup lang="tsx">
  import { ref, onMounted, createVNode } from 'vue';
  import { message, Button, Modal, Switch } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import type { TableColumn, ModalFormType, Recordable, TableActionType } from '@bmos/components';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { BMModalForm, BMPageComponent } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';
  import ViewModal from './components/ViewModal.vue';
  import ProductionLineModal from './components/ProductionLineModal.vue';
  import BindRoomModal from './components/BindRoomModal.vue';
  import BindStationModal from './components/BindStationModal.vue';
  import PermissionModal from '../roomManagement/components/PermissionModal.vue';

  import { loopTree2 } from './utils';

  import {
    reqLineModuleTreeList, //获取树数据
    reqLineModuleSave, //树新增
    reqLineModuleUpdate, //树修改
    reqLineModuleDelete, //树删除
    reqFactoryLineDelete, //删除产线
    reqFactoryLineEnable, //启停产线
    reqFactoryLinePage, //获取表格数据
  } from '@/services';
  const { hasPermission } = usePermissionStore();
  const tableInstance = ref<any>();
  const treeOpen = ref<boolean>(false);
  const treeSelectedKeys = ref<string[]>(['all']);
  const modalFormRef = ref<any>();
  const ViewModalRef = ref<any>();
  const ProductionLineModalRef = ref<any>();
  const BindRoomModalRef = ref<any>();
  const BindStationModalRef = ref<any>();
  const treeTitle = ref<string>(t('新增分类'));
  const rowId = ref<any>();
  const treeData = ref<any[]>([]);
  const formData = ref<any>();
  const roomIdList = ref<any>(); //绑定房间lists
  const stationIdList = ref<any>(); //绑定工位lists
  const type = ref<string>('');
  const permissionModalOpen = ref<boolean>(false); // 数据权限modal
  const firstRowData = ref<any>({});
  const formProps = reactive<any>({
    initialValues: {
      parentId: 'all',
    },
    schemas: [
      {
        field: 'parentId',
        component: 'TreeSelect',
        label: t('上级分类'),
        required: true,
        componentProps: {
          disabled: true,
          fieldNames: {
            label: 'showName',
            value: 'id',
          },
          request: async () => {
            const res: any = await reqLineModuleTreeList();
            return [
              {
                name: t('全部'),
                showName: t('全部'),
                key: 'all',
                id: 'all',
                children: loopTree2(res.data),
              },
            ];
          },
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('分类名称'),
        required: true,
      },
      {
        field: 'code',
        component: 'Input',
        label: t('分类编码'),
        required: true,
        componentProps: {
          disabled: false,
        },
      },
    ],
  });
  const actionList = [
    {
      title: t('新增子分类'),
      action: 'addChildren',
      ifShow: () => {
        return hasPermission('160030001000001');
      },
    },
    {
      title: t('编辑分类'),
      action: 'editNode',
      ifShow: () => {
        return hasPermission('160030001000002');
      },
    },
    {
      title: t('删除分类'),
      action: 'deleteNode',
      ifShow: () => {
        return hasPermission('160030001000003');
      },
    },
  ];
  // 表格项
  const columns: TableColumn[] = [
    {
      title: t('产线编码'),
      dataIndex: 'code',
      resizable: true,
      sorter: true,
      formItemProps: {
        defaultValue: '',
        required: false,
      },
    },
    {
      title: t('产线名称'),
      dataIndex: 'name',
      resizable: true,
    },
    {
      title: t('描述'),
      dataIndex: 'description',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('更新人'),
      dataIndex: 'operator',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('更新时间'),
      dataIndex: 'operateTime',
      hideInSearch: true,
      resizable: true,
      sorter: true,
    },
    {
      title: t('启停状态'),
      align: 'left',
      dataIndex: 'enable',
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
      dataIndex: 'enable',
      fixed: 'right',
      width: 76,
      resizable: true,
      hideInSearch: true,
      customRender: (col: any) => {
        const { record, tableAction } = col;
        const enable = record?.enable || 'false';
        return (
          <Switch
            v-hasAuth='160030001000010'
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
      actions: ({ record }, tableAction) => [
        {
          label: t('查看'),
          ifShow: hasPermission('160030001000006'),
          onClick: () => {
            rowId.value = record.id;
            ViewModalRef.value.openModal();
          },
        },
        {
          label: t('编辑'),
          ifShow: !record.enable && hasPermission('160030001000005'),
          onClick: () => {
            handleProductionLine(record, 'edit');
          },
        },
        {
          label: t('绑定房间'),
          ifShow: hasPermission('160030001000007'),
          onClick: () => {
            rowId.value = record.id;
            roomIdList.value = record?.roomIdList || [];
            BindRoomModalRef.value.openModal();
          },
        },
        {
          label: t('绑定工位'),
          ifShow: hasPermission('160030001000008'),
          onClick: () => {
            rowId.value = record.id;
            stationIdList.value = record?.stationIdList || [];
            BindStationModalRef.value.openModal();
          },
        },
        {
          label: t('数据权限'),
          ifShow: hasPermission('160030001000011'),
          onClick: () => {
            dataPermissions(record);
          },
        },
        {
          label: t('删除'),
          ifShow: !record.enable && hasPermission('160030001000009'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('删除确认'),
              icon: h(ExclamationCircleOutlined),
              closable: true,
              content: t('是否删除该产线'),
              onOk: async () => {
                try {
                  await reqFactoryLineDelete(record.id);
                  message.success(t('删除成功'));
                  tableAction.fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                }
              },
            });
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
    return await reqFactoryLinePage(params as any);
  };
  // 新增树节点
  const addItem = (node: any) => {
    treeTitle.value = t('新增分类');
    formProps.initialValues = {
      parentId: node?.id || 'all',
    };
    formProps.schemas[2].componentProps.disabled = false;
    treeOpen.value = true;
  };
  // 编辑树节点
  const editNodeFn = (node: any) => {
    treeTitle.value = t('编辑分类');
    formProps.initialValues = {
      parentId: node.parentId === '0' ? 'all' : node.parentId,
      id: node.id,
      name: node.name,
      code: node.code,
    };
    formProps.schemas[2].componentProps.disabled = true;
    treeOpen.value = true;
  };
  // 删除树节点
  const deleteNodeFn = (node: any) => {
    Modal.confirm({
      title: t('是否删除该信息'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('信息删除后无法恢复，是否删除'),
      okText: t('确认'),
      cancelText: t('取消'),
      onOk: async () => {
        try {
          await reqLineModuleDelete(node.id);
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
    if (action.action === 'addChildren') {
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
      data.parentId = data.parentId === 'all' ? '0' : data.parentId;
      try {
        if (data.id) {
          // 编辑保存
          const { id, name } = data;
          const params2 = {
            id,
            name,
          };
          await reqLineModuleUpdate(params2 as any);
        } else {
          // 新增保存
          const data2 = {
            ...data,
          };
          await reqLineModuleSave(data2);
        }
        message.success(t(data.id ? t('编辑成功') : t('新增成功')));
        treeOpen.value = false;
        await getTreeData();
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };
  // 操作产线
  const handleProductionLine = (formData1: any, type1: any) => {
    formData.value = formData1;
    type.value = type1;
    ProductionLineModalRef.value.openModal();
  };
  // 启停
  const changeState = async (record: Recordable, checked: boolean, tableAction: TableActionType) => {
    const title = checked ? t('是否启用此产线') : t('是否停用此产线');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}`,
      onOk: async () => {
        try {
          await reqFactoryLineEnable({
            id: record.id,
            enable: checked ? true : false,
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
  // 数据权限按钮
  const dataPermissions = (record: any) => {
    permissionModalOpen.value = true;
    firstRowData.value = record;
  };
  // 保存数据权限之后刷新第一个表格
  const savePermission = async () => {
    updateTable();
  };
  //刷新列表
  const updateTable = () => {
    tableInstance.value?.fetchData();
  };
  //获取所有节点
  const getTreeData = async () => {
    try {
      const res: any = await reqLineModuleTreeList();
      treeData.value = [
        {
          name: t('全部'),
          showName: t('全部'),
          key: 'all',
          id: 'all',
          children: loopTree2(res.data),
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
