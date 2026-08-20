<!-- 出库标本明细弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('出库标本明细')"
    wrapClassName="modalSizeLarge"
    :show-ok-button="false">
    <template #formBefore>
      <div style="height: 50vh">
        <BMTable
          ref="tableRef"
          :data-request="loadData"
          :columns="columns"
          :formProps="formProps"
          row-key="id"
          :show-tool-bar="true"
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          :pagination="paginationSmall">
          <template #tableHeaderTitle>
            <Button>{{ t('导出') }}</Button>
          </template>
        </BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import { getSampleOutWarehouseDetail } from '@/services';
  import { BMModalForm, BMTable, DataRequestFn, ModalFormInstance } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const modalFormRef = ref<ModalFormInstance>();
  const { tableRef, columns, formProps } = useTable();

  const info = ref<any>({});

  const openModal = async (row: any) => {
    info.value = row;
    open.value = true;
    await nextTick();
  };

  const loadData: DataRequestFn = async (params: any, onChangeParams: any): Promise<any> => {
    const datas = {
      ...params,

      outPlanBatchNo: info.value?.outPlanBatchNo,
      sortingPlanBatchNo: info.value?.sortingPlanBatchNo,
      palletNo: info.value?.palletNo,
    };
    const { data } = await getSampleOutWarehouseDetail(datas);
    return {
      data: data.voList,
    };
  };

  const cancel = () => {
    info.value = {};
    open.value = false;
  };

  defineExpose({ openModal, cancel });
</script>

<style scoped></style>
