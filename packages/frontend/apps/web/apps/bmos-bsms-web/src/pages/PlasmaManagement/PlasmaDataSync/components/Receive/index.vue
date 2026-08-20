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
          :data-request="loadData"
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
  import { plasmaDataSyncReceive, plasmaDataSyncUpdate } from '@/services';
  import { t } from '@bmos/i18n';
  import { useForm, useTable } from './hooks';
  import { message } from 'ant-design-vue';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef } = useForm();
  const { tableRef, columns } = useTable();

  const dataList = ref<any>([]);
  const dialogType = ref('receive');

  const openModal = async (rows: any, type: 'receive' | 'edit') => {
    dataList.value = rows;
    dialogType.value = type;
    open.value = true;
    await nextTick();
  };

  const receiveList = computed(() => {
    return tableRef.value?.tableData?.map((item: any) => {
      return {
        syncHistoryId: item.id,
        warehouseId: item.warehouseId,
      };
    });
  });

  const loadData: DataRequestFn = async (): Promise<any> => {
    return new Promise(resolve => {
      setTimeout(() => {
        resolve({
          data: [...dataList.value],
          total: dataList.value?.length || 0,
        });
      }, 100);
    });
  };

  const cancel = () => {
    open.value = false;
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
      if (dialogType.value === 'receive') {
        await plasmaDataSyncReceive(receiveList.value);
      } else {
        await plasmaDataSyncUpdate(receiveList.value[0]);
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
