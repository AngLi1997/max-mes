<!-- 扫码接收 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('扫码接收')"
    wrapClassName="modalSizeLarge"
    :cancelButtonText="t('关闭')"
    :show-ok-button="false"
    @cancel="cancel">
    <template #formBefore>
      <div class="line">
        <span>{{ `${t('标本批号')}: ${batchNo}` }}</span>
        <span>{{ `${t('总数量')}: ${cntObj.totalNum}` }}</span>
        <span>{{ `${t('待扫描')}: ${cntObj.toScanCount}` }}</span>
        <span>{{ `${t('已扫描')}: ${cntObj.scanCount}` }}</span>
        <div class="scan">
          <Input
            ref="scanInputRef"
            v-model:value="scanNo"
            style="margin-right: 8px"
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
</template>

<script setup lang="ts">
  import { paginationSmall } from '@/utils/paginationConfig';
  import { getSampleReceiveScanPage, sampleReceiveScan } from '@/services';
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import { message } from 'ant-design-vue';
  import { BMModalForm, BMTable, DataRequestFn } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();

  const scanNo = ref('');

  const open = ref(false);
  const emits = defineEmits(['submitSuccess']);

  const submitSuccess = (val: any) => {
    emits('submitSuccess', [val]);
  };

  const modalFormRef = ref();
  const { tableRef, columns } = useTable();

  const scanInputRef = ref();

  //  批号
  const batchNo = ref('');
  const record = ref<any>({});

  // 数量信息
  const cntObj = ref<any>({
    totalNum: 0,
    toScanCount: 0,
    scanCount: 0,
  });
  // 上一次待接收数量
  const lastNum = ref(0);

  const openModal = async (row: any) => {
    batchNo.value = row.batchNo;
    record.value = { ...row };
    open.value = true;
    await nextTick();
  };

  const loadData: DataRequestFn = async (params: any): Promise<any> => {
    const datas = {
      ...params,
      batchNo: batchNo.value,
      verifyStatus: 0,
    };

    const { data } = await getSampleReceiveScanPage(datas);
    if (hasPermission('210020001000001') && data.toScanCount === 0 && lastNum.value !== 0) {
      submitSuccess(record.value);
      cancel();
      return {};
    }
    cntObj.value = {
      totalNum: data.transferCount,
      toScanCount: data.toScanCount,
      scanCount: data.scanCount,
    };
    lastNum.value = data.toScanCount;
    return {
      data: data.pageResponse,
    };
  };

  const cancel = () => {
    open.value = false;
    cntObj.value = {
      totalNum: 0,
      toScanCount: 0,
      scanCount: 0,
    };
    lastNum.value = 0;
    batchNo.value = '';
    record.value = {};
    scanNo.value = '';
  };

  // 扫描条码
  const scanFn = async () => {
    try {
      await sampleReceiveScan({ batchNo: batchNo.value, orgSampleNo: scanNo.value });
      scanNo.value = '';
      // if (hasPermission('170040003000001') && res?.data) {
      //   openBatchStorage({ syncBatchNo: batchNo.value });
      //   cancel();
      //   return;
      // }
      await tableRef.value?.fetchData();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
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
