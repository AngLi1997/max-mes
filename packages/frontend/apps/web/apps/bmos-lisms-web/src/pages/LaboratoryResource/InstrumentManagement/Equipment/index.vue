<!-- 仪器设备管理 -->
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
    :paginations="[paginationBig]"
    :requests="[getLaboratoryInstrumentPage as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0>
      <div style="display: flex; align-items: center; gap: 8px">
        <Button v-hasAuth="210050007000001" type="primary" @click="() => openModal(OperationStatusMap.ADD, {})">
          {{ t('新增') }}
        </Button>
        <Button
          v-hasAuth="210050007000002"
          danger
          :disabled="selectedRows.length === 0"
          @click="() => deleteEquipment(selectedRows)">
          {{ t('删除') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <EquipmentModal
    ref="equipmentModalRef"
    @submitSuccess="
      () => {
        pageRef?.fetchData();
        clearSelect();
      }
    " />
  <RemarkModal v-model:modalOpen="remarkModalOpen" :details="remarkDetails" />
</template>

<script setup lang="ts">
  import { deleteLaboratoryInstrument, getLaboratoryInstrumentPage } from '@/services';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import RemarkModal from '@/components/RemarkModal';
  import { EquipmentModal } from './components';
  import { useRowSelection, useWarn } from '@/hooks';
  import { paginationBig } from '@/utils';
  import { OperationStatusMap } from '@/types';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'InstrumentEquipmentManagement',
    inheritAttrs: false,
  });

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (record: any) => {
      return {
        disabled: record.status === 'AUDITED',
      };
    },
  });

  const equipmentModalRef = ref<InstanceType<typeof EquipmentModal> | null>(null);

  const openModal = (type: OperationStatusMap, row: any) => {
    equipmentModalRef.value?.openModal(type, row);
  };

  const { warnModal } = useWarn();

  const deleteEquipment = async (rows: any[]) => {
    warnModal(t('是否删除该数据?'), {
      async onOk() {
        try {
          const idList = rows.map((item: any) => item.identify);
          await deleteLaboratoryInstrument({
            idList,
            instrumentNo: rows.map((item: any) => item.instrumentNo).join(','),
          });
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

  const { pageRef, columnsFirst, formFirstProps, remarkModalOpen, remarkDetails } = useTable(
    openModal,
    deleteEquipment,
  );
</script>

<style lang="less" scoped></style>
