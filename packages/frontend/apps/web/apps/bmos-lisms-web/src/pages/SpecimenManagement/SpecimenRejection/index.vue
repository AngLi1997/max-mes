<!-- 标本拒收 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['sampleNo']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="[rowSelection]"
    :showHeader="[false]"
    :showToolBars="[true]"
    :tableFields="[
      {
        default: { receiveStatus },
      },
    ]"
    :paginations="[paginationBig]"
    :formProps="[formFirstProps]"
    :requests="[getSampleRejectPage as DataRequestFn]"
    :columns="[columnsFirst]"
    :show-indexs="[true]">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="receiveStatus" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="210020003000001"
        :disabled="selectedRows.length === 0"
        type="primary"
        @click="() => openRecept(selectedRows)">
        {{ t('拒收') }}
      </Button>
    </template>
  </BMPageComponent>
  <RejectModal
    ref="rejectModalRef"
    @submitSuccess="
      () => {
        pageRef?.fetchData();
        clearSelect();
      }
    " />
  <RejectDetail ref="rejectDetailRef" />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { useTable } from './hooks';
  import { getSampleRejectPage } from '@/services';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import RemarkModal from '@/components/RemarkModal';
  import { RejectModal, RejectDetail } from './components';
  import { useRowSelection } from '@/hooks';
  import { paginationBig } from '@/utils';

  defineOptions({
    name: 'SpecimenRejection',
    inheritAttrs: false,
  });

  const receiveStatus = ref('RECEIVING');

  const options = [
    { label: t('待接收'), value: 'RECEIVING' },
    { label: t('已拒收'), value: 'REJECTED' },
    { label: t('全部'), value: '' },
  ];

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (record: any) => {
      return {
        disabled: record?.receiveStatus?.value !== 'RECEIVING',
      };
    },
  });

  // 拒收弹窗
  const rejectModalRef = ref<InstanceType<typeof RejectModal> | null>(null);
  const openRecept = (rows: any) => {
    rejectModalRef.value?.openModal(rows);
  };
  // 拒收详情弹窗
  const rejectDetailRef = ref<InstanceType<typeof RejectDetail> | null>(null);

  const { pageRef, columnsFirst, formFirstProps, remarkModalOpen, remarkDetails } = useTable(
    openRecept,
    (record: any) => {
      rejectDetailRef.value?.openModal(record);
    },
  );
</script>

<style lang="less" scoped></style>
