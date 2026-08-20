<!-- 成功/失败数量弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="dialogType == 'success' ? t('成功同步数据') : t('失败同步数据')"
    wrapClassName="modalSizeExtraLarge"
    :cancel-button-text="t('关闭')"
    :showOkButton="false">
    <template #formBefore>
      <div style="height: 50vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-request="loadData"
          :columns="dialogType == 'success' ? columns : failColumns"
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
  import { useTable } from './hooks/useTable';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';
  import { sampleDataSyncDetailQuery } from '@/services';

  const open = ref(false);

  const modalFormRef = ref();
  const { tableRef, columns, failColumns } = useTable();

  const data = ref<any>({});

  const dialogType = ref('success');

  const openModal = async (row: any, type: 'success' | 'fail') => {
    data.value = row;
    dialogType.value = type;
    open.value = true;
  };

  const loadData: DataRequestFn = async (params: any): Promise<any> => {
    // 默认查最近30天
    const datas = {
      ...params,
      syncHistoryId: data.value?.id,
      successFlag: dialogType.value == 'success' ? 1 : 0,
    };
    // return getLoginLogList(datas);
    return await sampleDataSyncDetailQuery(datas);
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
