<!-- 标本入库验收 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('标本入库验收')"
    wrapClassName="modalSizeLarge"
    :formProps="formProps"
    :submit="submit">
    <template #formBefore>
      <div style="height: 45vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-request="loadData"
          :columns="columns"
          :showToolBar="false"
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
  import { getWaitInStorageEcho, waitInStorageAcceptance } from '@/services';
  import { useForm, useTable } from './hooks';
  import { message } from 'ant-design-vue';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps } = useForm();
  const { tableRef, columns } = useTable();

  const dataList = ref<any>([]);

  const openModal = async (rows: any) => {
    dataList.value = rows.map((item: any) => item.syncBatchNo);
    open.value = true;
    await nextTick();
  };

  const loadData: DataRequestFn = async (): Promise<any> => {
    const res = await getWaitInStorageEcho(dataList.value);
    return {
      data: res.data,
    };
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const params = {
        syncBatchNoList: dataList.value,
        ...formModal,
      };
      return await waitInStorageAcceptance(params);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async () => {
    try {
      await modalFormRef.value?.submit(request);
      message.success(t('操作成功'));
      emits('submitSuccess');
      cancel();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
