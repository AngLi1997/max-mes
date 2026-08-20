<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="modalType === OperationStatusMap.ADD ? t('新增供应商信息') : t('编辑供应商信息')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"
    @cancelModal="closeModal"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { addMaterialSupplier, updateMaterialSupplier } from '@/services';
  import { useForm } from './hooks';
  import { BMModalForm } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { OperationStatusMap } from '@/types';
  import { useDict } from '@/stores';

  const { setDict } = useDict();

  const modalType = ref<OperationStatusMap>(OperationStatusMap.ADD);

  const emit = defineEmits(['submitSuccess']);

  const open = ref(false);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const openModal = async (type: OperationStatusMap, row?: any) => {
    modalType.value = type;
    open.value = true;
    if (type === OperationStatusMap.EDIT) {
      await nextTick();
      setFormModels({ ...row, requireAudit: row.requireAudit?.value });
    }
  };

  const submit = async (formModel: any) => {
    try {
      if (modalType.value === OperationStatusMap.EDIT) {
        await updateMaterialSupplier({
          ...formModel,
          supplierCnShortName: formModel.cnShortName,
        });
      } else {
        await addMaterialSupplier({
          ...formModel,
          supplierName: formModel.supplierName,
        });
      }
      message.success(t('操作成功'));
      setDict('供应商');
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
