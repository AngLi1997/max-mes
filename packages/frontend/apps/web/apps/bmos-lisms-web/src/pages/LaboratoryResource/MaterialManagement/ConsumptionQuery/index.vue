<!-- 领用库消耗查询 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['identify']"
    :search="[true]"
    :hideRightTree="true"
    :titles="[t('领用库消耗记录')]"
    :showHeader="[false]"
    :showToolBars="[true]"
    :table-fields="[
      {
        default: {
          recordSource: RecordSourceAuditTypeEnum.OUT_CONSUME,
          targetWarehouseId,
        },
      },
    ]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[loadData as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderToolbar0>
      <Select v-model:value="targetWarehouseId" style="width: 250px" :options="targetWarehouseList" />
    </template>
  </BMPageComponent>
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { getLaboratoryUseAuditPage, getLaboratoryUseInventoryList } from '@/services';
  import { useTable } from './hooks';
  import RemarkModal from '@/components/RemarkModal';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { RecordSourceAuditTypeEnum } from '@/types';
  import { paginationBig } from '@/utils';

  defineOptions({
    name: 'ConsumptionQuery',
    inheritAttrs: false,
  });

  const { pageRef, columnsFirst, formFirstProps, remarkModalOpen, remarkDetails } = useTable();

  const targetWarehouseId = ref<string>('');

  const targetWarehouseList = ref<any[]>([]);

  const loadData = async (params: any) => {
    if (!params.targetWarehouseId) {
      return {
        data: {
          list: [],
          total: 0,
        },
      };
    }
    return await getLaboratoryUseAuditPage(params);
  };

  const initTargetWarehouseList = async () => {
    const { data } = await getLaboratoryUseInventoryList({ flag: 0 });
    targetWarehouseList.value = data ?? [];
    targetWarehouseId.value = targetWarehouseList.value.length > 0 ? targetWarehouseList.value[0].value : '';
  };

  onBeforeMount(async () => {
    await initTargetWarehouseList();
  });

  onActivated(async () => {
    await initTargetWarehouseList();
  });
</script>

<style lang="less" scoped></style>
