<!-- 标本出库弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('标本出库')"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit">
    <template #formBefore>
      <div style="height: 45vh">
        <BMTable
          ref="tableRef"
          :data-source="[info]"
          :columns="columns"
          :search="false"
          auto-height
          row-key="id"
          :show-tool-bar="true"
          :scroll="{ x: 700, y: 400 }"
          :showRefresh="false"
          :pagination="false"></BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import { qualifiedSampleOutWarehouseOut } from '@/services';
  import { message } from 'ant-design-vue';
  import { BMModalForm, BMTable, ModalFormInstance, FormProps } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('备注'),
        field: 'remark',
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        component: 'InputTextArea',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
    ],
  });

  const { tableRef, columns } = useTable();

  const info = ref<any>({});

  const openModal = async (row: any) => {
    info.value = row;
    open.value = true;
    await nextTick();
  };

  const request = async (formModel: any) => {
    try {
      const params = {
        batchNo: info.value.batchNo,
        qualified: 0,
        palletNo: info.value.palletNo,
        currentInventoryStatus: info.value.currentInventoryStatus,
        warehouseId: info.value?.warehouse?.value,
        ...formModel,
      };
      return await qualifiedSampleOutWarehouseOut(params);
    } catch (error: any) {
      return Promise.reject(error);
    }
  };

  const submit = async (formModal: any) => {
    try {
      await request(formModal);
      message.success(t('操作成功'));
      emits('submitSuccess', info.value.batchNo);
      cancel();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const cancel = () => {
    info.value = {};
    open.value = false;
  };

  defineExpose({ openModal, cancel });
</script>

<style scoped></style>
