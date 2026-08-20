<template>
  <keep-alive>
    <EmptyBlock v-if="!showAddPlan && !showApprovalProgress">
      <div class="plan-approval-table">
        <!-- 计划审核页 -->
        <div class="table">
          <BMTable
            ref="tableInstance"
            :data-request="loadData"
            :columns="columns"
            row-key="id"
            headerTitle=""
            :showSearchBorder="true"
            :scroll="{ x: 844, y: 400 }"
            :showRefresh="false"
            :pagination="{
              pageSize: 20,
            }"
            :show-tool-bar="false"
            :formProps="formProps"></BMTable>
        </div>
      </div>
    </EmptyBlock>
  </keep-alive>
  <!-- 处理流程页面（同新建计划页面）(可查看详情) -->
  <div v-if="showAddPlan" class="create">
    <singlePlan ref="singlePlanRef" :state="state" :formInfo="formInfo" @backAndSave="backAndSave"></singlePlan>
  </div>
  <!-- 审核进度页面 -->
  <div v-else-if="!showAddPlan && showApprovalProgress" class="create">
    <approvalProgress
      ref="approvalProgressRef"
      :pageParams="pageParams"
      :source="t('指令单审核')"
      @toPlanApproval="toPlanApproval"></approvalProgress>
  </div>
</template>

<script lang="tsx" setup>
  import type { DataRequestFn, FormProps, TableInstance } from '@bmos/components';
  import { BMTable, TableColumn } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { productionPlanApprovalPage } from '@/services';
  import singlePlan from '../ProductionPlan/CreatePlan/index.vue'; //点详情跳转至
  import approvalProgress from '../PlanApproval/approvalProgress/index.vue'; //审核进度页面
  import { usePermissionStore } from '@/stores/permission';
  import EmptyBlock from '@/components/EmptyBlock/index.vue';

  const { hasPermission } = usePermissionStore();
  const showAddPlan = ref(false);
  const showApprovalProgress = ref(false); //控制审核进度页面展示

  const pageParams = ref(); //传给审核进度的参数
  const singlePlanRef = ref();
  const state = ref(); //判断新建编辑查看还是详情
  const formInfo = ref();

  const tableInstance = ref<TableInstance>();
  const formProps: Ref<Partial<FormProps>> = ref({
    actionColOptions: {
      // span: 4,
    },
    baseColProps: {
      span: 6,
    },
    showAdvancedButton: true,
    showAdvancedButtonBadge: false,
  });
  // 详情按钮（处理）
  const lookDetails = async (val: any, type: String) => {
    showAddPlan.value = true;
    state.value = type;
    formInfo.value = val;
  };
  // 详情页的返回
  const backAndSave = async () => {
    showAddPlan.value = false;
  };
  // 审核进度按钮
  const approvalSchedule = async (row: any) => {
    showApprovalProgress.value = true;
    pageParams.value = {
      processInstanceId: row.processInstanceId,
      deploymentId: row.deploymentId,
    };
  };
  // 审核进度页返回计划审核
  const toPlanApproval = async () => {
    showApprovalProgress.value = false;
  };
  // 表格数据来源
  const loadData: DataRequestFn = async (params): Promise<any> => {
    return productionPlanApprovalPage(params);
  };
  // 表格列
  const columns: TableColumn[] = [
    {
      title: t('指令单编号'),
      align: 'left',
      dataIndex: 'planNo',
      fixed: 'left',
      width: 200,
      resizable: true,
      sorter: true,
    },
    {
      title: t('产品名称'),
      align: 'left',
      width: 200,
      resizable: true,
      dataIndex: 'productName',
    },
    {
      title: t('产品编码'),
      align: 'left',
      dataIndex: 'productMergeCode',
      width: 200,
      resizable: true,
      hideInSearch: true,
      // hideInTable: true,//是否隐藏表格里的此项
    },
    {
      title: t('规格'),
      align: 'left',
      width: 200,
      resizable: true,
      dataIndex: 'productSpecification',
      hideInSearch: true,
    },
    {
      title: t('工艺名称'),
      align: 'left',
      dataIndex: 'processName',
      width: 200,
      resizable: true,
    },
    {
      title: t('生产批号'),
      align: 'left',
      width: 200,
      resizable: true,
      dataIndex: 'batchNo',
      sorter: true,
    },
    {
      title: t('计划生产时间'),
      align: 'left',
      dataIndex: 'productDate',
      width: 150,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('生产批量'),
      dataIndex: 'batchQuantity',
      hideInSearch: true,
      width: 200,
      customRender: ({ record }) => record.batchQuantity + record.unitName,
    },
    {
      title: t('指令单类型'),
      align: 'left',
      dataIndex: 'type',
      width: 120,
      resizable: true,
      // hideInSearch: true,
      formItemProps: {
        component: 'Select',
        order: 5,
        componentProps: () => ({
          options: [
            {
              label: t('生产批次'),
              value: 'PRODUCT',
            },
            {
              label: t('实验批次'),
              value: 'EXPERIMENT',
            },
            {
              label: t('验证批次'),
              value: 'VERIFY',
            },
          ],
        }),
      },
      customRender: ({ record }) => record.type?.label || '-',
    },
    // 新加三字段
    {
      title: t('审核节点名称'),
      align: 'left',
      dataIndex: 'elementName',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('发起人'),
      align: 'left',
      dataIndex: 'processStartByName',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('发起时间'),
      align: 'left',
      dataIndex: 'processStartTime',
      width: 180,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 220,
      actions: ({ record }) => [
        {
          label: t('处理'),
          ifShow: hasPermission('120030002000001'),
          onClick: () => {
            lookDetails(record, 'details');
          },
        },
        {
          label: t('审核进度'),
          ifShow: hasPermission('120030002000002'),
          onClick: () => {
            approvalSchedule(record);
          },
        },
      ],
    },
  ];
</script>
<style lang="less" scoped>
  :deep(.action-list) {
    .mes-btn {
      padding-right: 12px;
      padding-left: 0;
    }
  }
  .plan-approval-table {
    padding: 0 var(--bmos-padding-small);
    padding-top: 16px;
    background-color: var(--bmos-primary-color-white);
    height: 100%;
    display: flex;
    flex-direction: column;
    .bmos-table {
      height: 100%;
    }
    .table {
      flex: 1;
      overflow-y: hidden;
    }
    :deep(.mes-form) {
      border-bottom: 1px solid rgb(225, 227, 229);
    }
  }
  .create {
    height: 100%;
  }
</style>
