<template>
  <keep-alive>
    <EmptyBlock v-if="!showAddPlan">
      <div class="process-plan-table">
        <!-- 生产指令单页 -->
        <div class="plan-table">
          <BMTable
            ref="tableInstance"
            :data-request="loadData"
            :columns="columns"
            row-key="id"
            headerTitle=""
            :scroll="{ x: 844, y: 400 }"
            :showRefresh="false"
            :formProps="formProps"
            :showSearchBorder="true"
            :row-selection="{ selectedRowKeys: selected.selectedRowKeys, onChange: onSelectChange, getCheckboxProps: (record: any) => ({
    disabled: record.status?.value !== 'EDIT'
  }), }"
            :pagination="{
              pageSize: 20,
            }">
            <template #toolbar>
              <Button v-hasAuth="120030001000001" type="primary" @click="lookOrEdit({}, 'add')">
                {{ t('新建指令单') }}
              </Button>
              <Button v-hasAuth="120030001000005" @click="batchApproval">{{ t('提交审批') }}</Button>
            </template>
          </BMTable>

          <HistoryModal v-model:historyOpen="historyOpen" :businessId="rowData.id" />
        </div>
      </div>
    </EmptyBlock>
  </keep-alive>
  <!-- 新建指令单页面 -->
  <div v-if="showAddPlan && single" class="create">
    <singlePlan ref="singlePlanRef" :state="state" :formInfo="formInfo" @backAndSave="backAndSave"></singlePlan>
  </div>
  <!-- 批量创建页面 -->
  <div v-else-if="showAddPlan && multiple" class="create">
    <multiplePlan ref="multiplePlanRef" @multipleBackAndSave="multipleBackAndSave"></multiplePlan>
  </div>
  <!-- 审核进度页面 -->
  <div v-else-if="showAddPlan && showApprovalProgress" class="create">
    <approvalProgress
      ref="approvalProgressRef"
      :pageParams="pageParams"
      :source="t('生产指令单')"
      @toPlanApproval="toPlanApproval"></approvalProgress>
  </div>
  <!-- 日历调整页面 -->
  <div v-else-if="showAddPlan && showCalendarPage" class="create">
    <CalendarPage :rowData="rowData" @back="calendarBack"></CalendarPage>
  </div>
</template>

