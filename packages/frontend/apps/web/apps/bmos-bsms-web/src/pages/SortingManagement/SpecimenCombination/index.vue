<!-- 合并标本 -->
<template>
  <DubRowTable
    ref="dubTableRef"
    :leftTitle="t('主合并箱')"
    :rightTitle="t('副合并箱')"
    :leftTableProps="leftTableProps"
    :rightTableProps="rightTableProps">
    <template #leftHeaderToolbar>
      <div class="scan-box">
        <Button style="margin-right: 8px" @click="printBox">
          {{ t('打印箱号') }}
        </Button>
        <Button type="primary" style="margin-right: 8px" :disabled="scanList?.length === 0" @click="submitMerge">
          {{ t('提交合并') }}
        </Button>
      </div>
    </template>
    <template #rightHeaderToolbar>
      <div class="scan-box">
        <Button type="primary" style="margin-right: 8px" @click="scanInputRef?.focus()">
          {{ t('开始扫描') }}
        </Button>
        <Input
          ref="scanInputRef"
          v-model:value="scanNo"
          style="margin-right: 8px"
          :disabled="rightTableData?.length === 0 || leftTableData?.length === 0"
          :placeholder="t('扫描条码')"
          @pressEnter="scanFn"></Input>
      </div>
    </template>
  </DubRowTable>
  <PrintBoxModal ref="printBoxModalRef" />
</template>

<script setup lang="ts">
  import DubRowTable from '@/components/DubRowTable/index.vue';
  import { t } from '@bmos/i18n';
  import { useDubTable } from './hooks/useDubTable';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, message } from 'ant-design-vue';
  import PrintBoxModal from '@/components/PrintBoxModal/index.vue';
  import { sampleAmalgamationMerge, sampleAmalgamationScan } from '@/services';

  defineOptions({
    name: 'SpecimenCombination',
    inheritAttrs: false,
  });

  const {
    dubTableRef,
    leftTableProps,
    rightTableProps,
    leftTableData,
    rightTableData,
    scanPlasma,
    fetchDubData,
    resetScan,
    scanList,
  } = useDubTable();

  const printBoxModalRef = ref<any>();

  const printBox = () => {
    printBoxModalRef.value?.openModal(2);
  };

  const scanNo = ref('');

  const scanInputRef = ref();

  // 扫描条码
  const scanFn = async () => {
    try {
      // 测试和后端说不判断箱子装满
      // if (isFull()) {
      //   message.error(t('主合并箱已满'));
      //   scanNo.value = '';
      //   return;
      // }
      if (!rightTableData.value?.find((item: any) => item.sampleOrgNo === scanNo.value)) {
        message.error(t('该条码不属于此副合并箱'));
        scanNo.value = '';
        return;
      }
      const mainContainerNo = dubTableRef.value?.leftRef?.getQueryFormRef()?.formModel?.containerNo;

      await sampleAmalgamationScan({ mainContainerNo, sampleOrgNo: scanNo.value });
      scanPlasma(scanNo.value);
      scanNo.value = '';
      await fetchDubData();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
      scanNo.value = '';
    }
  };

  const submitMerge = async () => {
    Modal.confirm({
      title: t('是否进行合并操作?'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          await sampleAmalgamationMerge({
            mainContainerNo: dubTableRef.value?.leftRef?.getQueryFormRef()?.formModel?.containerNo,
            sampleOrgNoList: scanList.value,
          });

          message.success(t('操作成功'));
          fetchDubData();
          resetScan();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };
</script>

<style lang="less" scoped>
  .scan-box {
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }
</style>
