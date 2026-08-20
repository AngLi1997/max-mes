<!-- 领用库入库查询 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['sampleNo']"
    :search="[true]"
    :hideRightTree="true"
    :titles="[t('物料入库记录')]"
    :showHeader="[false]"
    :showToolBars="[true]"
    :tableFields="[{ default: { targetWarehouseId } }]"
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
  import { getLaboratoryUseInPage } from '@/services';
  import { useTable } from './hooks';
  import RemarkModal from '@/components/RemarkModal';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { paginationBig } from '@/utils';
  import { useDict } from '@/stores';

  defineOptions({
    name: 'InventoryQuery',
    inheritAttrs: false,
  });

  /**
   * @description: 加载列表数据
   * @return {Promise<void>}
   */
  const loadData = async (params: any) => {
    if (!params.targetWarehouseId) {
      return {
        data: {
          list: [],
          total: 0,
        },
      };
    }
    return await getLaboratoryUseInPage(params);
  };

  const { pageRef, columnsFirst, formFirstProps, remarkModalOpen, remarkDetails } = useTable();

  const targetWarehouseId = ref<string>('');

  const targetWarehouseList = ref<any[]>([]);

  const { getDict } = useDict();

  const initTargetWarehouseList = async () => {
    const data = await getDict('领用库');
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
