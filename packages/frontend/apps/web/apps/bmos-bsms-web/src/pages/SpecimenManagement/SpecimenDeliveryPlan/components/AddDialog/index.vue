<!-- 新增标本出库计划 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('计划信息')"
    :formProps="formProps"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { useModalForm } from './hooks/useModalForm';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { createSampleDeliveryPlan } from '@/services';

  const open = ref(false);

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps } = useModalForm();

  const openModal = async () => {
    open.value = true;
    await nextTick();
    // setFormModels({});
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      return await createSampleDeliveryPlan(formModal);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      const data = formModal?.outPlanBatchNo;
      await request(formModal);
      message.success(t('操作成功'));
      console.log('outPlanBatchNo', data);
      emits('submitSuccess', data);
      cancel();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
