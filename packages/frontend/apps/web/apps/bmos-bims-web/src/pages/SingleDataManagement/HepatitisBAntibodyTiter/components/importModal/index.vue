<!-- 读取 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('乙肝抗体效价上传')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { message } from 'ant-design-vue';
  import { readTiter } from '@/services';
  import { BMModalForm } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps } = useForm();

  const openModal = async () => {
    open.value = true;
  };

  const cancel = () => {
    open.value = false;
    // tableRef.value?.fetchData();
  };

  const request = async (formModal: any) => {
    try {
      const formData = new FormData();
      formData.append('file', formModal.file[0].originFileObj);
      formData.append('reagentBatchNo', formModal.reagentBatchNo);
      formData.append('qualityControllerBatchNo', formModal.qualityControllerBatchNo);
      formData.append('titerType', '2');

      return await readTiter(formData);
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
