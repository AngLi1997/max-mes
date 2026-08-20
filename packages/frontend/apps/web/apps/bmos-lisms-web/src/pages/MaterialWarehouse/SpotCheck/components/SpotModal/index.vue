<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="modalType == ModelType.SUBMIT ? t('物料抽检') : t('撤销抽检')"
    wrapClassName="modalSizeLarge"
    :formProps="modalType == ModelType.SUBMIT ? formProps : undefined"
    :submit="submit"
    @cancelModal="closeModal">
    <template #formBefore>
      <div :style="{ height: tableData.length > 4 ? '40vh' : 'auto' }">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-source="tableData"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"></BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { materialUseSpotCheckSubmit, materialUseSpotCheckRevert } from '@/services';
  import { useTable, useForm } from './hooks';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { message } from 'ant-design-vue';

  const emit = defineEmits(['submitSuccess']);

  enum ModelType {
    SUBMIT = 'submit',
    CANCEL = 'cancel',
  }

  const modalType = ref<ModelType>(ModelType.SUBMIT);

  const open = ref(false);

  const { modalFormRef, formProps, updateSchema } = useForm();
  const { tableRef, columns } = useTable();
  const tableData = ref<any[]>([]);

  const openModal = async (type: ModelType, rows: any[]) => {
    modalType.value = type;
    tableData.value = [...rows];
    const minQuantity = rows.reduce((pre: number, cur: any) => Math.min(pre, cur.quantity), 10e8);
    open.value = true;
    await nextTick();
    updateSchema({
      field: 'quality',
      componentProps: {
        max: minQuantity,
        placeholder: `${t('当前抽检入库单号中，最小入库数量为')}${minQuantity}`,
      },
    });
  };

  const submit = async (formModel: any) => {
    try {
      const params = {
        identifiers: tableData.value.map((item: any) => item.useFormIdentify),
        materialNo: tableData.value.map((item: any) => item.materialNo).join(','),
      };
      if (modalType.value === ModelType.SUBMIT) {
        await materialUseSpotCheckSubmit({ ...params, ...formModel });
      } else {
        await materialUseSpotCheckRevert(params);
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
    tableData.value = [];
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
