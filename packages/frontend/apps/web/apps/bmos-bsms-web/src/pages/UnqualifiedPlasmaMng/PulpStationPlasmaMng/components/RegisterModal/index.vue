<!-- 登记不合格信息弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('登记不合格信息')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { unqualifiedPlasmaRegister } from '@/services';
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { message } from 'ant-design-vue';
  import { BMModalForm } from '@bmos/components';

  const open = ref(false);

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps } = useForm();

  const openModal = async () => {
    open.value = true;
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      return await unqualifiedPlasmaRegister(formModal);
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
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
