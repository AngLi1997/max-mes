<!-- 称量中心 -->
<template>
  <BMPageComponent
    ref="tableInstance"
    :treeData="treeData"
    :titles="[t('称量中心')]"
    :showAction="
      hasPermission('120020012000001') || hasPermission('120020012000002') || hasPermission('120020012000003')
    "
    :fieldNames="{
      title: 'showName',
      key: 'id',
    }"
    :actionList="actionList"
    :selectedKeys="treeSelectedKeys"
    :rowKeys="['id']"
    :treeField="{
      field: {
        categoryId: 'id',
      },
    }"
    :default-selected-keys="['all']"
    :requests="[loadData as any]"
    :columns="[columns]"
    :search="[true]"
    :formProps="[
      {
        actionColOptions: {
          span: 12,
        },
        showAdvancedButton: false,
      },
      {},
    ]"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <!-- 新增编辑查看-称量中心弹框 -->
      <WeighingCenterModal
        ref="WeighingCenterModalRef"
        :rowId="rowId"
        :type="type"
        :formData="formData"
        @updateTable="updateTable"></WeighingCenterModal>
      <!-- 待加数据权限弹框 -->
      <PermissionModal
        v-model:permissionOpen="permissionModalOpen"
        :processId="firstRowData?.id"
        @ok="savePermission" />
      <!-- 绑定工位弹框 -->
      <BindStationModal
        ref="BindStationModalRef"
        :rowId="rowId"
        :stationIdList="stationIdList"
        @updateTable="updateTable"></BindStationModal>
      <Button v-hasAuth="120020012000004" type="primary" @click="handleWeighingCenter(treeNode, 'add')">
        {{ t('新增') }}
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
  import WeighingCenterModal from './components/WeighingCenterModal.vue';
  import BindStationModal from './components/BindStationModal.vue';
  import PermissionModal from './components/PermissionModal.vue';
  import { loopTree } from './utils';
  import {
    reqWeighCentreCategoryTree, //获取树数据
    reqWeighCentreCategoryCreate, //树新增
    reqWeighCentreCategoryEdit, //树修改
    reqWeighCentreCategoryDelete, //树删除
    reqWeighCentreDelete, //删除称量中心
    reqWeighCentreEnable, //启用称量中心
    reqWeighCentreDisable, //停用称量中心
    reqWeighCentreQueryPage, //获取表格数据
  } from '@/services';
  const { hasPermission } = usePermissionStore();

  const tableInstance = ref<any>();
  const treeOpen = ref<boolean>(false);
  const treeSelectedKeys = ref<string[]>(['all']);
  const modalFormRef = ref<any>();
  const WeighingCenterModalRef = ref<any>();
  const BindStationModalRef = ref<any>();
  const treeTitle = ref<string>(t('新建分类'));
  const rowId = ref<any>();
  const treeData = ref<any[]>([]);
  const formData = ref<any>();
  const stationIdList = ref<any>(); //绑定工位lists
  const type = ref<string>('');
  const permissionModalOpen = ref<boolean>(false); // 数据权限modal
  // 行数据
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
            const res: any = await reqWeighCentreCategoryTree();
            return [
              {
                name: t('全部'),
                showName: t('全部'),
                key: 'all',
                id: 'all',
                children: loopTree(res.data),
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
    ],
  });
  const actionList = [
    {
      title: t('新建分类'),
      action: 'addChildren',
      ifShow: () => {
        return hasPermission('120020012000001');
      },
    },
    {
      title: t('编辑分类'),
      action: 'editNode',
      ifShow: () => {
        return hasPermission('120020012000002');
      },
    },
    {
      title: t('删除分类'),
      action: 'deleteNode',
      ifShow: () => {
        return hasPermission('120020012000003');
      },
    },
  ];
  // 表格项
  const columns: TableColumn[] = [
    {
      title: t('称量中心'),
      dataIndex: 'name',
      resizable: true,
    },
    {
      title: t('编码'),
      dataIndex: 'code',
      resizable: true,
    },
    {
      title: t('所属分类'),
      dataIndex: 'categoryNamePath',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('启停'),
      dataIndex: 'enabled',
      width: 76,
      resizable: true,
      hideInSearch: true,
      customRender: (col: any) => {
        const { record, tableAction } = col;
        const enabled = record?.enabled || 'false';
        return (
          <Switch
            v-hasAuth='120020012000009'
            checked={enabled}
            onChange={checked => {
              changeState(record, checked as boolean, tableAction);
            }}
          />
        );
      },
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      hideInSearch: true,
      width: 200,
      resizable: true,
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      hideInSearch: true,
      width: 280,
      resizable: true,
      key: 'ACTION',
      actions: ({ record }, tableAction) => [
        {
          label: t('查看'),
          ifShow: record.enabled && hasPermission('120020012000006'),
          onClick: () => {
            handleWeighingCenter(record, 'look');
          },
        },
        {
          label: t('编辑'),
          ifShow: !record.enabled && hasPermission('120020012000005'),
          onClick: () => {
            handleWeighingCenter(record, 'edit');
          },
        },
        {
          label: t('数据权限'),
          ifShow: hasPermission('120020012000007'),
          onClick: () => {
            dataPermissions(record);
          },
        },
        {
          label: t('绑定工位'),
          ifShow: hasPermission('120020012000008'),
          onClick: () => {
            rowId.value = record.id;
            stationIdList.value = record?.stationIdList || [];
            BindStationModalRef.value.openModal();
          },
        },
        {
          label: t('删除'),
          ifShow: !record.enabled && hasPermission('120020012000010'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('删除确认'),
              icon: h(ExclamationCircleOutlined),
              closable: true,
              content: t('是否删除该称量中心'),
              onOk: async () => {
                try {
                  await reqWeighCentreDelete({ id: record.id });
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
    const { categoryId }: any = params;
    if (categoryId === 'all') {
      delete params.categoryId;
    }
    return await reqWeighCentreQueryPage(params as any);
  };
  // 新增树节点
  const addItem = (node: any) => {
    treeTitle.value = t('新建分类');
    formProps.initialValues = {
      parentId: node?.id || 'all',
    };
    treeOpen.value = true;
  };
  // 编辑树节点
  const editNodeFn = (node: any) => {
    treeTitle.value = t('编辑分类');
    formProps.initialValues = {
      parentId: !node.parentId ? 'all' : node.parentId,
      id: node.id,
      name: node.name,
    };
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
          await reqWeighCentreCategoryDelete({ id: node.id });
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
      data.parentId = data.parentId === 'all' ? undefined : data.parentId;
      try {
        if (data.id) {
          // 编辑保存
          const { id, name } = data;
          const params2 = {
            id,
            name,
          };
          await reqWeighCentreCategoryEdit(params2 as any);
        } else {
          // 新增保存
          const data2 = {
            ...data,
          };
          await reqWeighCentreCategoryCreate(data2);
        }
        message.success(t(data.id ? t('编辑成功') : t('新增成功')));
        treeOpen.value = false;
        await getTreeData();
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };
  // 操作称量中心
  const handleWeighingCenter = (formData1: any, type1: any) => {
    formData.value = formData1;
    type.value = type1;
    WeighingCenterModalRef.value.openModal();
  };
  // 启停
  const changeState = async (record: Recordable, checked: boolean, tableAction: TableActionType) => {
    const title = checked ? t('是否启用此称量中心') : t('是否停用此称量中心');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}`,
      onOk: async () => {
        try {
          checked
            ? await reqWeighCentreEnable({
                id: record.id,
              })
            : await reqWeighCentreDisable({ id: record.id });
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
      const res: any = await reqWeighCentreCategoryTree();
      treeData.value = [
        {
          name: t('全部'),
          showName: t('全部'),
          key: 'all',
          id: 'all',
          children: loopTree(res.data),
        },
      ];
    } catch (error) {}
  };
  onMounted(async () => {
    await getTreeData();
  });
</script>

<style scoped></style>
