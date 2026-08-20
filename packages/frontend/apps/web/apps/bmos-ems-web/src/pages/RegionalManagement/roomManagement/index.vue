<!-- 房间管理 -->
<template>
  <BMPageComponent
    ref="tableInstance"
    :treeData="treeData"
    :titles="[t('房间列表')]"
    :showAction="
      hasPermission('160030002000001') || hasPermission('160030002000002') || hasPermission('160030002000003')
    "
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
          // span: 12,
        },
      },
      {},
    ]"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <!-- 新增编辑-房间弹框 -->
      <RoomModal
        ref="RoomModalRef"
        :rowId="rowId"
        :type="type"
        :formData="formData"
        @updateTable="updateTable"></RoomModal>
      <!-- 查看-房间弹框 -->
      <ViewModal ref="ViewModalRef" :rowId="rowId" :cleanLevelInfo="cleanLevelInfo"></ViewModal>
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
      <!-- 打印标签弹框 -->
      <BMPrint
        v-model:open="printOpen"
        :getPrinter="reqGetPrintEquipment"
        sceneId="160005001"
        @printConfirm="printConfirm"></BMPrint>
      <Button v-hasAuth="160030002000004" type="primary" @click="handleRoom(treeNode, 'add')">
        {{ t('新增房间') }}
      </Button>
      <Button v-hasAuth="160030002000011" @click="print">
        {{ t('打印标签') }}
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
  <!--关联模型弹窗-->
  <BMModalForm
    ref="relationModalFormRef"
    v-model:open="relationOpen"
    :title="t('关联模型')"
    :formProps="relationFormProps"
    wrapClassName="modalSizeMedium"
    @okModal="okRelationModal"></BMModalForm>
</template>

