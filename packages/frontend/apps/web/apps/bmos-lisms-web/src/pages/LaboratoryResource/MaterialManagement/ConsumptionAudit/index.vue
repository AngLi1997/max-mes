<!-- 领用库消耗审核 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['identify']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowSelections="[rowSelection]"
    :formProps="[formFirstProps]"
    :table-fields="[
      {
        default: {
          recordSource: RecordSourceAuditTypeEnum.OUT_CONSUME,
          targetWarehouseId,
          auditStatus,
        },
      },
    ]"
    :paginations="[paginationBig]"
    :requests="[loadData as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="auditStatus" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Select v-model:value="targetWarehouseId" style="width: 250px" :options="targetWarehouseList" />
      <Button
        v-hasAuth="210050003000001"
        type="primary"
        :disabled="selectedRows.length === 0"
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
  import { getLaboratoryUseAuditPage, getLaboratoryUseInventoryList } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import RemarkModal from '@/components/RemarkModal';
  import { AuditModal } from './components';
  import { useRowSelection } from '@/hooks';
  import { paginationBig } from '@/utils';
  import { Segmented } from 'ant-design-vue';
  import { RecordSourceAuditTypeEnum } from '@/types';

  defineOptions({
    name: 'ConsumptionAudit',
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
