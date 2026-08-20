<!-- 按批号/托盘号退回 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('标本退回')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { useModalForm } from './hooks/useModalForm';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { batchBackSampleDeliveryPlan } from '@/services';

  const open = ref(false);

  const actionType = ref<'batch' | 'tray'>('batch');

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels, changeType } = useModalForm();

  const openModal = async (outPlanBatchNo: string, type: 'batch' | 'tray') => {
    actionType.value = type;
    changeType(type);
    open.value = true;
    await nextTick();
    setFormModels({
      outPlanBatchNo,
    });
  };

  const cancel = () => {
    setFormModels({
      sortingPlanBatchNo: undefined,
      palletNo: undefined,
    });
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      return await batchBackSampleDeliveryPlan(formModal);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      await request(formModal);
      message.success(t('操作成功'));
      emits('submitSuccess');
      cancel();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