<script setup lang="tsx">
  import { ref, onMounted, createVNode } from 'vue';
  import { message, Button, Modal, Switch } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import type { TableColumn, ModalFormType, Recordable, TableActionType } from '@bmos/components';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { BMModalForm, BMPageComponent, BMPrint } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';
  import ViewModal from './components/ViewModal.vue';
  import RoomModal from './components/RoomModal.vue';
  import BindStationModal from './components/BindStationModal.vue';
  import PermissionModal from './components/PermissionModal.vue';
  import { loopTree2 } from './utils';
  import {
    reqRoomModuleTreeList, //获取树数据
    reqRoomModuleSave, //树新增
    reqRoomModuleUpdate, //树修改
    reqRoomModuleDelete, //树删除
    reqFactoryRoomDelete, //删除房间
    reqFactoryRoomEnable, //启停房间
    reqFactoryRoomPage, //获取表格数据
    reqGetPrintEquipment, //获取打印机
    postTagInstancePrintBatch, //打印接口
    reqRoomBind3DModel, //房间关联模型
    reqListDictCode, //获取洁净等级字典
  } from '@/services';
  const { hasPermission } = usePermissionStore();
  const router = useRouter();

  const tableInstance = ref<any>();
  const treeOpen = ref<boolean>(false);
  const relationOpen = ref<boolean>(false);
  const treeSelectedKeys = ref<string[]>(['all']);
  const modalFormRef = ref<any>();
  const relationModalFormRef = ref<any>();
  const ViewModalRef = ref<any>();
  const RoomModalRef = ref<any>();
  const BindStationModalRef = ref<any>();
  const treeTitle = ref<string>(t('新增分类'));
  const rowId = ref<any>();
  const treeData = ref<any[]>([]);
  const formData = ref<any>();
  const stationIdList = ref<any>(); //绑定工位lists
  const type = ref<string>('');
  const permissionModalOpen = ref<boolean>(false); // 数据权限modal
  const selectedRowKeys1 = ref<any>([]); //多选时的表格ids
  const operationSelectedRows = ref<any>([]); //存多选的数据
  const printOpen = ref<any>(false);
  // 洁净等级
  const cleanLevelInfo = ref<any>({ a: 123 });
  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      selectedRowKeys: selectedRowKeys1.value,
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
        }
        operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);
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
            const res: any = await reqRoomModuleTreeList();
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
  const relationFormProps = reactive<any>({
    initialValues: {
      '3DModelId': '',
    },
    schemas: [
      {
        field: '3DModelId',
        component: 'Input',
        label: t('模型id'),
        required: true,
      },
    ],
  });
  const actionList = [
    {
      title: t('新增子分类'),
      action: 'addChildren',
      ifShow: () => {
        return hasPermission('160030002000001');
      },
    },
    {
      title: t('编辑分类'),
      action: 'editNode',
      ifShow: () => {
        return hasPermission('160030002000002');
      },
    },
    {
      title: t('删除分类'),
      action: 'deleteNode',
      ifShow: () => {
        return hasPermission('160030002000003');
      },
    },
  ];
  // 表格项
  const columns: TableColumn[] = [
    {
      title: t('房间编码'),
      dataIndex: 'code',
      resizable: true,
      sorter: true,
    },
    {
      title: t('房间名称'),
      dataIndex: 'name',
      resizable: true,
    },
    {
      title: t('所属楼栋'),
      dataIndex: 'tenementName',
      resizable: true,
    },
    {
      title: t('所属楼层'),
      dataIndex: 'floorName',
      resizable: true,
    },
    {
      title: t('洁净等级'),
      dataIndex: 'cleanLevel',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return cleanLevelInfo.value[record.cleanLevel] || '-';
      },
    },
    {
      title: t('默认效期'),
      dataIndex: 'timeLimit',
      hideInSearch: true,
      width: 120,
      resizable: true,
      customRender: ({ record }) => record.timeLimit + t('分钟'),
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
            v-hasAuth='160030002000010'
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
          ifShow: record.enable && hasPermission('160030002000006'),
          onClick: () => {
            rowId.value = record.id;
            ViewModalRef.value.openModal();
          },
        },
        {
          label: t('编辑'),
          ifShow: !record.enable && hasPermission('160030002000005'),
          onClick: () => {
            handleRoom(record, 'edit');
          },
        },
        {
          label: t('数据权限'),
          ifShow: hasPermission('160030002000007'),
          onClick: () => {
            dataPermissions(record);
          },
        },
        {
          label: t('绑定工位'),
          ifShow: hasPermission('160030002000008'),
          onClick: () => {
            rowId.value = record.id;
            stationIdList.value = record?.stationIdList || [];
            BindStationModalRef.value.openModal();
          },
        },
        {
          label: t('配置环境参数'),
          ifShow: hasPermission('160030002000012'),
          onClick: () => {
            router.push({
              name: 'configEnvironmentParams',
              query: {
                roomId: record.id,
              },
            });
          },
        },
        {
          label: t('关联模型'),
          ifShow: hasPermission('160030002000013'),
          onClick: () => {
            rowId.value = record.id;
            relationFormProps.initialValues['3DModelId'] = record.threeDModelId;
            relationOpen.value = true;
          },
        },
        {
          label: t('删除'),
          ifShow: !record.enable && hasPermission('160030002000009'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('删除确认'),
              icon: h(ExclamationCircleOutlined),
              closable: true,
              content: t('是否删除该房间'),
              onOk: async () => {
                try {
                  await reqFactoryRoomDelete(record.id);
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
    return await reqFactoryRoomPage(params as any);
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
    Modal.confirm({
      title: t('是否删除该信息'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('信息删除后无法恢复，是否删除'),
      okText: t('确认'),
      cancelText: t('取消'),
      onOk: async () => {
        try {
          await reqRoomModuleDelete(node.id);
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
          await reqRoomModuleUpdate(params2 as any);
        } else {
          // 新增保存
          const data2 = {
            ...data,
          };
          await reqRoomModuleSave(data2);
        }
        message.success(t(data.id ? t('编辑成功') : t('新增成功')));
        treeOpen.value = false;
        await getTreeData();
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };
  // 关联模型弹框确定
  const okRelationModal = (instance: ModalFormType) => {
    instance.validate().then(async params => {
      const data: any = { ...params, roomId: rowId.value };
      try {
        await reqRoomBind3DModel(data);
        message.success(t('关联成功'));
        relationOpen.value = false;
        updateTable();
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };
  // 操作房间
  const handleRoom = (formData1: any, type1: any) => {
    formData.value = formData1;
    type.value = type1;
    RoomModalRef.value.openModal();
  };
  // 启停
  const changeState = async (record: Recordable, checked: boolean, tableAction: TableActionType) => {
    const title = checked ? t('是否启用此房间') : t('是否停用此房间');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}`,
      onOk: async () => {
        try {
          await reqFactoryRoomEnable({
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
  // 获取打印机
  // const getPrinter = async () => {
  //   try {
  //     const { data } = await reqGetPrintEquipment();
  //     return data;
  //   } catch (error) {
  //     return [];
  //   }
  //   // return [{ name: '写死的打印机1', id: '123', ip: '打印机ip', port: '打印机端口' }];
  // };
  // 打印标签
  const print = () => {
    if (operationSelectedRows.value.length === 0) return message.error(t('请先选择房间'));
    if (operationSelectedRows.value.findIndex((item: any) => item.enable === false) >= 0) {
      return message.error(t('不能打印停用的房间'));
    }
    printOpen.value = true;
  };
  // 确认打印
  const printConfirm = async (printerParams: any) => {
    try {
      const { printerIp, printerPort, printerDpi, sceneId } = printerParams;
      const batchParams = operationSelectedRows.value.map((item: any) => {
        return {
          printerIp,
          printerPort,
          dpi: printerDpi,
          sceneId,
          body: {
            roomId: item?.id,
          },
        };
      });
      await postTagInstancePrintBatch(batchParams);
      message.success(t('打印成功'));
      printOpen.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  //刷新列表
  const updateTable = () => {
    tableInstance.value?.fetchData();
  };
  //获取所有节点
  const getTreeData = async () => {
    try {
      const res: any = await reqRoomModuleTreeList();
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
  // 获取洁净等级字典
  const getDictCode = async () => {
    try {
      const res = await reqListDictCode({
        code: 'CleanroomClassifications',
      });
      (res.data || []).forEach((item: any) => {
        cleanLevelInfo.value[item.value] = item.label;
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  onMounted(async () => {
    getDictCode();
    await getTreeData();
  });
</script>

<style scoped></style>
