<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('检验报告撤回')"
    wrapClassName="modalSizeLarge"
    :formProps="formProps"
    :submit="submit"
    @cancelModal="closeModal">
    <template #formBefore>
      <div :style="{ height: tableData.length > 4 ? '40vh' : 'auto' }">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-source="tableData"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :scroll="{ x: 700, y: 400 }"
          :showRefresh="false"></BMTable>
      </div>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="1011" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useTable, useForm } from './hooks';
  import { Sign } from '@/components/Sign';
  import { message } from 'ant-design-vue';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { backReport } from '@/services';

  const emits = defineEmits(['submitSuccess']);

  const signRef = ref<InstanceType<typeof Sign>>();

  const open = ref(false);

  const { modalFormRef, formProps } = useForm();
  const { tableRef, columns } = useTable();
  const tableData = ref<any[]>([]);

  const openModal = async (rows: any) => {
    tableData.value = [...rows];
    open.value = true;
  };

  const submitParams = ref<any>({});

  const submit = async (formModel: any) => {
    try {
      const ids = tableData.value.map((item: any) => item.id);
      submitParams.value = {
        id: ids?.[0],
        sampleBatchNo: tableData.value?.map((item: any) => item.sampleBatchNo).join(','),
        ...formModel,
      };
      await signRef.value?.openSign(submitParams.value);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = async (signUrl: string) => {
    try {
      await backReport({
        ...submitParams.value,
        signature: signUrl,
      });
      message.success(t('操作成功'));
      closeModal();
      emits('submitSuccess');
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  const closeModal = () => {
    open.value = false;
    tableData.value = [];
  };

  defineExpose({
    openModal,
    closeModal,
  });
</script>

<style lang="less" scoped>
  :deep(.delete-icon) {
    color: var(--bmos-danger-color);
  }
</style>
