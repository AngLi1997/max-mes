<!-- 编辑颜色 -->
<template>
  <div>
    <BMModalForm
      ref="modalFormRef"
      v-model:open="open"
      :title="t('编辑')"
      :formProps="formProps"
      :submit="submit"></BMModalForm>
  </div>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { useForm } from './hooks/useForm';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';
  import { updateImmunetype } from '@/services';
  import { useDict } from '@/stores/dictStore';

  const open = ref(false);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const emits = defineEmits(['submitSuccess']);

  const request = async (formModal: any) => {
    try {
      return await updateImmunetype({
        id: formModal.id,
        colour: formModal.colour,
      });
    } catch (error) {
      return Promise.reject(error);
    }
  };

  const submit = async (formModal: any) => {
    try {
      await request(formModal);
      message.success(t('操作成功'));
      const { setImmunityTypeDict } = useDict();
      await setImmunityTypeDict();
      emits('submitSuccess');
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const openModal = async (data: any) => {
    open.value = true;
    await nextTick();
    setFormModels(data);
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
