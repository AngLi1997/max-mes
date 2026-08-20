<template>
  <BMModalForm
    ref="myFormRef"
    v-model:open="open"
    :title="t('新增批次配料')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"
    @cancelModal="closeModal"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks/useForm';
  import { BMModalForm } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { reqWeighingRequirementsCreate } from '@/services';
  import router from '@/router';

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const { myFormRef, formProps } = useForm();

  const submit = async (formModel: any) => {
    try {
      const { data } = await reqWeighingRequirementsCreate(formModel);
      message.success(t('操作成功'));
      closeModal();
      router.push({
        name: 'weighing-requirements-detail',
        query: { id: data, type: 'edit' },
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const closeModal = () => {
    open.value = false;
  };

  defineExpose({
    closeModal,
  });
</script>

<style lang="less" scoped>
  :deep(.delete-icon) {
    color: var(--bmos-danger-color);
  }
</style>
