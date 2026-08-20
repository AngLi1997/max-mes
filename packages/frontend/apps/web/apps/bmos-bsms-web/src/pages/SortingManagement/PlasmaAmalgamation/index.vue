<!-- 合并血浆 -->
<template>
  <DubRowTable
    ref="dubTableRef"
    :leftTitle="t('主合并箱/托盘')"
    :rightTitle="t('副合并箱/托盘')"
    :leftTableProps="leftTableProps"
    :rightTableProps="rightTableProps">
    <template #leftHeaderToolbar>
      <div class="scan-box">
        <Button v-hasAuth="170080006000001" style="margin-right: 8px" @click="printBox">
          {{ t('打印箱号') }}
        </Button>
        <Button type="primary" style="margin-right: 8px" :disabled="scanList?.length === 0" @click="submitMerge">
          {{ t('提交合并') }}
        </Button>
      </div>
    </template>
    <template #rightHeaderToolbar>
      <div v-hasAuth="170080006000002" class="scan-box">
        <Button type="primary" style="margin-right: 8px" @click="scanInputRef?.focus()">
          {{ t('开始扫描') }}
        </Button>
        <Input
          ref="scanInputRef"
          style="margin-right: 8px"
          :disabled="rightTableData?.length === 0 || leftTableData?.length === 0"
          v-model:value="scanNo"
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
  import { plasmaAmalgamationMerge, plasmaAmalgamationScan } from '@/services';

  defineOptions({
    name: 'PlasmaAmalgamation',
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
    isFull,
  } = useDubTable();

  const printBoxModalRef = ref<any>();

  const printBox = () => {
    printBoxModalRef.value?.openModal(1);
  };

  const scanNo = ref('');

  const scanInputRef = ref();

  // 扫描条码
  const scanFn = async () => {
    try {
      if (isFull()) {
        message.error(t('主合并箱/托盘已满'));
        scanNo.value = '';
        return;
      }
      if (!rightTableData.value?.find((item: any) => item.plasmaOrgNo === scanNo.value)) {
        message.error(t('该条码不属于此副合并箱/托盘'));
        scanNo.value = '';
        return;
      }
      const mainContainerNo = dubTableRef.value?.leftRef?.getQueryFormRef()?.formModel?.containerNo;

      await plasmaAmalgamationScan({ mainContainerNo, plasmaOrgNo: scanNo.value });
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
          await plasmaAmalgamationMerge({
            mainContainerNo: dubTableRef.value?.leftRef?.getQueryFormRef()?.formModel?.containerNo,
            plasmaOrgNoList: scanList.value,
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
