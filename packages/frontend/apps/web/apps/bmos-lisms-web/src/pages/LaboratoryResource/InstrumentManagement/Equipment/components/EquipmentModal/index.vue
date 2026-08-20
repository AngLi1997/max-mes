<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="modalType === OperationStatusMap.ADD ? t('仪器设备新增') : t('仪器设备编辑')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"
    @cancelModal="closeModal"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { saveLaboratoryInstrument, updateLaboratoryInstrument } from '@/services';
  import { useForm } from './hooks';
  import { BMModalForm } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { OperationStatusMap } from '@/types';

  const modalType = ref<OperationStatusMap>(OperationStatusMap.ADD);

  const emit = defineEmits(['submitSuccess']);

  const open = ref(false);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const openModal = async (type: OperationStatusMap, row?: any) => {
    modalType.value = type;
    open.value = true;
    if (type === OperationStatusMap.EDIT) {
      await nextTick();
      setFormModels(row);
    }
  };

  const submit = async (formModel: any) => {
    try {
      const params = {
        ...formModel,
      };
      if (modalType.value === OperationStatusMap.EDIT) {
        await updateLaboratoryInstrument(params);
      } else {
        await saveLaboratoryInstrument(params);
      }
      message.success(t('操作成功'));
      closeModal();
      emit('submitSuccess');
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const closeModal = () => {
    open.value = false;
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
