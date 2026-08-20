<!-- 物料基础信息 -->
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
    :requests="[getMaterialInfoPage as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div style="display: flex; align-items: center; gap: 8px">
        <Button
          v-hasAuth="210060002000001"
          type="primary"
          @click="() => openModal(OperationStatusMap.ADD, selectedRows)">
          {{ t('新增') }}
        </Button>
        <Button
          v-hasAuth="210060002000002"
          danger
          :disabled="selectedRows.length === 0"
          @click="() => deleteSupplier(selectedRows)">
          {{ t('删除') }}
        </Button>
      </div>
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
  import { getMaterialInfoPage, deleteMaterialInfo } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import RemarkModal from '@/components/RemarkModal';
  import { MaterialModal } from './components';
  import { useRowSelection, useWarn } from '@/hooks';
  import { paginationBig } from '@/utils';
  import { message } from 'ant-design-vue';
  import { OperationStatusMap } from '@/types';

  defineOptions({
    name: 'MaterialInfo',
    inheritAttrs: false,
  });

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (_record: any) => {
      return {
        disabled: false,
      };
    },
  });

  const materialModalRef = ref<InstanceType<typeof MaterialModal> | null>(null);

  const openModal = (type: OperationStatusMap, row: any) => {
    materialModalRef.value?.openModal(type, row);
  };

  const { warnModal } = useWarn();
  // 删除
  const deleteSupplier = (rows: any[]) => {
    warnModal(t('是否删除该数据?'), {
      async onOk() {
        try {
          const idList = rows.map((item: any) => item.id);
          await deleteMaterialInfo({ idList, materialNo: rows.map((item: any) => item.materialNo).join(',') });
          message.success(t('操作成功'));
          pageRef.value?.fetchData();
          clearSelect();
          return Promise.resolve();
        } catch (error: any) {
          message.error(error.message);
          return Promise.reject();
        }
      },
    });
  };

  const { pageRef, columnsFirst, formFirstProps, remarkModalOpen, remarkDetails } = useTable(openModal, deleteSupplier);
</script>

<style lang="less" scoped></style>
