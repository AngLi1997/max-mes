<template>
  <div class="main bg-white">
    <BMTable
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      auto-height
      :autoHeightOffset="24"
      :scroll="{ x: 1144, y: 500 }"
      :formProps="formProps"
      :pagination="{ pageSize: 20 }"
      :show-tool-bar="false"
      showSearchBorder></BMTable>
  </div>
</template>
<script setup lang="tsx">
  import { getWeighLogPage } from '@/services';
  import { DataRequestFn, BMTable } from '@bmos/components';
  import { useTable, useForm } from './hooks';
  import dayjs from 'dayjs';
  // 表格配置
  const { columns } = useTable();
  const { formProps } = useForm();
  // 搜索数据
  const loadData: DataRequestFn = async (params): Promise<any> => {
    return await getWeighLogPage({
      startTime: dayjs().subtract(29, 'day').format('YYYY-MM-DD'),
      endTime: dayjs().format('YYYY-MM-DD'),
      ...params,
    });
  };
</script>
<style scoped lang="less">
  .main {
    height: 100%;
    min-height: 100%;
    background-color: white;
    padding: 0 var(--bmos-padding-small);
    padding-top: 16px;
  }
</style>
