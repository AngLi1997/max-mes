<!-- 接收 弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('接收')"
    wrapClassName="modalSizeLarge"
    :submit="submit">
    <template #formBefore>
      <div style="height: 50vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-source="dataList"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :scroll="{ x: 800, y: 400 }"
          :showRefresh="false"
          :pagination="paginationSmall"></BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { t } from '@bmos/i18n';
  import { useForm, useTable } from './hooks';
  import { message } from 'ant-design-vue';
  import { acceptSampleBatch } from '@/services';
  import { BMModalForm, BMTable } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef } = useForm();
  const { tableRef, columns } = useTable();

  const dataList = ref<any>([]);

  const openModal = async (rows: any) => {
    dataList.value = [...rows];
    open.value = true;
    await nextTick();
  };

  const cancel = () => {
    open.value = false;
    dataList.value = [];
    // tableRef.value?.fetchData();
  };

  // 提交
  const submit = async () => {
    try {
      const params = {
        inspectionBatchNoList: dataList.value.map((item: any) => item.inspectionBatchNo),
      };
      await acceptSampleBatch(params);
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
