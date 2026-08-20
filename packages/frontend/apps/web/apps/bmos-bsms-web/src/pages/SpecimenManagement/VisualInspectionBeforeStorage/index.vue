<!-- 入库前外观检验 -- 标本 -->
<template>
  <DubRowTable ref="dubTableRef" :leftTableProps="leftTableProps" :rightTableProps="rightTableProps">
    <template #lefttableHeaderTitle>
      <div class="toolbar-box">
        <Input
          ref="scanInputRef"
          style="margin-right: 8px"
          v-model:value="scanNo"
          @press-enter="scanFn"
          :placeholder="t('扫描条码')"></Input>
        <Button type="primary" @click="() => scanInputRef?.focus()">
          {{ t('开始扫描') }}
        </Button>
      </div>
    </template>
    <template #leftHeaderToolbar>
      <span>{{ t('总数量') + ':' }}</span>
      <span style="color: red">{{ totalObj.totalNum }}</span>
      <span>{{ t('待检测') + ':' }}</span>
      <span style="color: red">{{ totalObj.toBeDetectedNum }}</span>
    </template>
    <template #rightHeaderToolbar="{ instance }">
      <div class="toolbar-box">
        <Button
          v-hasAuth="170020003000001"
          type="primary"
          :disabled="rowSelections[0]?.selectedRowKeys?.length === 0"
          style="margin-right: 8px"
          @click="openInspection">
          {{ t('外观检验') }}
        </Button>
        <Button
          v-hasAuth="170020003000002"
          :disabled="instance?.getTableData()?.length === 0"
          type="primary"
          @click="submit">
          {{ t('提交') }}
        </Button>
      </div>
    </template>
  </DubRowTable>
  <Inspection ref="inspectionRef" :syncBatchNo="syncBatchNo" :boxId="boxId" @submit-success="fetchDubData('right')" />
</template>

<script setup lang="ts">
  import DubRowTable from '@/components/DubRowTable/index.vue';
  import { t } from '@bmos/i18n';
  import { useDubTable } from './hooks/useDubTable';
  import { Inspection } from './components';
  import {
    getSpecimenAppearanceBeforeScan,
    specimenAppearanceBeforeClearCache,
    specimenAppearanceBeforeSubmit,
  } from '@/services';
  import { Modal, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  defineOptions({
    name: 'visualInspection-before-storage',
    inheritAttrs: false,
  });

  const {
    dubTableRef,
    leftTableProps,
    rightTableProps,
    fetchDubData,
    rowSelections,
    syncBatchNo,
    boxId,
    operationSelectedRows,
    totalObj,
  } = useDubTable();

  const scanInputRef = ref();
  const scanNo = ref('');

  // 外观检验
  const inspectionRef = ref<any>();

  const openInspection = () => {
    inspectionRef.value.openModal(operationSelectedRows.value);
  };

  // 扫描
  const scanFn = async () => {
    if (!syncBatchNo.value || !scanNo.value) {
      return;
    }
    try {
      await getSpecimenAppearanceBeforeScan(syncBatchNo.value, scanNo.value, boxId.value);
      await fetchDubData('left');
      scanNo.value = '';
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const submit = async () => {
    if (!syncBatchNo.value) return;
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否提交数据'),
      async onOk() {
        try {
          await specimenAppearanceBeforeSubmit(syncBatchNo.value, boxId.value);
          await specimenAppearanceBeforeClearCache();
          message.success(t('操作成功'));
          await fetchDubData();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };

  onBeforeUnmount(async () => {
    await specimenAppearanceBeforeClearCache();
  });
</script>

<style lang="less" scoped>
  .toolbar-box {
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }
</style>
