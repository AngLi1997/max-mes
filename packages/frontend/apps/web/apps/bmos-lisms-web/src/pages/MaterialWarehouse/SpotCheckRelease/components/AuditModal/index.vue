<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('抽检放行')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"
    @cancelModal="closeModal"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { materialUseSpotCheckPassSubmit } from '@/services';
  import { useForm } from './hooks';
  import { BMModalForm } from '@bmos/components';
  import { message } from 'ant-design-vue';

  const emit = defineEmits(['submitSuccess']);

  const open = ref(false);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const openModal = async (row: any) => {
    open.value = true;
    await nextTick();
    setFormModels({
      useFormIdentify: row.useFormIdentify,
      inWarehouseNo: row.inWarehouseNo,
      materialNo: row.materialNo,
      materialName: row.materialName,
      supplierName: row.supplierName,
      inWarehouseDate: row.inWarehouseDate,
    });
  };

  const submit = async (formModel: any) => {
    try {
      const params = {
        ...formModel,
        inWarehouseNo: formModel.inWarehouseNo,
        fileList: undefined,
        passFiles: formModel.fileList.map((item: any) => item.response),
      };
      await materialUseSpotCheckPassSubmit(params);
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
