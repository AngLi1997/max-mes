<!-- 出库血浆核对 -->
<template>
  <DubRowTable ref="dubTableRef" :leftTableProps="leftTableProps" :rightTableProps="rightTableProps">
    <template #righttableHeaderTitle>
      <div class="scan-box">
        <div style="margin-right: 16px">{{ `${t('出库批号')}: ${leftSelectData.batchNo ?? '--'}` }}</div>
        <span>
          {{ t('总数量') + ':' }}
          <span style="color: red; margin: 0 2px">{{ leftSelectData.num ?? 0 }}</span>
          {{ t('份') }}
        </span>
        <span style="margin-left: 8px">
          {{ t('待核对') + ':' }}
          <span style="color: red; margin: 0 2px">{{ waitCnt }}</span>
          {{ t('份') }}
        </span>
      </div>
    </template>
    <template #rightHeaderToolbar>
      <div class="scan-box">
        <!-- <Button type="primary" style="margin-right: 8px" @click="() => scanInputRef.focus()">
          {{ t('开始扫描') }}
        </Button> -->
        <Input
          ref="scanInputRef"
          style="margin-right: 8px; width: 150px"
          v-model:value="scanNo"
          :placeholder="t('扫描条码')"
          @pressEnter="scanFn"></Input>
        <Button
          v-hasAuth="170100010000002"
          type="primary"
          :disabled="!leftSelectData.batchNo || waitCnt > 0"
          @click="delivery">
          {{ t('血浆出库') }}
        </Button>
      </div>
    </template>
    <template #leftexpandColumnTitle>{{}}</template>
    <template #leftexpandedRowRender="{ record }">
      <BMPageComponent
        :ref="el => setExpandRef(record.batchNo, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: { batchNo: record.batchNo },
          },
        ]"
        :scrolls="[{ x: 800, y: 300 }]"
        :isExtraParamsChangeQuerys="[false]"
        :paginations="[paginationBig]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :bordereds="[false]"
        :requests="[getOutboundPlasmaCheckList as DataRequestFn]"
        :columns="[expandedTableMap[record.batchNo].columnsFirst]" />
    </template>
  </DubRowTable>
  <CheckUser ref="checkUserRef" @signSuccess="checkSuccess" />
  <Sign ref="signRef" :signatureAction="911" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import DubRowTable from '@/components/DubRowTable/index.vue';
  import { t } from '@bmos/i18n';
  import { Sign } from '@/components/Sign';
  import { useDubTable } from './hooks/useDubTable';
  import CheckUser from '@/components/CheckUser/index.vue';
  import { getOutboundPlasmaCheckList, outboundCheckScan, outboundCheckDelivery } from '@/services';
  import { BMPageComponent, DataRequestFn } from '@bmos/components';
  import { message, Modal } from 'ant-design-vue';
  import { paginationBig } from '@/utils/paginationConfig';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  defineOptions({
    name: 'PlasmaExitCheck',
  });

  const {
    leftSelectData,
    waitCnt,
    changeCheckState,
    dubTableRef,
    leftTableProps,
    rightTableProps,
    expandedTableMap,
    fetchDubData,
  } = useDubTable();

  // 扫描相关
  const scanNo = ref('');
  const scanInputRef = ref();

  // 身份核对相关
  const checkUserRef = ref<InstanceType<typeof CheckUser>>();

  // 身份核对成功
  const checkSuccess = async (values: any) => {
    changeCheckState(true);
    // checkState.value = true;
    await fetchDubData();
  };

  // 设置二级列表的ref
  const setExpandRef = (key: any, ref: any) => {
    expandedTableMap[key].setRef(ref);
  };

  // 扫描条码
  const scanFn = async () => {
    try {
      await outboundCheckScan({ batchNo: leftSelectData.value.batchNo, no: scanNo.value });
      scanNo.value = '';
      fetchDubData('right');
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  // 血浆出库
  const delivery = () => {
    Modal.confirm({
      title: t('是否进行血浆出库?'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          await signRef.value.openSign([
            {
              batchNo: leftSelectData.value.batchNo,
            },
          ]);
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };

  // 签名
  const signRef = ref();

  // 签名成功
  const signSuccess = async (signUrl: string) => {
    try {
      await outboundCheckDelivery({
        batchNo: leftSelectData.value.batchNo,
        signature: signUrl,
      });
      message.success(t('操作成功'));
      fetchDubData();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  onActivated(() => {
    if (checkUserRef.value) {
      checkUserRef.value.open = true;
    }
  });
  onMounted(() => {
    if (checkUserRef.value) {
      checkUserRef.value.open = true;
    }
  });
</script>

<style lang="less" scoped>
  .scan-box {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    flex-shrink: 0;
  }
</style>
