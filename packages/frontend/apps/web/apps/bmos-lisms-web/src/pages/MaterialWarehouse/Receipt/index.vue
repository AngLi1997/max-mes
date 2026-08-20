<!-- 物料接收 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowSelections="[rowSelection]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getMaterialReceivePage as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Button
        v-hasAuth="210060003000001"
        :disabled="!selectedRow.id"
        type="primary"
        @click="() => openModal(selectedRow)">
        {{ t('物料接收') }}
      </Button>
    </template>
  </BMPageComponent>
  <MaterialModal
    ref="materialModalRef"
    @submitSuccess="
      () => {
        pageRef?.fetchData();
        clearSelect();
      }
    " />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { getMaterialReceivePage } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import RemarkModal from '@/components/RemarkModal';
  import { MaterialModal } from './components';
  import { useRowSelection } from '@/hooks';
  import { paginationBig } from '@/utils';

  defineOptions({
    name: 'MaterialReceipt',
    inheritAttrs: false,
  });

  const { selectedRow, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: true,
    getCheckboxProps: (_record: any) => {
      return {
        disabled: false,
      };
    },
  });

  const materialModalRef = ref<InstanceType<typeof MaterialModal> | null>(null);

  const openModal = (row: any) => {
    materialModalRef.value?.openModal(row);
  };

  const { pageRef, columnsFirst, formFirstProps, remarkModalOpen, remarkDetails } = useTable(openModal);
</script>

<style lang="less" scoped></style>
