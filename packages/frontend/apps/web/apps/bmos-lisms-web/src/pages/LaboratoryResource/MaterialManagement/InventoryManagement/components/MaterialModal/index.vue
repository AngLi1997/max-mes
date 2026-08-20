<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="modalType === RecordSourceAuditTypeEnum.OUT_CONSUME ? t('物料消耗') : t('物料报废')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"
    @cancelModal="closeModal"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { materialUseApply } from '@/services';
  import { useForm } from './hooks';
  import { BMModalForm } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { useDict } from '@/stores/dictStore';
  import { RecordSourceAuditTypeEnum } from '@/types';

  const emit = defineEmits(['submitSuccess']);

  const modalType = ref<RecordSourceAuditTypeEnum>(RecordSourceAuditTypeEnum.OUT_CONSUME);

  const open = ref(false);

  const { modalFormRef, formProps, setFormModels, updateSchema } = useForm();

  const { getDict } = useDict();

  const openModal = async (type: RecordSourceAuditTypeEnum, row: any) => {
    modalType.value = type;
    const reasonList = await getDict(type === RecordSourceAuditTypeEnum.OUT_CONSUME ? '消耗原因' : '报废原因');
    open.value = true;
    await nextTick();
    updateSchema([
      {
        field: 'useCount',
        label: type === RecordSourceAuditTypeEnum.OUT_CONSUME ? t('消耗数量') : t('报废数量'),
        componentProps: {
          max: row.useCount,
          placeholder: t('请输入') + (type === RecordSourceAuditTypeEnum.OUT_CONSUME ? t('消耗数量') : t('报废数量')),
        },
      },
      {
        field: 'reasonId',
        label: type === RecordSourceAuditTypeEnum.OUT_CONSUME ? t('消耗原因') : t('报废原因'),
        componentProps: {
          options: reasonList,
          placeholder: t('请选择') + (type === RecordSourceAuditTypeEnum.OUT_CONSUME ? t('消耗原因') : t('报废原因')),
        },
      },
    ]);
    setFormModels({
      storeNo: row.storeNo,
      materialNo: row.materialNo,
      materialName: row.materialName,
      supplierName: row.supplierName,
      specificationName: row.specificationName,
      availableCount: row.useCount,
    });
  };

  const submit = async (formModel: any) => {
    try {
      const params = {
        ...formModel,
        materialNo: formModel.materialNo,
        recordSource: modalType.value,
      };
      await materialUseApply(params);
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
