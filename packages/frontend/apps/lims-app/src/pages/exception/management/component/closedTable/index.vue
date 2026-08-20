<template>
  <BMTable
    ref="tableRef"
    v-bind="tableProps"
    :extra-params="{
      productId: filterData.productId,
      exceptionType: filterData.exceptionType,
      exceptionDescription: filterData.exceptionDescription,
      investigating: false,
    }"
    :data-request="getTableList"
  />
</template>

<script setup lang="jsx">
import {
  getExceptionPage,
  reqHistoryList,
} from '@/api';
import { BMTable } from '@/BMComponents';
import { usePermissionStore } from '@/stores/permission.js';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';
import { filterData, historyDataList, rowData, showData, showHandling, showHistory } from '../../hooks/useDatas';

const { hasPermission } = usePermissionStore();

const tableRef = ref();
const tableProps = reactive({
  pagination: true,
  border: false,
  showNoData: true,
  tableColProps: [
    {
      prop: 'exceptionType',
      label: t('异常类型'),
      width: 200,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'exceptionDescription',
      label: t('异常描述'),
      width: 300,
      thProps: {
        align: 'left',
      },
    },
    {
      label: t('异常状态'),
      prop: 'exceptionStatus',
      width: 120,
      customRender: ({ row }) => {
        return <view>{row.exceptionStatus?.name || ''}</view>;
      },
    },
    {
      prop: 'recordMode',
      label: t('记录方式'),
      width: 180,
      thProps: {
        align: 'left',
      },
      customRender: ({ row }) => {
        return <view>{row.recordMode.name}</view>;
      },
    },
    {
      prop: 'recordUserName',
      label: t('记录人'),
      width: 140,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'recordTime',
      label: t('记录时间'),
      width: 400,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'createByUserName',
      label: t('创建人'),
      width: 140,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'createTime',
      label: t('创建时间'),
      width: 225,
      thProps: {
        align: 'left',
      },
    },
    {
      label: t('处理结果'),
      prop: 'handleResult',
      width: 240,
    },
    {
      label: t('处理人'),
      prop: 'handleUserName',
      width: 140,
    },
    {
      label: t('处理时间'),
      prop: 'handleTime',
      width: 225,
    },
    {
      label: t('作废原因'),
      prop: 'cancelReason',
      width: 240,
    },
    {
      label: t('作废人'),
      prop: 'cancelUserName',
      width: 149,
    },
    {
      label: t('作废时间'),
      prop: 'cancelTime',
      width: 225,
    },
    {
      prop: 'productFullName',
      label: t('产品名称'),
      width: 300,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'batchNo',
      label: t('生产批号'),
      width: 240,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'processName',
      label: t('工艺名称'),
      width: 300,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'procedureName',
      label: t('工序名称'),
      width: 240,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'procedureStepName',
      label: t('工序步骤/任务名称'),
      width: 240,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'ACTION',
      label: t('操作'),
      width: 300,
      fixed: 'right',
      thProps: {
        align: 'left',
      },
      actions: ({ row }) => {
        return [
          {
            label: t('操作历史'),
            ifShow: hasPermission('121040001000005'),
            onClick: async () => {
              const { data } = await reqHistoryList(row.id);
              historyDataList.value = data.map((item) => {
                if (item.detail) {
                  item.detail = JSON.parse(item.detail);
                }
                return item;
              });
              showHistory.value = true;
            },
          },
          {
            label: t('重新调查'),
            ifShow: row.exceptionStatus.value !== '2' && hasPermission('121040001000006'),
            onClick: () => {
              showHandling.value = true;
              showData.value = {
                title: t('异常重新调查原因'),
                type: 'reinvestigate',
              };
              rowData.value = row;
            },
          },
        ];
      },
    },
  ],
});
const getTableList = async (params) => {
  return await getExceptionPage({ ...params });
};
const resetData = () => {
  tableRef.value.fetchData();
};
defineExpose({
  resetData,
});
</script>
