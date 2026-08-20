<!-- 检验数据中心 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :search="[true]"
    :hideRightTree="true"
    :paginations="[paginationBig]"
    :rowKeys="['sampleNo']"
    :tableFields="[
      {
        default: { publishStatus: segmentedValue },
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
      <Publish v-model:modalOpen="publishModal" :tableData="selectedRows" @ok="updateTable" />
      <BatchPublish v-model:modalOpen="batchPublishModal" />
      <Print v-model:modalOpen="printModal" />
      <UnqualifiedModal v-model:modalOpen="unqualifiedModalOpen" :tableData="[firstRowData]" />
      <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
      <Button
        v-if="segmentedValue === 'TO_PUBLISH' || segmentedValue === ''"
        v-hasAuth="210030002000001"
        :disabled="!selectedRows.length"
        type="primary"
        @click="openPublish">
        {{ t('发布') }}
      </Button>
      <Button
        v-if="segmentedValue === 'TO_PUBLISH' || segmentedValue === ''"
        v-hasAuth="210030002000002"
        type="primary"
        @click="openBatchPublish">
        {{ t('批量发布') }}
      </Button>

      <Button v-hasAuth="210030002000003" @click="openPrint">
        {{ t('打印检测记录单') }}
      </Button>
    </template>
  </BMPageComponent>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { BMPageComponent } from '@bmos/components';
  import { paginationBig } from '@/utils';
  import { useTable } from './hooks';
  import { postInspectTaskList } from '@/services';
  import { InspectionResultEnum, PublishStatusEnum } from '@/types';
  import { useRowSelection } from '@/hooks';
  import { message } from 'ant-design-vue';
  import { arrayToObject } from '@bmos/utils';
  import Publish from './components/Publish.vue';
  import BatchPublish from './components/BatchPublish.vue';
  import Print from './components/Print.vue';
  import UnqualifiedModal from './components/UnqualifiedModal.vue';
  import RemarkModal from '@/components/RemarkModal';

  defineOptions({
    name: 'TestingDataCenter',
    inheritAttrs: false,
  });

  const { PublishStatusDict } = getDicts();
  const { getConfigEnumsValueByParamId } = useConfig();

  const { columnsFirst, pageRef, formFirstProps, unqualifiedModalOpen, firstRowData, remarkDetails, remarkModalOpen } =
    useTable();
  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (record: any) => {
      return {
        disabled: record.publishStatus?.value !== PublishStatusEnum.TO_PUBLISH,
      };
    },
  });
  // 发布按钮状态
  // const publishStatus = computed(() => {
  //   return (
  //     selectedRows.value.length &&
  //     selectedRows.value.every((item: any) => item.publishStatus?.value === PublishStatusEnum.TO_PUBLISH)
  //   );
  // });
  // const inspectItemListDataIndex = ref<string[]>([]);
  const dataRequest = async (params: any) => {
    try {
      const res = await postInspectTaskList({
        ...params,
        fetchInspectDataDetail: true,
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

  const segmentedValue = ref('TO_INSPECT');
  const options = [...PublishStatusDict, { label: t('全部'), value: '' }];

  // 发布弹窗
  const publishModal = ref<boolean>(false);
  const openPublish = () => {
    if (selectedRows.value.length === 0) {
      message.warning(t('请选择需要发布的数据'));
      return;
    }
    publishModal.value = true;
  };

  // 批量发布弹窗
  const batchPublishModal = ref<boolean>(false);
  const openBatchPublish = () => {
    batchPublishModal.value = true;
  };

  // 打印弹窗
  const printModal = ref<boolean>(false);
  const openPrint = () => {
    printModal.value = true;
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
