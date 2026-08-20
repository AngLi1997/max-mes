<!-- 入库血浆核对 -- 列表 -->
<template>
  <DubRowTable ref="dubTableRef" :leftTableProps="leftTableProps" :rightTableProps="rightTableProps">
    <template #leftHeaderToolbar>
      <div v-hasAuth="170040004000001" class="scan-box">
        <Input
          v-model:value="scanNo"
          style="margin-right: 8px"
          :placeholder="t('扫描条码')"
          @press-enter="scanFn"></Input>
        <Button type="primary" style="margin-right: 8px">
          {{ t('开始扫描') }}
        </Button>
      </div>
    </template>
    <template #rightHeaderToolbar="{ instance }">
      <Button :disabled="instance?.paginationRef?.total === 0" type="primary" style="margin-right: 8px" @click="check">
        {{ t('完成核对') }}
      </Button>
    </template>
  </DubRowTable>
</template>

<script setup lang="ts">
  import DubRowTable from '@/components/DubRowTable/index.vue';
  import { t } from '@bmos/i18n';
  import { plasmaCheckStorageScan, plasmaCheckStorageSubmit, plasmaCheckStorageClear } from '@/services';
  import { useDubTable } from './hooks/useDubTable';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, message } from 'ant-design-vue';

  defineOptions({
    name: 'PlasmaCheckStorage',
  });

  const { dubTableRef, leftTableProps, rightTableProps, fetchDubData, syncBatchNo, containerNo } = useDubTable();

  const scanNo = ref('');

  // 扫描
  const scanFn = async () => {
    if (!syncBatchNo.value || !scanNo.value) {
      return;
    }
    try {
      await plasmaCheckStorageScan({
        syncBatchNo: syncBatchNo.value,
        plasmaOrgNo: scanNo.value,
        containerNo: containerNo.value,
      });
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
          await plasmaCheckStorageSubmit({ syncBatchNo: syncBatchNo.value, containerNo: containerNo.value });
          message.success(t('操作成功'));
          plasmaCheckStorageClear();
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
    await plasmaCheckStorageClear();
  });
</script>

<style lang="less" scoped>
  .scan-box {
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }
</style>
