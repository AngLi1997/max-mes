<!-- 出库标本核对 -->
<template>
  <DubRowTable ref="dubTableRef" :leftTableProps="leftTableProps" :rightTableProps="rightTableProps">
    <template #righttableHeaderTitle>
      <div class="scan-box">
        <span style="margin-right: 16px">{{ `${t('出库批号')}: ${leftSelectData?.outPlanBatchNo ?? ''}` }}</span>
        <span>
          {{ t('总数量') + ':' }}
          <span style="color: red; margin: 0 2px">{{ numObj.totalNum ?? 0 }}</span>
          {{ t('份') }}
        </span>
        <span style="margin-left: 8px">
          {{ t('待核对') + ':' }}
          <span style="color: red; margin: 0 2px">{{ numObj.waitVerifyNum ?? 0 }}</span>
          {{ t('份') }}
        </span>
      </div>
    </template>
    <template #rightHeaderToolbar>
      <div class="scan-box">
        <Input
          v-hasAuth="170020012000001"
          ref="scanInputRef"
          style="margin-right: 8px; width: 150px"
          v-model:value="scanNo"
          :placeholder="t('扫描条码')"
          @pressEnter="scanFn"></Input>
        <Button
          v-hasAuth="170020012000003"
          type="primary"
          :disabled="!leftSelectData.outPlanBatchNo || numObj.waitVerifyNum > 0"
          @click="delivery">
          {{ t('标本出库') }}
        </Button>
      </div>
    </template>
    <template #leftexpandColumnTitle>{{}}</template>
    <template #leftexpandedRowRender="{ record }">
      <BMPageComponent
        :ref="el => setExpandRef(record.outPlanBatchNo, el)"
        :rowKeys="['id']"
        :search="[false]"
        :hideRightTree="true"
        :tableFields="[
          {
            default: { outPlanBatchNo: record.outPlanBatchNo },
          },
        ]"
        :isExtraParamsChangeQuerys="[false]"
        :showHeader="[false]"
        :showToolBars="[false]"
        :bordereds="[false]"
        :requests="[loadData as DataRequestFn]"
        :paginations="[paginationBig]"
        :columns="[expandedTableMap[record.outPlanBatchNo].columnsFirst]" />
    </template>
  </DubRowTable>
  <CheckUser ref="checkUserRef" :checkNum="checkNum" @signSuccess="signSuccess" />
</template>

<script setup lang="ts">
  import DubRowTable from '@/components/DubRowTable/index.vue';
  import { paginationBig } from '@/utils/paginationConfig';
  import { t } from '@bmos/i18n';
  import { getSampleOutWarehouseDetail, sampleOutVerifyOut, sampleOutVerifyScan } from '@/services';
  import { useDubTable } from './hooks/useDubTable';
  import CheckUser from '@/components/CheckUser/index.vue';
  import { BMPageComponent, DataRequestFn } from '@bmos/components';
  import { Modal, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  defineOptions({
    name: 'outgoing-specimens-check',
    inheritAttrs: false,
  });

  const {
    leftSelectData,
    cangeCheckState,
    dubTableRef,
    leftTableProps,
    rightTableProps,
    expandedTableMap,
    fetchDubData,
    numObj,
  } = useDubTable();

  const loadData: DataRequestFn = async (params: any, onChangeParams: any): Promise<any> => {
    const datas = {
      ...params,
    };
    const { data } = await getSampleOutWarehouseDetail(datas);
    return {
      data: data.voList,
    };
  };

  // 扫描相关
  const scanNo = ref('');
  const scanInputRef = ref();

  // 扫描条码
  const scanFn = async () => {
    try {
      await sampleOutVerifyScan({ outPlanBatchNo: leftSelectData.value.outPlanBatchNo, orgSampleNo: scanNo.value });
      scanNo.value = '';
      fetchDubData('right');
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  // 身份核对相关
  const checkUserRef = ref<InstanceType<typeof CheckUser>>();
  const checkNum = ref(0);

  // 签名成功
  const signSuccess = async (values: any) => {
    checkNum.value += 1;
    if (checkNum.value == 1) {
      cangeCheckState(true);
      // checkState.value = true;
      await fetchDubData();
    } else {
      try {
        await sampleOutVerifyOut(leftSelectData.value.outPlanBatchNo);
        message.success(t('操作成功'));
        fetchDubData();
      } catch (error: any) {
        error.message && message.error(error.message);
      }
    }
  };

  // 设置二级列表的ref
  const setExpandRef = (key: any, ref: any) => {
    expandedTableMap[key].setRef(ref);
  };

  // 标本出库
  const delivery = () => {
    Modal.confirm({
      title: t('是否进行标本出库?'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          // await sampleOutVerifyOut(leftSelectData.value.outPlanBatchNo);

          // message.success(t('操作成功'));
          // fetchDubData();
          if (checkUserRef.value) {
            checkUserRef.value.open = true;
          }
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };

  onActivated(() => {
    checkNum.value = 0;
    if (checkUserRef.value) {
      checkUserRef.value.open = true;
    }
  });

  onMounted(() => {
    checkNum.value = 0;
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
