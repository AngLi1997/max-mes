<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('标本接收')"
    wrapClassName="modalSizeLarge"
    :formProps="formProps"
    :submit="submit"
    @cancelModal="closeModal">
    <template #formBefore>
      <div :style="{ height: tableData.length > 4 ? '30vh' : 'auto' }">
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
  <Sign ref="signRef" :signatureAction="1002" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { getSampleReceiveApply } from '@/services';
  import { Sign } from '@/components/Sign';
  import { useTable, useForm } from './hooks';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { message } from 'ant-design-vue';

  const emit = defineEmits(['submitSuccess']);

  const signRef = ref<InstanceType<typeof Sign>>();

  const open = ref(false);

  const { modalFormRef, formProps, temperatureGroup, initSchemas } = useForm();
  const { tableRef, columns } = useTable();
  const tableData = ref<any[]>([]);

  const openModal = async (rows: any) => {
    tableData.value = [...rows];
    open.value = true;
    await nextTick();
    initSchemas();
  };

  const submitParams = ref<any>({});

  const submit = async (formModal: any) => {
    try {
      const transportTemperature: Object[] = [];
      temperatureGroup.value.forEach((item: any) => {
        transportTemperature.push({
          minTemperature: formModal?.[`minTemperature${item}`],
          maxTemperature: formModal?.[`maxTemperature${item}`],
        });
      });
      const batchNoList = tableData.value.map((item: any) => item.batchNo);
      submitParams.value = {
        applyRemark: formModal.applyRemark,
        transportArrivalTime: formModal.transportArrivalTime,
        transportTemperature,
        transportStatus: [formModal.transportStatus],
        batchNoList,
        sampleBatchNo: batchNoList.join(','),
      };
      await signRef.value?.openSign(submitParams.value);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = async (signUrl: string) => {
    try {
      await getSampleReceiveApply({
        ...submitParams.value,
        receiveSignatureUrl: signUrl,
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
