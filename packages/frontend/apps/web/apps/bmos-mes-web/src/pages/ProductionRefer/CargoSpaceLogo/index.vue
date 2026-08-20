<template>
  <!-- 货位日志 -->
  <BMPageComponent
    :showAllAddIcon="false"
    :showAction="false"
    :treeData="treeData"
    :defaultSelectedKeys="['all']"
    :fieldNames="{
      title: 'name',
      key: 'id',
    }"
    :treeField="treeField"
    :rowKeys="['id']"
    :tableFields="tableFields"
    :pageSizeChangeToFirsts="[true]"
    :requests="[getCargoSpaceLogPage]"
    :titles="[t('货位日志')]"
    :columns="[columns]"
    :search="[true, false]"
    :formProps="[
      {
        showAdvancedButton: true,
        fieldMapToTime: [['operateTime', ['startDate', 'endDate'], 'YYYY-MM-DD']],
      },
      {},
    ]"
    @reset="reset"></BMPageComponent>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { getCargoSpaceLogPageApi } from '@/services';
  import { BMPageComponent } from '@bmos/components';
  import { useTable, useTree } from './hooks';
  import dayjs from 'dayjs';
  const { treeField, treeData, fetchTreeData } = useTree();
  const { columns, reset } = useTable();
  const tableFields = ref([
    {
      field: {
        id: 'id',
      },
    },
  ]);
  fetchTreeData();

  const getCargoSpaceLogPage = async (params: any) => {
    const data = {
      startDate: dayjs().startOf('month').format('YYYY-MM-DD'),
      endDate: dayjs().endOf('month').format('YYYY-MM-DD'),
      ...params,
      materialPositionId: params.materialPositionId === 'all' ? void 0 : params.materialPositionId,
    };
    const res = await getCargoSpaceLogPageApi(data);
    return res;
  };
</script>
<style scoped lang="less"></style>