<script lang="tsx" setup>
  import type { DataRequestFn, TableInstance } from '@bmos/components';
  import { BMTable, TableColumn } from '@bmos/components';
  import { reactive, ref, createVNode } from 'vue';
  import singlePlan from './CreatePlan/index.vue';
  import multiplePlan from './BatchCreate/index.vue';
  import approvalProgress from '../PlanApproval/approvalProgress/index.vue'; //审核进度页面
  import CalendarPage from './calendarPage/index.vue'; //日历页面
  import { usePermissionStore } from '@/stores/permission';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, message, Button } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { productionPlanPage, submitApprove, planDiscard, reqPlanInfoApproveBatch } from '@/services';
  import HistoryModal from '@/components/History/index.vue';
  import EmptyBlock from '@/components/EmptyBlock/index.vue';

  const { hasPermission } = usePermissionStore();
  const tableInstance = ref<TableInstance>();
  const singlePlanRef = ref();
  const multiplePlanRef = ref();
  const showAddPlan = ref(false);
  const pageParams = ref(); //审核进度组件参数
  const single = ref(false); //单条新建编辑查看(非批量创建)
  const multiple = ref(false); //控制批量创建页面展示
  const showApprovalProgress = ref(false); //控制计划审核页面展示
  const showCalendarPage = ref(false); //控制日历页面展示
  const selected = reactive<{
    selectedRowKeys: any[]; //勾选的id集合
    selectedAll: any[];
  }>({
    selectedRowKeys: [],
    selectedAll: [],
  });
  const state = ref(); //判断新建编辑查看
  const formInfo = ref();

  const formProps = reactive<any>({
    actionColOptions: {},
    baseColProps: {
      span: 6,
    },
    showAdvancedButtonBadge: false,
    showAdvancedButton: true,
  });
  // 新增编辑查看
  const lookOrEdit = async (val: any, type: String) => {
    formInfo.value = val;
    showAddPlan.value = true;
    multiple.value = false;
    single.value = true;
    state.value = type;
  };
  // 调整日历按钮
  const adjustmentCalendar = (row: any) => {
    rowData.value = row;
    showAddPlan.value = true;
    single.value = false;
    multiple.value = false;
    showCalendarPage.value = true;
  };

  // 提交审批
  const submitReview = async (row: any) => {
    Modal.confirm({
      title: t('提交审批'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('是否将此指令单提交审批?'),
      okText: t('确认'),
      cancelText: t('取消'),
      onOk() {
        handleSubmitReview(row);
      },
    });
  };
  // 提交审批确认
  const handleSubmitReview = async (row: any) => {
    try {
      await submitApprove(row.id);
      message.success(t('提交成功'));
      updaData();
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 多选
  const onSelectChange = (selectedRowKeys: any[], selectedRows: any) => {
    selected.selectedRowKeys = selectedRowKeys;
    selected.selectedAll = selectedRows;
  };
  // 批量提交审批
  const batchApproval = () => {
    if (selected.selectedAll.length === 0) return message.error(t('请勾选指令单'));
    Modal.confirm({
      title: t('提交审批'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否将指令单提交审批'),
      async onOk() {
        try {
          const data = { planIdList: selected.selectedRowKeys };
          await reqPlanInfoApproveBatch(data);
          message.success(t('操作成功'));
          tableInstance.value?.fetchData();
          selected.selectedRowKeys = [];
          selected.selectedAll = [];
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
  // 审核进度 跳转
  const lookSchedule = async (row: any) => {
    pageParams.value = {
      processInstanceId: row.processInstanceId,
      deploymentId: row.deploymentId,
    };
    showAddPlan.value = true;
    single.value = false;
    multiple.value = false;
    showApprovalProgress.value = true;
  };
  // 审核进度页返回生产计划
  const toPlanApproval = async () => {
    showApprovalProgress.value = false;
    showAddPlan.value = false;
  };
  // 作废
  const invalid = async (row: any) => {
    Modal.confirm({
      title: t('提示'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('是否将此指令单作废?'),
      okText: t('确认'),
      cancelText: t('取消'),
      async onOk() {
        try {
          const res = await planDiscard(row.id);
          if (res.code === 0) {
            message.success(t('操作成功'));
            updaData();
          }
        } catch (error: any) {
          message.error(error.message);
        }
      },
    });
  };
  // 新建计划页面的返回和保存
  const backAndSave = async () => {
    showAddPlan.value = false;
    single.value = false;
  };
  // 批量创建页面的返回和保存
  const multipleBackAndSave = async () => {
    showAddPlan.value = false;
    multiple.value = false;
  };
  // 日历页面的返回
  const calendarBack = async () => {
    showAddPlan.value = false;
    showCalendarPage.value = false;
  };

  // 表格数据来源
  const loadData: DataRequestFn = async (params): Promise<any> => {
    return productionPlanPage(params);
  };
  // 刷新表格数据
  const updaData = () => {
    tableInstance.value?.fetchData();
  };

  // 状态样式
  const style = {
    width: '7px',
    height: '7px',
    borderRadius: '50%',
    marginRight: '8px',
  };
  const colorList: any = {
    EDIT: {
      color: '#2871FF',
    },
    AUDIT: {
      color: '#FF9A2F',
    },
    CONFIRM: {
      color: '#59BF78',
    },
    DISCARD: {
      color: '#6C6E73',
    },
    // 生产状态
    NOT_ISSUED: {
      color: '#FF9A2F',
    },
    ISSUED: {
      color: '#59BF78',
    },
    DURING_PRODUCTION: {
      color: '#2871FF',
    },
    PRODUCTION_COMPLETED: {
      color: '#59BF78',
    },
    PRODUCTION_PAUSED: {
      color: '#FF9A2F',
    },
    PRODUCTION_TERMINATION: {
      color: '#FF5633',
    },
  };
  const historyOpen = ref<boolean>(false);
  const rowData = ref<any>({});
  // 表格列
  const columns: TableColumn[] = [
    {
      title: t('指令单编号'),
      dataIndex: 'planNo',
      fixed: 'left',
      sorter: true,
      width: 200,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 200,
      resizable: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      hideInSearch: true,
      width: 200,
      resizable: true,
    },
    {
      title: t('规格'),
      dataIndex: 'productSpecification',
      hideInSearch: true,
      width: 200,
      resizable: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 200,
      resizable: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      sorter: true,
      width: 200,
      resizable: true,
    },
    {
      title: t('计划生产时间'),
      dataIndex: 'productDate',
      hideInSearch: true,
      sorter: true,
      width: 200,
      resizable: true,
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
      dataIndex: 'type',
      width: 180,
      resizable: true,
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
      customRender: ({ record }) => record.type?.label,
    },
    {
      title: t('指令创建时间'),
      dataIndex: 'createTime',
      hideInSearch: true,
      sorter: true,
      width: 200,
      resizable: true,
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 130,
      fixed: 'right',
      resizable: true,
      formItemProps: {
        component: 'Select',
        componentProps: () => ({
          options: [
            {
              label: t('编辑'),
              value: 'EDIT',
            },
            {
              label: t('审批中'),
              value: 'AUDIT',
            },
            {
              label: t('确认'),
              value: 'CONFIRM',
            },
            {
              label: t('作废'),
              value: 'DISCARD',
            },
          ],
        }),
      },
      customRender: ({ record }) => (
        <div style='display: flex;align-items: center;'>
          <div
            style={{
              ...style,
              backgroundColor: colorList[record.status?.value]?.color,
            }}></div>
          <div style={{ color: colorList[record.status?.value]?.color }}>{record.status?.label}</div>
        </div>
      ),
    },
    {
      title: t('生产状态'),
      dataIndex: 'productionStatus', //字段待换、新建指令单按钮待隐藏、批量创建按钮待隐藏
      width: 130,
      fixed: 'right',
      resizable: true,
      formItemProps: {
        component: 'Select',
        componentProps: () => ({
          options: [
            {
              label: t('未下发'),
              value: 'NOT_ISSUED', //待对接
            },
            {
              label: t('已下发'),
              value: 'ISSUED',
            },
            {
              label: t('生产中'),
              value: 'DURING_PRODUCTION',
            },
            {
              label: t('生产完成'),
              value: 'PRODUCTION_COMPLETED',
            },
            {
              label: t('生产终止'),
              value: 'PRODUCTION_TERMINATION',
            },
            {
              label: t('生产暂停'),
              value: 'PRODUCTION_PAUSED',
            },
          ],
        }),
      },
      customRender: ({ record }) => (
        <div style='display: flex;align-items: center;'>
          <div
            style={{
              ...style,
              backgroundColor: colorList[record?.productionStatus?.value]?.color,
            }}></div>
          <div style={{ color: colorList[record?.productionStatus?.value]?.color }}>
            {record?.productionStatus?.label || '-'}
          </div>
        </div>
      ),
    },
    {
      title: t('操作'),
      fixed: 'right',
      hideInSearch: true,
      width: 260,
      resizable: true,
      key: 'ACTION',
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: record.status.value == 'EDIT' && hasPermission('120030001000003'),
          onClick: () => {
            lookOrEdit(record, 'edit');
          },
        },
        {
          label: t('查看'),
          ifShow: hasPermission('120030001000004'),
          onClick: () => {
            lookOrEdit(record, 'look');
          },
        },
        {
          label: t('日历调整'),
          ifShow: record.status.value !== 'AUDIT' && hasPermission('120030001000009'),
          onClick: () => {
            adjustmentCalendar(record);
          },
        },
        {
          label: t('提交审批'),
          ifShow: record.status.value == 'EDIT' && hasPermission('120030001000005'),
          onClick: () => {
            submitReview(record);
          },
        },
        {
          label: t('作废'),
          ifShow:
            (record.status.value == 'EDIT' || record.status.value == 'CONFIRM') && hasPermission('120030001000006'),
          onClick: () => {
            invalid(record);
          },
        },
        {
          label: t('审核进度'),
          ifShow: record.status.value == 'AUDIT' && hasPermission('120030001000007'),
          onClick: () => {
            lookSchedule(record);
          },
        },
        {
          label: t('历史'),
          ifShow: hasPermission('120030001000008'),
          onClick: () => {
            rowData.value = record;
            historyOpen.value = true;
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
  .process-plan-table {
    padding: 0 var(--bmos-padding-small);
    padding-top: 16px;
    background-color: var(--bmos-primary-color-white);
    height: 100%;
    display: flex;
    flex-direction: column;
    .plan-table {
      height: 100%;
      flex: 1;
      overflow-y: hidden;
    }
    .bmos-table {
      height: 100%;
    }
    :deep(.css-dev-only-do-not-override-czxzu3).mes-form {
      border-bottom: 1px solid rgb(225, 227, 229);
    }
  }
  .create {
    height: 100%;
  }
</style>
