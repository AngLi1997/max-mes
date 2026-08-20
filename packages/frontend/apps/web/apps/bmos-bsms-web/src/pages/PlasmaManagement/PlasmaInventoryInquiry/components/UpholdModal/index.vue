<!-- 血浆维护弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('血浆维护')"
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
  import { plasmaInventoryMaintain } from '@/services';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps } = useForm();
  const { tableRef, columns } = useTable();

  const dataList = ref([]);

  const openModal = async (rows: any) => {
    dataList.value = rows;
    open.value = true;
    await nextTick();
    // setFormModels({
    //   type: type,
    // });
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
        plasmaOrgNoList: dataList.value?.map((item: any) => item.plasmaOrgNo),
        maintainStatus: formModal.maintainStatus,
        maintainRemark: formModal.maintainRemark,
      };

      return await plasmaInventoryMaintain(approvalParams);
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
