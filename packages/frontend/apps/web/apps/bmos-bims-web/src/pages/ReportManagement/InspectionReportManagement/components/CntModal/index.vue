<!-- 不合格数量弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('不合格数量')"
    wrapClassName="modalSizeLarge"
    :show-ok-button="false">
    <template #formBefore>
      <div style="height: 50vh">
        <BMTable
          ref="tableRef"
          :data-request="loadData"
          :columns="columns"
          :formProps="formProps"
          auto-height
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
  import { getInspectionReportManagementNegativeInfo } from '@/services';
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

  const loadData: DataRequestFn = async (params: any): Promise<any> => {
    const datas = {
      ...params,
      id: info.value?.id,
    };
    return await getInspectionReportManagementNegativeInfo(datas);
  };

  const cancel = () => {
    info.value = {};
    open.value = false;
  };

  defineExpose({ openModal, cancel });
</script>

<style scoped></style>
