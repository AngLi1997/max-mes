<!-- 接收弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="dialogType == 'receive' ? t('确认接收') : t('修改信息')"
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
  import { getSampleDataSyncEcho, sampleDataSyncUpdateInfo, sampleDataSyncReceive } from '@/services';
  import { BMModalForm, BMTable } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef } = useForm();
  const { tableRef, columns } = useTable();

  const dialogType = ref('receive');

  const dataList = ref<any>([]);

  const openModal = async (rows: any, type: 'receive' | 'edit') => {
    try {
      if (type === 'receive') {
        dataList.value = [...rows];
      } else {
        const res = await getSampleDataSyncEcho({
          syncBatchNo: rows?.[0].syncBatchNo,
        });
        dataList.value = [{ ...res.data, warehouseId: res.data.warehouse?.value }];
      }
      dialogType.value = type;
      open.value = true;
      await nextTick();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  const receiveList = computed(() => {
    return (
      tableRef.value?.tableData?.map((item: any) => {
        return {
          syncBatchNo: item.syncBatchNo,
          syncHistoryId: item.id,
          warehouseId: item.warehouseId,
        };
      }) || []
    );
  });

  const cancel = () => {
    open.value = false;
    dataList.value = [];
    // tableRef.value?.fetchData();
  };

  // 提交
  const submit = async () => {
    try {
      if (
        getWarehouseConfigByCode.value &&
        receiveList.value.length != receiveList.value.filter((item: any) => item.warehouseId).length
      ) {
        message.error(t('请选择仓库'));
        return;
      }
      const params = receiveList.value;
      if (dialogType.value === 'receive') {
        await sampleDataSyncReceive(params);
      } else {
        await sampleDataSyncUpdateInfo(params[0]);
      }
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
