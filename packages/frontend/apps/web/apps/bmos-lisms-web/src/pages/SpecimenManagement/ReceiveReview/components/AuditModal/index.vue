<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('标本接收审核')"
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
          :scroll="{ x: 744, y: 400 }"
          :showRefresh="false"></BMTable>
      </div>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="1003" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { getSampleReceiveAudit } from '@/services';
  import { Sign } from '@/components/Sign';
  import { useTable, useForm } from './hooks';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { message } from 'ant-design-vue';

  const emit = defineEmits(['submitSuccess']);

  const signRef = ref<InstanceType<typeof Sign>>();

  const open = ref(false);

  const { modalFormRef, formProps } = useForm();
  const { tableRef, columns } = useTable();
  const tableData = ref<any[]>([]);

  const openModal = async (row: any) => {
    tableData.value = [...row];
    open.value = true;
  };

  const submitParams = ref<any>({});

  const submit = async (formModel: any) => {
    try {
      submitParams.value = {
        auditIds: tableData.value.map((item: any) => item.auditId),
        ...formModel,
        sampleBatchNo: tableData.value.map((item: any) => item.batchNo).join(','),
      };
      await signRef.value?.openSign(submitParams.value);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = async (signUrl: string) => {
    try {
      await getSampleReceiveAudit({
        ...submitParams.value,
        auditSignatureUrl: signUrl,
      });
      message.success(t('操作成功'));
      closeModal();
      emit('submitSuccess');
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
