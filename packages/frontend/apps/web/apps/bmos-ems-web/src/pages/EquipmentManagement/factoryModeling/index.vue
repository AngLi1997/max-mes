<!-- 工厂建模 -->
<template>
  <BMPageComponent
    ref="tableInstance"
    :treeData="treeData"
    :titles="[t('工位列表')]"
    :fieldNames="{
      title: 'showName',
      key: 'id',
    }"
    :actionList="actionList"
    :showAction="
      hasPermission('160010001000001') || hasPermission('160010001000002') || hasPermission('160010001000003')
    "
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
          span: 12,
        },
        showAdvancedButton: false,
      },
      {},
    ]"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <!-- 新增编辑-工位弹框 -->
      <WorkstationModal
        ref="WorkstationModalRef"
        :rowId="rowId"
        :type="type"
        :formData="formData"
        @updateTable="updateTable"></WorkstationModal>
      <!-- 查看-工位弹框 -->
      <ViewModal ref="ViewModalRef" :rowId="rowId"></ViewModal>
      <!-- 绑定设备弹框 -->
      <BindEquipmentModal
        ref="BindEquipmentModalRef"
        :equipmentIdList="equipmentIdList"
        :rowId="rowId"
        @updateTable="updateTable"></BindEquipmentModal>
      <!-- 绑定人员弹框 -->
      <BindPersonModal
        ref="BindPersonModalRef"
        :rowId="rowId"
        :rootNode="rootNode"
        :userIdList="userIdList"
        @updateTable="updateTable"></BindPersonModal>
      <Button v-hasAuth="160010001000004" type="primary" @click="handleWorkstation(treeNode, 'add')">
        {{ t('新增工位') }}
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
  import { BMModalForm, BMPageComponent, ActionListItemCustomRenderParams } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';
  import ViewModal from './components/ViewModal.vue';
  import WorkstationModal from './components/WorkstationModal.vue';
  import BindEquipmentModal from './components/BindEquipmentModal.vue';
  import BindPersonModal from './components/BindPersonModal.vue';
  import { loopTree2 } from './utils';

  import {
    reqEquipmentModuleList, //获取树数据
    reqEquipmentModuleSave, //树新增
    reqEquipmentModuleUpdate, //树修改
    reqEquipmentModuleDelete, //树删除
    reqEquipmentStationDelete, //删除设备工位
    reqEquipmentStationEnable, //启停设备工位
    getParameter,
    reqEquipmentStationPage, //获取表格数据
  } from '@/services';
  const { hasPermission } = usePermissionStore();
  const tableInstance = ref<any>();
  const treeOpen = ref<boolean>(false);
  const treeSelectedKeys = ref<string[]>(['all']);
  const modalFormRef = ref<any>();
  const ViewModalRef = ref<any>();
  const WorkstationModalRef = ref<any>();
  const BindEquipmentModalRef = ref<any>();
  const BindPersonModalRef = ref<any>();
  const treeTitle = ref<string>(t('新建子模型'));
  const rowId = ref<any>();
  const treeData = ref<any[]>([]);
  const formData = ref<any>();
  const equipmentIdList = ref<any>(); //绑定设备lists
  const userIdList = ref<any>(); //绑定人员lists
  const type = ref<string>('');
  const rootNode = ref(''); // 参数配置配的部门根节点名称
  const levelList = ref<any>([t('企业'), t('工厂'), t('产线'), t('房间')]);
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
            const res: any = await reqEquipmentModuleList();
            return [
              {
                name: t('全部'),
                showName: t('全部'),
                key: 'all',
                id: 'all',
                // disabled: true,
                children: loopTree2(res.data),
                // children: res.data,
              },
            ];
          },
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('模型名称'),
        required: true,
      },
      {
        field: 'code',
        component: 'Input',
        label: t('模型编码'),
        required: true,
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'type',
        component: 'Input',
        label: t('模型类型'),
        required: true,
        componentProps: {
          disabled: true,
        },
      },
    ],
  });
  const actionList = [
    {
      title: t('新建子模型'),
      action: 'addChildren',
      ifShow: (node: ActionListItemCustomRenderParams) => {
        return node.nodeLevelInTree < 5 && hasPermission('160010001000001');
      },
    },
    {
      title: t('编辑模型'),
      action: 'editNode',
      ifShow: () => {
        return hasPermission('160010001000002');
      },
    },
    {
      title: t('删除模型'),
      action: 'deleteNode',
      ifShow: () => {
        return hasPermission('160010001000003');
      },
    },
  ];
  // 表格项
  const columns: TableColumn[] = [
    {
      title: t('工位编码'),
      dataIndex: 'code',
      hideInSearch: true,
      resizable: true,
      formItemProps: {
        defaultValue: '',
        required: false,
      },
    },
    {
      title: t('工位名称'),
      dataIndex: 'name',
      resizable: true,
    },
    {
      title: t('所属模型'),
      dataIndex: 'moduleName',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('描述'),
      dataIndex: 'description',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('状态'),
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
            v-hasAuth='160010001000010'
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
      width: 220,
      key: 'ACTION',
      resizable: true,
      actions: ({ record }, tableAction) => [
        {
          label: t('查看'),
          ifShow: record.enable && hasPermission('160010001000006'),
          onClick: () => {
            rowId.value = record.id;
            ViewModalRef.value.openModal();
          },
        },
        {
          label: t('编辑'),
          ifShow: !record.enable && hasPermission('160010001000005'),
          onClick: () => {
            handleWorkstation(record, 'edit');
          },
        },
        {
          label: t('绑定设备'),
          ifShow: hasPermission('160010001000007'),
          onClick: () => {
            rowId.value = record.id;
            equipmentIdList.value = record?.equipmentIdList || [];
            BindEquipmentModalRef.value.openModal();
          },
        },
        {
          label: t('绑定人员'),
          ifShow: hasPermission('160010001000008'),
          onClick: () => {
            rowId.value = record.id;
            userIdList.value = record?.userIdList || [];
            BindPersonModalRef.value.openModal();
          },
        },
        {
          label: t('删除'),
          ifShow: !record.enable && hasPermission('160010001000009'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('删除确认'),
              icon: h(ExclamationCircleOutlined),
              closable: true,
              content: t('是否删除该工位'),
              onOk: async () => {
                try {
                  await reqEquipmentStationDelete(record.id);
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
    return await reqEquipmentStationPage(params as any);
  };
  // 获取部门根节点(来源参数配置)
  const getRootNode = async () => {
    try {
      const res: any = await getParameter('platform.sys.client-name');
      rootNode.value = res.data.value || t('全部');
    } catch (error) {
      console.log(error);
    }
  };
  // 新增树节点
  const addItem = (node: any) => {
    treeTitle.value = t('新建子模型');
    formProps.initialValues = {
      parentId: node?.id || 'all',
      type: levelList.value[node?.type + 1] || levelList.value[0],
    };
    formProps.schemas[2].componentProps.disabled = false;
    treeOpen.value = true;
  };
  // 编辑树节点
  const editNodeFn = (node: any) => {
    treeTitle.value = t('编辑模型');
    formProps.initialValues = {
      parentId: node.parentId === '0' ? 'all' : node.parentId,
      id: node.id,
      name: node.name,
      code: node.code,
      type: levelList.value[node?.type],
    };
    formProps.schemas[2].componentProps.disabled = true;
    // modalFormRef.value?.formRef?.updateSchema({
    //   field: 'code',
    //   componentProps: {
    //     disabled: true,
    //   },
    // });
    treeOpen.value = true;
  };
  // 删除树节点
  const deleteNodeFn = (node: any) => {
    const content =
      node.children?.length > 0 ? t('该节点存在子节点，是否确定删除') : t('信息删除后无法恢复，是否删除?');
    Modal.confirm({
      title: t('是否删除该信息'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content,
      okText: t('确认'),
      cancelText: t('取消'),
      onOk: async () => {
        try {
          // const data = { id: node.id };
          await reqEquipmentModuleDelete(node.id);
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
          await reqEquipmentModuleUpdate(params2 as any);
        } else {
          // 新增保存
          const data2 = {
            ...data,
            type: levelList.value.indexOf(data?.type),
          };
          await reqEquipmentModuleSave(data2);
        }
        message.success(t(data.id ? t('编辑成功') : t('新增成功')));
        treeOpen.value = false;
        await getTreeData();
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };
  // 操作工位
  const handleWorkstation = (formData1: any, type1: any) => {
    formData.value = formData1;
    type.value = type1;
    WorkstationModalRef.value.openModal();
  };
  // 启停
  const changeState = async (record: Recordable, checked: boolean, tableAction: TableActionType) => {
    const title = checked ? t('是否启用此工位') : t('是否停用此工位');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}`,
      onOk: async () => {
        try {
          await reqEquipmentStationEnable({
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
  //刷新列表
  const updateTable = () => {
    tableInstance.value?.fetchData();
  };
  //获取所有节点
  const getTreeData = async () => {
    try {
      const res: any = await reqEquipmentModuleList();
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
    await getRootNode();
    await getTreeData();
  });
</script>

<style scoped></style>
