<!-- 物料退货审核 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['auditId']"
    :search="[true]"
    :hideRightTree="true"
    :rowSelections="[rowSelection]"
    :showHeader="[false]"
    :showToolBars="[true]"
    :tableFields="[
      {
        default: { auditStatus },
      },
    ]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getMaterialUseReturnAuditPage as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="auditStatus" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="210060011000001"
        :disabled="selectedRows.length === 0"
        type="primary"
        @click="() => openAudit(selectedRows)">
        {{ t('审核') }}
      </Button>
    </template>
  </BMPageComponent>
  <AuditModal
    ref="auditModalRef"
    @submitSuccess="
      () => {
        pageRef?.fetchData();
        clearSelect();
      }
    " />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { getMaterialUseReturnAuditPage } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import RemarkModal from '@/components/RemarkModal';
  import { AuditModal } from './components';
  import { paginationBig } from '@/utils';
  import { useRowSelection } from '@/hooks';

  defineOptions({
    name: 'MaterialGoodsReturnAudit',
    inheritAttrs: false,
  });

  const { auditStatusDict } = getDicts();

  const auditStatus = ref('TO_AUDIT');

  const options = [...auditStatusDict, { label: t('全部'), value: '' }];

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (record: any) => {
      return {
        disabled: record?.auditStatus?.value !== 'TO_AUDIT',
      };
    },
  });

  const auditModalRef = ref<InstanceType<typeof AuditModal> | null>(null);

  const openAudit = (rows: any) => {
    auditModalRef.value?.openModal(rows);
  };

  const { pageRef, columnsFirst, formFirstProps, remarkModalOpen, remarkDetails } = useTable();
</script>

<style lang="less" scoped></style>
