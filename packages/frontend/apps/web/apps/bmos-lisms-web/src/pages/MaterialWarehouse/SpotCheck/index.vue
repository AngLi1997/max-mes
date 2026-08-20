<!-- 物料抽检 -->
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
        default: { status },
      },
    ]"
    :paginations="[paginationBig]"
    :formProps="[formFirstProps]"
    :requests="[getMaterialUseSpotCheckPage as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="status" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="210060006000001"
        :disabled="selectedRows.length === 0"
        type="primary"
        @click="() => openModal('submit', selectedRows)">
        {{ t('提交抽检') }}
      </Button>
      <Button
        v-hasAuth="210060006000002"
        :disabled="selectedRows.length === 0"
        @click="() => openModal('cancel', selectedRows)">
        {{ t('撤销抽检') }}
      </Button>
    </template>
  </BMPageComponent>
  <SpotModal
    ref="spotModalRef"
    @submitSuccess="
      () => {
        pageRef?.fetchData();
        clearSelect();
      }
    " />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { useTable } from './hooks';
  import { getMaterialUseSpotCheckPage } from '@/services';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import RemarkModal from '@/components/RemarkModal';
  import { SpotModal } from './components';
  import { useRowSelection } from '@/hooks';
  import { paginationBig } from '@/utils';
  import { SpotCheckStatusEnum } from '@/types';

  defineOptions({
    name: 'MaterialSpotCheck',
    inheritAttrs: false,
  });

  const { spotStatusDict } = getDicts();

  const status = ref(SpotCheckStatusEnum.WAIT_SUBMIT);

  const options = [...spotStatusDict, { label: t('全部'), value: '' }];

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (record: any) => {
      return {
        disabled: record?.status?.value !== SpotCheckStatusEnum.WAIT_SUBMIT,
      };
    },
  });

  // 抽检弹窗
  const spotModalRef = ref<InstanceType<typeof SpotModal> | null>(null);
  const openModal = (type: any, rows: any) => {
    spotModalRef.value?.openModal(type, rows);
  };

  const { pageRef, columnsFirst, formFirstProps, remarkModalOpen, remarkDetails } = useTable();
</script>

<style lang="less" scoped></style>
