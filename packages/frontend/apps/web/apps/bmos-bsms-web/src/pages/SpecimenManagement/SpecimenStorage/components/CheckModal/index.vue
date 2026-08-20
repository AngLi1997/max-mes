<!-- 入库标本核对 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('入库标本核对')"
    wrapClassName="modalSizeLarge"
    :cancelButtonText="t('关闭')"
    :show-ok-button="false">
    <template #formBefore>
      <div class="line">
        <span>{{ `${t('浆站入库批号')}: ${batchNo}` }}</span>
        <span>{{ `${t('总数量')}: ${cntObj.totalNum}` }}</span>
        <span>{{ `${t('待核对')}: ${cntObj.waitCheckNum}` }}</span>
        <span>{{ `${t('已核对')}: ${cntObj.checkNum}` }}</span>
        <div class="scan">
          <Input
            ref="scanInputRef"
            v-model:value="scanNo"
            style="margin-right: 8px; min-width: 150px"
            :placeholder="t('扫描条码')"
            @pressEnter="scanFn"></Input>
          <Button type="primary" @click="() => scanInputRef?.focus()">
            {{ t('开始扫描') }}
          </Button>
        </div>
      </div>
      <div style="height: 45vh">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-request="loadData"
          :columns="columns"
          row-key="id"
          :show-tool-bar="false"
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          :pagination="paginationSmall"></BMTable>
      </div>
    </template>
  </BMModalForm>
  <BatchStorage ref="batchStorageRef" isCheck @submitSuccess="submitSuccess" />
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { getInStorageCheckDetailList, getInStorageScan } from '@/services';
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import { message } from 'ant-design-vue';
  import { BatchStorage } from '../index';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';

  const scanNo = ref('');

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const submitSuccess = () => {
    emits('submitSuccess');
  };

  const modalFormRef = ref();
  const { tableRef, columns } = useTable();

  const scanInputRef = ref();

  //  批号
  const batchNo = ref('');

  // 数量信息
  const cntObj = ref<any>({
    totalNum: 0,
    waitCheckNum: 0,
    checkNum: 0,
  });

  const openModal = async (row: any) => {
    batchNo.value = row.syncBatchNo;
    open.value = true;
    await nextTick();
  };

  const loadData: DataRequestFn = async (params): Promise<any> => {
    const datas = {
      ...params,
      syncBatchNo: batchNo.value,
      verifyStatus: 0,
    };

    const { data } = await getInStorageCheckDetailList(datas);
    cntObj.value = {
      totalNum: data.totalNum,
      waitCheckNum: data.waitCheckNum,
      checkNum: data.checkNum,
    };
    return {
      data: data.detailList,
    };
  };

  const cancel = () => {
    open.value = false;
    cntObj.value = {
      totalNum: 0,
      waitCheckNum: 0,
      checkNum: 0,
    };
    batchNo.value = '';
    scanNo.value = '';
  };

  const { hasPermission } = usePermissionStore();

  // 扫描条码
  const scanFn = async () => {
    try {
      const res = await getInStorageScan({ batchNo: batchNo.value, no: scanNo.value });
      scanNo.value = '';
      if (hasPermission('170020004000001') && res?.data) {
        openBatchStorage({ syncBatchNo: batchNo.value });
        cancel();
        return;
      }
      await tableRef.value?.fetchData();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  // =====入库=====
  const batchStorageRef = ref();
  const openBatchStorage = (row: any) => {
    batchStorageRef.value.openModal(row);
  };

  defineExpose({ openModal, openBatchStorage });
</script>

<style lang="less" scoped>
  .line {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    margin-bottom: 20px;
    span {
      margin: 0 10px;
    }
    .scan {
      margin-left: auto;
      display: flex;
      align-items: center;
      justify-content: flex-end;
    }
  }
</style>
