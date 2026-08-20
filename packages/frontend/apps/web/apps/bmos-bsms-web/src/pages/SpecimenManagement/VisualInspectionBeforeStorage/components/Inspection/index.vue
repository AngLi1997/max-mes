<!-- 外观检验 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('外观检验')"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit">
    <template #formBefore>
      <div style="height: 45vh">
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
  import { specimenAppearanceBeforeUpdate } from '@/services';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';

  const props = defineProps({
    syncBatchNo: {
      type: String,
      default: '',
    },
    boxId: {
      type: String,
      default: '',
    },
  });

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps } = useForm();
  const { tableRef, columns } = useTable();

  const dataList = ref([]);

  const openModal = async (rows: any) => {
    dataList.value = JSON.parse(JSON.stringify(rows));
    open.value = true;
    await nextTick();
  };

  const loadData: DataRequestFn = async (params: any, onChangeParams: any): Promise<any> => {
    return {
      data: [...dataList.value],
    };
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const approvalParams = {
        orgSampleNoList: dataList.value?.map((item: any) => item.orgSampleNo),
        syncBatchNo: props.syncBatchNo,
        boxId: props.boxId,
        appearance: formModal.appearance,
        remark: formModal.remark,
      };

      return await specimenAppearanceBeforeUpdate(approvalParams);
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
