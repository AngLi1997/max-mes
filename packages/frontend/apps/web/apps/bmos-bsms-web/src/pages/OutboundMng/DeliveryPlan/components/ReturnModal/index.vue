<!-- 按批号/托盘号退回 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="actionType == 'batch' ? t('按批次退回') : t('按托盘退回')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { useModalForm } from './hooks/useModalForm';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { batchBackOutboundPlasma } from '@/services';

  const open = ref(false);

  const actionType = ref<'batch' | 'tray'>('batch');

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels, changeType } = useModalForm();

  const openModal = async (batchNo: string, type: 'batch' | 'tray') => {
    actionType.value = type;
    changeType(type);
    open.value = true;
    await nextTick();
    setFormModels({
      batchNo,
    });
  };

  const cancel = () => {
    setFormModels({
      containerNo: undefined,
      sortingNo: undefined,
    });
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      return await batchBackOutboundPlasma(formModal);
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
