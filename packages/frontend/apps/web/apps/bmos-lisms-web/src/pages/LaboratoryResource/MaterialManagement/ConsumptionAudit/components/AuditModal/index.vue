<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('消耗审核')"
    wrapClassName="modalSizeLarge"
    :formProps="formProps"
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
          :scroll="{ x: 800, y: 400 }"
          :showRefresh="false"></BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { laboratoryUseDepleteAudit } from '@/services';
  import { useTable, useForm } from './hooks';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { message } from 'ant-design-vue';

  const emit = defineEmits(['submitSuccess']);

  const open = ref(false);

  const { modalFormRef, formProps } = useForm();
  const { tableRef, columns } = useTable();
  const tableData = ref<any[]>([]);

  const openModal = async (row: any) => {
    tableData.value = [...row];
    open.value = true;
  };

  const submit = async (formModel: any) => {
    try {
      const params = {
        auditIds: tableData.value.map((item: any) => item.identify),
        materialNo: tableData.value.map((item: any) => item.materialNo).join(','),
        ...formModel,
      };
      await laboratoryUseDepleteAudit(params);
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
