<!-- 验收审核弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('验收审核')"
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
  import { specimenAcceptanceAudit } from '@/services';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useForm();
  const { tableRef, columns } = useTable();

  const dataList = ref([]);

  const openModal = async (rows: any, type: 'audit' | 'return') => {
    dataList.value = rows;
    open.value = true;
    await nextTick();
    setFormModels({
      auditResult: type == 'audit' ? 1 : 2,
    });
  };

  // const receiveList = computed(() => {
  //   return tableRef.value?.tableData.value.map((item: any) => {
  //     return {
  //       syncHistoryId: item.id,
  //       warehouseId: item.warehouseId,
  //     };
  //   });
  // });

  const loadData: DataRequestFn = async (params: any, onChangeParams: any): Promise<any> => {
    return {
      data: dataList.value,
    };
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const params = {
        auditIdList: dataList.value?.map((item: any) => item.auditId),
        auditResult: formModal.auditResult,
        remark: formModal.remark,
      };

      return await specimenAcceptanceAudit(params);
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
