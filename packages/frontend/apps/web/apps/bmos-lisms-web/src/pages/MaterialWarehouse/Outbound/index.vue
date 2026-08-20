<!-- 物料出库 -->
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
        default: { outStatus },
      },
    ]"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getMaterialUseOutPage as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="outStatus" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Button
        v-hasAuth="210060012000001"
        :disabled="selectedRows.length === 0"
        type="primary"
        @click="() => openAudit(selectedRows)">
        {{ t('出库') }}
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
  import { getMaterialUseOutPage } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import RemarkModal from '@/components/RemarkModal';
  import { AuditModal } from './components';
  import { paginationBig } from '@/utils';
  import { useRowSelection } from '@/hooks';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'MaterialOutbound',
    inheritAttrs: false,
  });

  const { outStatusDict } = getDicts();

  const outStatus = ref('WAITING_DELIVERY');

  const options = [...outStatusDict, { label: t('全部'), value: '' }];

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (record: any) => {
      return {
        disabled: record?.outStatus?.value !== 'WAITING_DELIVERY',
      };
    },
  });

  const auditModalRef = ref<InstanceType<typeof AuditModal> | null>(null);

  const openAudit = (rows: any) => {
    // 判断使用类型是否一致
    if (rows?.length > 1) {
      const useType = rows[0]?.useType?.value;
      const flag = rows.every((item: any) => item.useType?.value === useType);
      if (!flag) {
        message.error(t('只允许相同使用类别批量出库'));
        return;
      }
    }

    auditModalRef.value?.openModal(rows);
  };

  const { pageRef, columnsFirst, formFirstProps, remarkModalOpen, remarkDetails } = useTable();
</script>

<style lang="less" scoped></style>
