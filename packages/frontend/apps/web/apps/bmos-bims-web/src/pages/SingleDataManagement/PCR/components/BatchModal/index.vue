<!-- 批量发布/核对 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="actionType == 'publish' ? t('检验结果批量发布') : t('检验结果批量核对')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { message } from 'ant-design-vue';
  import { batchPublishPCR, batchCheckPCR } from '@/services';
  import { BMModalForm } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps } = useForm();

  const actionType = ref<'publish' | 'check'>('publish');

  const openModal = async (type: 'publish' | 'check') => {
    actionType.value = type;
    open.value = true;
  };

  const cancel = () => {
    open.value = false;
    // tableRef.value?.fetchData();
  };

  const request = async (formModal: any) => {
    try {
      if (actionType.value == 'publish') {
        return await batchPublishPCR(formModal);
      } else {
        return await batchCheckPCR(formModal);
      }
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      const { data } = await request(formModal);
      message.success(t('操作成功'));
      emits('submitSuccess', data, actionType.value);
      cancel();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
