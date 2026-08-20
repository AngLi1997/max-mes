<!-- 颜色变更 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('编辑系统参数')"
    :formProps="formProps"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { useModalForm } from './hooks/useModalForm';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { getPlasmaColorById, updatePlasmaColor } from '@/services';

  const open = ref(false);

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useModalForm();

  const openModal = async (data: any) => {
    const res = await getPlasmaColorById(data.id);
    open.value = true;
    await nextTick();
    setFormModels({ ...res.data });
  };

  const request = async (formModal: any) => {
    try {
      const approvalParams = {
        id: formModal.id,
        colour: formModal.colour,
      };

      return await updatePlasmaColor(approvalParams);
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

  const cancel = () => {
    open.value = false;
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
