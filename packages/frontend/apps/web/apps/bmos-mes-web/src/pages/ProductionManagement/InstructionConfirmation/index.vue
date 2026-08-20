<!-- 指令单确认 -->
<template>
  <keep-alive>
    <EmptyBlock v-if="!open">
      <div class="main bg-white">
        <BMTable
          ref="tableInstance"
          :data-request="loadData"
          :columns="columns"
          row-key="id"
          auto-height
          :autoHeightOffset="24"
          :scroll="{ x: 1144, y: 400 }"
          :formProps="formProps"
          :row-selection="{ selectedRowKeys: state.selectedRowKeys, onChange: onSelectChange }"
          showSearchBorder
          @handleClickRow="handleClickRow">
          <template #toolbar>
            <Button v-hasAuth="120030004000001" @click="batchConfirm">
              {{ t('批量确认') }}
            </Button>
          </template>
        </BMTable>
        <!-- 确认页 -->
      </div>
    </EmptyBlock>
  </keep-alive>
  <Decompose v-if="open" :rowData="rowData" @close="decomposeClose" />
</template>

<script lang="ts" setup>
  import type { DataRequestFn, FormProps, TableInstance } from '@bmos/components';
  import { BMTable } from '@bmos/components';
  import { GetRowKey } from 'ant-design-vue/es/table/interface';
  import { reactive, ref } from 'vue';
  import { useTable } from './hooks/useTable';
  import { t } from '@bmos/i18n';
  import Decompose from './components/Decompose.vue';
  import { planInstructionPage, reqPlanInstructionTeamBatchConfirm } from '@/services';
  import { Button, message, Modal } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import EmptyBlock from '@/components/EmptyBlock/index.vue';

  const open = ref<boolean>(false);
  const rowData = ref<any>({}); //当前行数据
  // 点击确认
  const rowClick = (row: any) => {
    open.value = true;
    rowData.value = row;
  };
  const state = reactive<{
    selectedRowKeys: any[]; //勾选的id集合
    selectedAll: any[];
  }>({
    selectedRowKeys: [],
    selectedAll: [],
  });
  const { columns } = useTable({ rowClick });
  const tableInstance = ref<TableInstance>();
  // 表单配置
  const formProps = reactive<Partial<FormProps>>({
    actionColOptions: {
      // span: 6,
    },
    baseColProps: {
      span: 6,
    },
    schemas: [
      {
        field: 'planNo',
        component: 'Input',
        label: t('指令单编号'),
      },
      {
        field: 'productName',
        component: 'Input',
        label: t('产品名称'),
      },
      {
        field: 'processName',
        component: 'Input',
        label: t('工艺名称'),
      },
      {
        field: 'batchNo',
        component: 'Input',
        label: t('生产批号'),
      },
      {
        field: 'type',
        component: 'Select',
        label: t('指令单类型'),
        componentProps: {
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
        },
      },
    ],
  });
  const loadData: DataRequestFn = async (params): Promise<any> => {
    return planInstructionPage({ ...params });
  };
  const handleClickRow = (record: any, key: string | GetRowKey<any>, selectedRowKeys: (string | GetRowKey<any>)[]) => {
    console.log('record', record, key, selectedRowKeys);
  };
  // 详情页关闭
  const decomposeClose = () => {
    open.value = false;
    tableInstance.value?.fetchData();
  };
  // 多选
  const onSelectChange = (selectedRowKeys: any[], selectedRows: any) => {
    state.selectedRowKeys = selectedRowKeys;
    state.selectedAll = selectedRows;
  };
  // 批量确认
  const batchConfirm = () => {
    if (state.selectedAll.length === 0) return message.error(t('请勾选指令单'));
    Modal.confirm({
      title: t('批量确认'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否确认此次所选择的指令单'),
      async onOk() {
        try {
          const instructionInfoList = state.selectedAll?.map((item: any) => {
            return {
              instructionId: item.id, //指令单id
              productPlanId: item.productPlanId, //生产计划id
            };
          });
          await reqPlanInstructionTeamBatchConfirm({ instructionInfoList });
          message.success(t('操作成功'));
          tableInstance.value?.fetchData();
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };
</script>
<style scoped lang="less">
  .main {
    height: 100%;
    min-height: 100%;
    background-color: white;
    padding: 16px;
  }
  :deep(.action-list) {
    display: flex;
    justify-content: flex-start;
  }
  :deep(.bmos-table .bmos-action-list .mes-btn) {
    padding: 6px 0;
  }
</style>
