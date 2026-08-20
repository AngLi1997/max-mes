<template>
  <BMPageComponent
    ref="tableInstance"
    :showAllAddIcon="false"
    :showAction="false"
    :rowKeys="['id', 'id']"
    :treeData="treeData"
    :default-selected-keys="['all']"
    :search="[true, false]"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: 18,
        },
      },
      {},
    ]"
    :fieldNames="{
      title: 'showName',
      key: 'id',
      children: 'children',
    }"
    :treeField="{
      field: {
        productId: 'id',
        categoryFlag: 'categoryFlag',
      },
    }"
    :tableFields="[
      {},
      {
        field: {
          id: 'id',
        },
      },
    ]"
    :requests="[
      reqFormulaListReq as DataRequestFn,
      reqFormulaVersionListReq as DataRequestFn,
    ]"
    :columns="[column1, column2]"
    @tree-select="treeSelect">
    <template #tableHeaderToolbar0="{ treeNode }">
      <PermissionModal
        v-model:permissionOpen="permissionModalOpen"
        :processId="firstRowData?.id"
        :type="true"
        @ok="savePermission" />
      <Button v-hasAuth="120020004000001" type="primary" @click="addFormula(treeNode)">
        {{ t('新增生产BOM') }}
      </Button>
    </template>

    <template #tableHeaderToolbar1="{ currentNodes }">
      <HistoryModal v-model:historyOpen="historyOpen" :businessId="secondRowData?.id" />
      <Button v-hasAuth="120020004000003" @click="() => addVersion(currentNodes)">
        {{ t('新增版本') }}
      </Button>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('生产BOM列表')"></BMTableTitle>
    </template>
    <template #tableHeaderTitle1>
      <BMTableTitle :title="t('版本信息')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import {
    reqProductMaterialProductTreeReq, //树数据接口
    reqFormulaList, //生产BOM分页
    reqFormulaVersionList, //生产BOM-版本分页
    reqFormulaVersionChangeState, //修改启停状态
    reqFormulaVersionApprove, //提交审核
  } from '@/services';
  import { Switch, message, Modal } from 'ant-design-vue';
  import { ref, createVNode } from 'vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import PermissionModal from './components/PermissionModal.vue';
  import HistoryModal from '@/components/History/index.vue';
  import { DataRequestFn, BMPageComponent, BMTableTitle, TableColumn, TableInstance } from '@bmos/components';
  import { useRouter } from 'vue-router';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();
  const router = useRouter();
  // 第一个table 行数据
  const firstRowData = ref<any>({});
  // 数据权限modal
  const permissionModalOpen = ref<boolean>(false);
  // 历史弹框
  const historyOpen = ref<boolean>(false);
  // 当前操作行数据
  const secondRowData = ref<any>({});
  const selectCurrentNode = ref<any>(); //两个表格都选中行的时候存两行的数据

  // 状态样式
  const style = {
    width: '7px',
    height: '7px',
    borderRadius: '50%',
    marginRight: '8px',
  };
  const colorList: any = {
    0: {
      color: '#1677ff',
    },
    1: {
      color: '#FF9A2F',
    },
    2: {
      color: '#59BF78',
    },
  };
  const tableInstance = ref<TableInstance>();
  const treeData = ref<any[]>([]);
  const column1: TableColumn[] = [
    {
      title: t('BOM名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 200,
      resizable: true,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 200,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 200,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('规格'),
      dataIndex: 'productSpecification',
      width: 200,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('启用版本'),
      dataIndex: 'enableVersion',
      width: 200,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 220,
      actions: ({ record }) => [
        {
          label: t('数据权限'),
          ifShow: hasPermission('120020004000002'),
          onClick: () => {
            dataPermissions(record);
          },
        },
      ],
    },
  ];
  const column2: TableColumn[] = [
    {
      title: t('版本号'),
      dataIndex: 'versionNo',
      fixed: 'left',
      width: 200,
      resizable: true,
    },
    {
      title: t('版本描述'),
      dataIndex: 'description',
      resizable: true,
      width: 200,
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 200,
      resizable: true,
      customRender: ({ record }) => (
        <div style='display: flex;align-items: center;'>
          <div
            style={{
              ...style,
              backgroundColor: colorList[record.status?.value]?.color,
            }}></div>
          <div style={{ color: colorList[record.status?.value]?.color }}>{record.status.name}</div>
        </div>
      ),
    },

    {
      title: t('启停'),
      dataIndex: 'enable',
      width: 200,
      resizable: true,
      customRender: ({ record }) => (
        <Switch
          v-hasAuth='120020004000008'
          disabled={record.status?.value === 2 ? false : true}
          checked={record.enable == true ? true : false}
          onChange={(h, e: any) => {
            e.stopPropagation();
            changeSwitch(record);
          }}></Switch>
      ),
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: record.status?.value === 0 && hasPermission('120020004000004'),
          onClick: () => {
            router.push({
              name: 'formula-config-add-formula',
              query: {
                status: 'editVersion',
                versionNo: record.versionNo,
                versionId: record.id,
                formulaId: '',
              },
            });
          },
        },
        {
          label: t('查看'),
          ifShow: hasPermission('120020004000005'),
          onClick: () => {
            router.push({
              name: 'formula-config-add-formula',
              query: {
                status: 'viewVersion',
                versionNo: record.versionNo,
                versionId: record.id,
                formulaId: '',
              },
            });
          },
        },
        {
          label: t('审核'),
          ifShow: record.status?.name === t('编辑') && hasPermission('120020004000006'),
          onClick: () => {
            approval(record);
          },
        },
        {
          label: t('历史'),
          ifShow: hasPermission('120020004000007'),
          onClick: () => {
            secondRowData.value = record;
            historyOpen.value = true;
          },
        },
        {
          label: t('审核进度'),
          ifShow: record.status?.value === 1 && hasPermission('120020004000009'),
          onClick: () => {
            router.push({
              name: 'formula-config-schedule',
              query: {
                processInstanceId: record.processInstanceId,
                fromList: 'fromList',
                title: t('生产BOM配置'),
              },
            });
          },
        },
      ],
    },
  ];
  // 生产BOM分页
  const reqFormulaListReq = async (params: any) => {
    const { productId, categoryFlag, ...newParams }: any = params;
    if (categoryFlag) {
      newParams.categoryId = productId;
    } else {
      newParams.productId = productId;
    }
    if (productId === 'all') {
      delete newParams.productId;
      delete newParams.categoryId;
    }
    return await reqFormulaList(newParams as any);
  };

  const curSelect = ref<any>({});
  const treeSelect = (node: any, info: any) => {
    curSelect.value = info.node;
  };
  // 第二表格数据来源
  const reqFormulaVersionListReq = async (params: any) => {
    if (!params.id) return Promise.resolve({ data: [] });
    return await reqFormulaVersionList(params);
  };

  // 数据权限
  const dataPermissions = (record: any) => {
    permissionModalOpen.value = true;
    firstRowData.value = record;
  };
  // 生产BOM
  const addFormula = (treeNode: any) => {
    router.push({
      name: 'formula-config-add-formula',
      query: {
        status: 'addFormula',
        treeNodeId: treeNode?.id && treeNode.id !== 'all' ? treeNode?.id : undefined,
        categoryFlag: treeNode?.categoryFlag ? treeNode?.categoryFlag : '',
      },
    });
  };
  // 新增版本
  const addVersion = (currentNode: any) => {
    if (currentNode?.[1]?.versionNo) {
      selectCurrentNode.value = currentNode;
      router.push({
        name: 'formula-config-add-formula',
        query: {
          status: 'addVersion',
          versionNo: currentNode?.[1]?.versionNo,
          versionId: currentNode?.[1]?.id,
          formulaId: currentNode?.[0]?.id, //第一个生产BOM列表表格的id
        },
      });
    } else {
      message.warning(t('请先选择生产BOM版本'));
    }
  };

  // 切换开关
  const changeSwitch = (val: any) => {
    let content = val.enable ? t('是否停用生产BOM版本') : t('是否启用生产BOM版本');
    Modal.confirm({
      title: t('提示'),
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
    // 若最先为开启true
    if (row.enable) {
      try {
        const data = { id: row.id, state: 'false' };
        const res: any = await reqFormulaVersionChangeState(data);
        if (res.code === 0) {
          message.success(t('停用成功'));
          tableInstance.value?.fetchData(1);
          return;
        }
        message.error(t(res.message));
      } catch (error: any) {
        message.error(t(error.message));
      }
    } else {
      try {
        const data = { id: row.id, state: 'true' };
        const res: any = await reqFormulaVersionChangeState(data);
        if (res.code === 0) {
          message.success(t('启用成功'));
          tableInstance.value?.fetchData(1);
          return;
        }
        message.error(t(res.message));
      } catch (error: any) {
        message.error(t(error.message));
      }
    }
  };
  // 审核
  const approval = async (record: any) => {
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${t('是否发起审核版本')}${record.versionNo}`,
      async onOk() {
        try {
          await reqFormulaVersionApprove({ versionId: record.id });
          message.success(t('操作成功'));
          tableInstance.value?.fetchData(1);
          sendMessage(MessageType.UpdateMessageCount);
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };
  // 保存数据权限之后刷新第一个表格
  const savePermission = async () => {
    tableInstance.value?.fetchData(0);
  };
  const getTreeData = async () => {
    try {
      const { data } = await reqProductMaterialProductTreeReq();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          showName: t('全部'),
          key: 'all',
          categoryFlag: true,
          children: data,
        },
      ];
    } catch (error) {}
  };
  onMounted(() => {
    getTreeData();
  });
</script>
