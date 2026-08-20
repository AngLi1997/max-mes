<!-- 检验数据审核 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :search="[true]"
    :hideRightTree="true"
    :paginations="[paginationBig]"
    :rowKeys="['publishId']"
    :tableFields="[
      {
        default: { auditStatus: segmentedValue },
      },
    ]"
    :rowClassNames="[
      (record: any) => {
        return record.inspectResult?.value === InspectionResultEnum.UNQUALIFIED ? 'unqualified-row' : 'qualified-row';
      },
    ]"
    :rowSelections="[rowSelection]"
    :formProps="[formFirstProps]"
    :requests="[dataRequest as any]"
    :columns="[columnsFirst]"
    :show-indexs="[true]">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="segmentedValue" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Audit v-model:modalOpen="auditModal" :tableData="selectedRows" @ok="updateTable" />
      <BatchAudit v-model:modalOpen="batchAuditModal" />
      <UnqualifiedModal v-model:modalOpen="unqualifiedModalOpen" :tableData="[firstRowData]" />
      <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />

      <Button v-hasAuth="210030003000001" :disabled="!selectedRows.length" type="primary" @click="openAudit">
        {{ t('审核') }}
      </Button>
      <Button v-hasAuth="210030003000002" type="primary" @click="openBatchAudit">
        {{ t('批量审核') }}
      </Button>
    </template>
  </BMPageComponent>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { BMPageComponent } from '@bmos/components';
  import { paginationBig } from '@/utils';
  import { useTable } from './hooks';
  import { postInspectDatapubCheck } from '@/services';
  import { InspectionResultEnum, PublishStatusEnum } from '@/types';
  import { useRowSelection } from '@/hooks';
  import { message } from 'ant-design-vue';
  import { arrayToObject } from '@bmos/utils';
  import Audit from './components/Audit.vue';
  import BatchAudit from './components/BatchAudit.vue';
  import { useConfig } from '@/stores';
  import UnqualifiedModal from './components/UnqualifiedModal.vue';
  import RemarkModal from '@/components/RemarkModal';

  defineOptions({
    name: 'InspectionDataAudit',
    inheritAttrs: false,
  });

  const { getConfigEnumsValueByParamId } = useConfig();
  const { auditStatusDict } = getDicts();

  const { columnsFirst, pageRef, formFirstProps, unqualifiedModalOpen, firstRowData, remarkDetails, remarkModalOpen } =
    useTable();
  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (record: any) => {
      return {
        disabled: record.auditStatus?.value !== PublishStatusEnum.TO_AUDIT,
      };
    },
  });

  // 审核按钮状态
  // const auditStatus = computed(() => {
  //   return (
  //     selectedRows.value.length &&
  //     selectedRows.value.every((item: any) => item.auditStatus?.value === PublishStatusEnum.TO_AUDIT)
  //   );
  // });
  const dataRequest = async (params: any) => {
    try {
      const res = await postInspectDatapubCheck({
        ...params,
        fetchSampleDetail: true,
      });
      return Promise.resolve({
        ...res,
        data: {
          ...res.data,
          list: res.data?.list?.map((item: any) => {
            return {
              ...item,
              ...(item.inspectItemList && arrayToObject(item.inspectItemList, 'code')),
            };
          }),
        },
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const updateTable = () => {
    clearSelect();
    pageRef.value?.fetchData(0);
  };

  const segmentedValue = ref('TO_AUDIT');
  const options = [...auditStatusDict, { label: t('全部'), value: '' }];

  // 审核弹窗
  const auditModal = ref<boolean>(false);
  const openAudit = () => {
    if (selectedRows.value.length === 0) {
      message.warning(t('请选择需要发布的数据'));
      return;
    }
    auditModal.value = true;
  };

  // 批量审核弹窗
  const batchAuditModal = ref<boolean>(false);
  const openBatchAudit = () => {
    batchAuditModal.value = true;
  };

  const unqualifiedColor = computed(() => {
    return getConfigEnumsValueByParamId('不合格数据颜色');
  });

  const qualifiedColor = computed(() => {
    return getConfigEnumsValueByParamId('合格数据颜色');
  });

  provide('page', { updateTable });
</script>

<style scoped lang="less">
  :deep(.unqualified-row) {
    color: v-bind(unqualifiedColor);
  }
  :deep(.qualified-row) {
    color: v-bind(qualifiedColor);
  }
</style>
