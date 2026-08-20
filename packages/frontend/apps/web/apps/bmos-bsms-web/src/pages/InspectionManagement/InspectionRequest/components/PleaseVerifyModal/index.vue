<!-- 标本请验弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('标本请验')"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit">
    <template #formBefore>
      <div style="height: 50vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-request="loadData"
          :columns="columns"
          auto-height
          row-key="id"
          :show-tool-bar="false"
          :scroll="{ x: 844, y: 400 }"
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
  import { sampleExaminationInfoInspection } from '@/services';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps } = useForm();
  const { tableRef, columns } = useTable();

  const dataList = ref([]);

  const openModal = (rows: any) => {
    dataList.value = rows;
    open.value = true;
  };

  // const receiveList = computed(() => {
  //   return tableRef.value?.tableData.value.map((item: any) => {
  //     return {
  //       syncHistoryId: item.id,
  //       warehouseId: item.warehouseId,
  //     };
  //   });
  // });

  const loadData: DataRequestFn = async (): Promise<any> => {
    return new Promise(resolve => {
      resolve({
        data: [...dataList.value],
        total: dataList.value?.length || 0,
      });
    });
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const params = {
        inspectionBatchNoList: dataList.value?.map((item: any) => item.inspectionBatchNo),
        remark: formModal.remark,
      };

      return await sampleExaminationInfoInspection(params);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      await request(formModal);
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
