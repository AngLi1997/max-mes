<!-- 文件模板审核 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :hideRightTree="true"
    :paginations="[paginationBig]"
    :formProps="[formFirstProps]"
    :rowSelections="[rowSelection]"
    :tableFields="[
      {
        default: { auditStatus: segmentedValue },
      },
    ]"
    :requests="[dataRequest as any]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="segmentedValue" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="210080006000001" type="primary" :disabled="disAudit" @click="openAuditModal()">
        {{ t('审核') }}
      </Button>
    </template>
  </BMPageComponent>
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
  <AuditModal v-model:modalOpen="auditModalOpen" :tableData="selectedRows" @ok="updateTableData" />
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMPageComponent, Recordable } from '@bmos/components';
  import { useTable } from './hooks';
  import { postConfigFileTemplateChildPage } from '@/services';
  import { paginationBig } from '@/utils';
  import RemarkModal from '@/components/RemarkModal';
  import { useRowSelection } from '@/hooks';
  import AuditModal from './components/AuditModal.vue';

  defineOptions({
    name: 'FileTemplateAudit',
    inheritAttrs: false,
  });

  const { auditStatusDict } = getDicts();

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (_record: any) => {
      return {
        disabled: false,
      };
    },
  });

  const dataRequest = async (params: Recordable) => {
    clearSelect();
    return await postConfigFileTemplateChildPage(params);
  };

  const segmentedValue = ref('TO_AUDIT');

  const options = [...auditStatusDict, { label: t('全部'), value: '' }];
  // 禁止审核
  const disAudit = computed(() => {
    return !selectedRows.value.some((item: any) => item.auditStatus?.value === 'TO_AUDIT');
  });
  const { columnsFirst, pageRef, formFirstProps, remarkModalOpen, remarkDetails } = useTable();

  const auditModalOpen = ref<boolean>(false);
  const openAuditModal = () => {
    auditModalOpen.value = true;
  };

  const updateTableData = () => {
    clearSelect();
    pageRef.value?.fetchData(0);
  };
</script>
