<!-- 物料抽检放行 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['useFormIdentify']"
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
    :requests="[getMaterialUseSpotCheckPassPage as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="auditStatus" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="210060008000001"
        :disabled="!selectedRow.useFormIdentify"
        type="primary"
        @click="() => openAudit(selectedRow)">
        {{ t('抽检放行') }}
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
  <EditModal
    ref="editModalRef"
    @submitSuccess="
      () => {
        pageRef?.fetchData();
        clearSelect();
      }
    " />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { getMaterialUseSpotCheckPassPage } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import RemarkModal from '@/components/RemarkModal';
  import { AuditModal, EditModal } from './components';
  import { paginationBig } from '@/utils';
  import { useRowSelection } from '@/hooks';

  defineOptions({
    name: 'MaterialSpotCheckRelease',
    inheritAttrs: false,
  });

  const { auditStatusDict } = getDicts();

  const auditStatus = ref('TO_AUDIT');

  const options = [...auditStatusDict, { label: t('全部'), value: '' }];

  const { selectedRow, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: true,
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

  const editModalRef = ref<InstanceType<typeof EditModal> | null>(null);

  const { pageRef, columnsFirst, formFirstProps, remarkModalOpen, remarkDetails } = useTable(async (record: any) => {
    await editModalRef.value?.openModal(record);
  });
</script>

<style lang="less" scoped></style>
