<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="modalType === OperationStatusMap.ADD ? t('新增物料信息') : t('编辑物料信息')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"
    @cancelModal="closeModal"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { addMaterialInfo, updateMaterialInfo } from '@/services';
  import { useForm } from './hooks';
  import { BMModalForm } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { OperationStatusMap } from '@/types';

  const modalType = ref<OperationStatusMap>(OperationStatusMap.ADD);

  const emit = defineEmits(['submitSuccess']);

  const open = ref(false);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const { getConfigEnumsValueByParamId } = useConfig();

  const openModal = async (type: OperationStatusMap, row?: any) => {
    modalType.value = type;
    open.value = true;
    let initData = {
      minInventory: Number(getConfigEnumsValueByParamId('默认最低库存量')),
    };
    if (type === OperationStatusMap.EDIT) {
      initData = {
        ...row,
        materialType: row.materialType?.value,
        keyMaterialCategory: row.keyMaterialCategory?.value,
      };
    }
    await nextTick();
    setFormModels(initData);
  };

  const submit = async (formModel: any) => {
    try {
      const params = {
        ...formModel,
        materialNo: formModel.materialNo,
      };
      if (modalType.value === OperationStatusMap.EDIT) {
        await updateMaterialInfo(params);
      } else {
        await addMaterialInfo(params);
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
