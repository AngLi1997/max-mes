<!-- 入库标本核对 -- 列表 -->
<template>
  <DubRowTable ref="dubTableRef" :leftTableProps="leftTableProps" :rightTableProps="rightTableProps">
    <template #leftHeaderToolbar>
      <div class="scan-box">
        <Input
          style="margin-right: 8px"
          v-model:value="scanNo"
          :placeholder="t('扫描条码')"
          @press-enter="scanFn"></Input>
        <Button type="primary" style="margin-right: 8px">
          {{ t('开始扫描') }}
        </Button>
      </div>
    </template>
    <template #rightHeaderToolbar="{ instance }">
      <Button
        v-hasAuth="170020005000001"
        :disabled="instance?.paginationRef?.total === 0"
        type="primary"
        style="margin-right: 8px"
        @click="check">
        {{ t('完成核对') }}
      </Button>
    </template>
  </DubRowTable>
</template>

<script setup lang="ts">
  import DubRowTable from '@/components/DubRowTable/index.vue';
  import { t } from '@bmos/i18n';
  import {
    getSampleInStorageVerifyClearCache,
    sampleInStorageVerifyScan,
    sampleInStorageVerifySubmit,
  } from '@/services';
  import { useDubTable } from './hooks/useDubTable';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, message } from 'ant-design-vue';

  defineOptions({
    name: 'specimen-verification',
    inheritAttrs: false,
  });

  const { dubTableRef, leftTableProps, rightTableProps, fetchDubData, syncBatchNo, boxId } = useDubTable();

  const scanNo = ref('');

  // 扫描
  const scanFn = async () => {
    if (!syncBatchNo.value || !scanNo.value) {
      return;
    }
    try {
      await sampleInStorageVerifyScan({ syncBatchNo: syncBatchNo.value, no: scanNo.value, boxId: boxId.value });
      await fetchDubData();
      scanNo.value = '';
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 完成核对
  const check = () => {
    Modal.confirm({
      title: t('请确认是否完成核对'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          await sampleInStorageVerifySubmit({ syncBatchNo: syncBatchNo.value, boxId: boxId.value });
          message.success(t('操作成功'));

          await getSampleInStorageVerifyClearCache();
          fetchDubData();
        } catch (error: any) {
          console.log(error);
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };

  onBeforeUnmount(async () => {
    await getSampleInStorageVerifyClearCache();
  });
</script>

<style lang="less" scoped>
  .scan-box {
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }
</style>
