<template>
  <BMLayout>
    <BMBasicPage
      :title="t('工艺流程')"
      :default-padding="false"
      :show-buttons="false"
      @left-click="toBack"
    >
      <view class="container">
        <view class="page-title">
          <view class="title_top_box">
            <view class="title_top_title">
              <wd-icon
                class-prefix="bmos-app-icon"
                name="chengpinwuliao"
                size="18.75rpx"
                color="#434C59"
              />
              <text style="margin-left: 5rpx;font-size: 14.06rpx">
                {{ props.productMergeCode }}-{{ props.productName }}
              </text>
              <wd-tag v-if="suspendedState" color="#FF4C26" bg-color="#FFD5CC">
                {{ t("已暂停") }}
              </wd-tag>
            </view>
            <view v-if="!isProductionHistory" class="right-button-box">
              <wd-button
                v-hasAuth="121010001001014"
                type="info"
                plain
                size="small"
                @click="associatedBatches"
              >
                {{ t("关联批次") }}
              </wd-button>
              <wd-button
                v-if="suspendedState"
                v-hasAuth="121010001001002"
                type="info"
                plain
                size="small"
                @click="recoverHandle"
              >
                {{ t("恢复生产") }}
              </wd-button>
              <wd-button
                v-else
                v-hasAuth="121010001001001"
                type="info"
                plain
                size="small"
                @click="pauseHandle"
              >
                {{ t("暂停生产") }}
              </wd-button>
              <wd-button
                v-hasAuth="121010001001003"
                type="warning"
                class="warning_btn"
                size="small"
                @click="endHandle"
              >
                {{ t("终止生产") }}
              </wd-button>
            </view>
            <view
              v-if="isProductionHistory && isProductionRevision"
              class="right-button-box"
            >
              <wd-button
                v-hasAuth="121010001001014"
                type="info"
                plain
                size="small"
                @click="associatedBatches"
              >
                {{ t("关联批次") }}
              </wd-button>
            </view>
          </view>
          <view class="title_content">
            <view class="title_content_item">
              {{ t("批号") }}:&nbsp;&nbsp;{{ props.batchNo }}
            </view>
            <view class="title_content_item">
              {{ t("工艺") }}:&nbsp;&nbsp;{{ props.processName }}-{{ props.processVersion }}
            </view>
            <view class="title_content_item">
              {{ t("产线") }}:&nbsp;&nbsp;{{ props.lineName }}
            </view>
          </view>
        </view>
        <view class="content">
          <view
            v-for="(node, index) in allData"
            :key="index"
            class="node_item_box"
          >
            <NodeItem
              :key="node.nodeId"
              :node="node"
              :index="index"
              :completed="completed"
              :completed-index="index - allData.length"
              @click="nodeClickHandle(node)"
            />
          </view>
        </view>
        <!-- 签名 -->
        <BMSignModal
          v-model:show="showSign"
          v-model="signValue"
          :label-list="labelList"
          :title="signTitle"
          :signature-data="curParams"
          @confirm="stopConfirm"
        />
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import {
  getHistoryProcessNodeApi,
  getProcessNodeApi
  ,
  pauseProductionApi,
  recoverProductionApi,
  stopProductionApi,
} from '@/api/productionApi.js';
import { BMBasicPage, BMLayout, BMSignModal } from '@/BMComponents';
import { parseUrlQuery } from '@/utils/url';
import { t } from '@/utils/useBmosI18n.js';
import { queryParams } from '@climblee/uv-ui/libs/function/index.js';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import NodeItem from '../component/NodeItem.vue';

const props = ref({});
const { showNotify } = useNotify();
const showSign = ref(false);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const labelList = ref([
  {
    label: t('操作人'),
    signatureAction: 9,
  },
]);
const curParams = ref({
  signatureData: '',
});
const signTitle = ref('');
const allData = ref([]);

const suspendedState = ref(null);
const isProductionHistory = ref(false);
const isProductionRevision = ref(false);

// 暂停生产方法
const pauseHandle = () => {
  labelList.value[0].signatureAction = 27;
  signValue.value.password1 = '';
  curParams.value.signatureData = props.value.productPlanId;
  signTitle.value = t('是否暂停生产');
  showSign.value = true;
};
  // 恢复生产方法
const recoverHandle = () => {
  signValue.value.password1 = '';
  labelList.value[0].signatureAction = 28;
  curParams.value.signatureData = props.value.productPlanId;
  signTitle.value = t('是否恢复生产');
  showSign.value = true;
};

// 中止生产方法
const endHandle = () => {
  signValue.value.password1 = '';
  labelList.value[0].signatureAction = 112;
  curParams.value.signatureData = props.value.id;
  signTitle.value = t('是否终止生产');
  showSign.value = true;
};

// 关联批次
const associatedBatches = () => {
  uni.navigateTo({
    url: `/pages/production/associatedBatches/index?productPlanId=${props.value.productPlanId}`,
  });
};
const toBack = () => {
  uni.navigateBack();
};
const stopConfirm = async () => {
  showSign.value = false;
  try {
    switch (labelList.value[0].signatureAction) {
      case 112:
        await stopProductionApi(props.value.id);
        signValue.value = {
          loginName1: '',
          password1: '',
          userId1: '',
        };
        break;
      case 27:
        await pauseProductionApi(props.value.productPlanId);
        signValue.value = {
          loginName1: '',
          password1: '',
          userId1: '',
        };
        break;
      case 28:
        await recoverProductionApi(props.value.productPlanId);
        signValue.value = {
          loginName1: '',
          password1: '',
          userId1: '',
        };
        break;
      default:
        break;
    }
    if (labelList.value[0].signatureAction === 112) {
      uni.navigateBack();
      return;
    }
    suspendedState.value = !suspendedState.value;
  }
  catch (e) {
    e.message && showNotify({ type: 'danger', message: e.message });
  }
};
  // 获取工序节点
const getProcessNode = async () => {
  const api = isProductionHistory.value
    ? getHistoryProcessNodeApi
    : getProcessNodeApi;
  const res = await api({
    processInstanceId: props.value.id,
    processVersionId: props.value.processVersionId,
  });
  allData.value = res.data || [];
};
  // 跳转到工序流程页面
const nodeClickHandle = (item) => {
  const {
    executionId,
    name,
    nodeId,
    processInstanceId,
    procedureModelId,
    state,
    nodeFunction,
    procedureChangeNumber,
    processChangeNumber,
  } = item;
  const { processId, processVersion, batchNo, productPlanId, productName, productMergeCode } = props.value;

  const urlQuery = {
    id: executionId,
    name,
    productName,
    productMergeCode,
    processId,
    processVersion,
    processInstanceId,
    nodeId,
    batchNo,
    productPlanId,
    executePaused: suspendedState.value,
    productionHistory: isProductionHistory.value,
    productionRevision: isProductionRevision.value,
    procedureModelId,
    state,
    nodeFunction: nodeFunction?.value || '',
    procedureChangeNumber,
    processChangeNumber,
  };
  uni.navigateTo({
    url: `/pages/production/productionManagement/processFlow/index${queryParams(
      urlQuery,
    )}`,
  });
};

onShow(() => {
  console.log('onShow-工艺流程页面显示');
  isProductionHistory.value = props.value.productionHistory === 'true';
  isProductionRevision.value = props.value.productionRevision === 'true';
  getProcessNode();
  suspendedState.value = props.value.executePaused === 'true';
});
onLoad((e) => {
  props.value = parseUrlQuery(e);
});
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100%;
  overflow: hidden;
  box-sizing: border-box;
  background-image: linear-gradient(to bottom, #ecf2fe 0%, #f2f3f5 40%, #fff 100%);
  border-top-left-radius: 15.72rpx;
  border-top-right-radius: 15.72rpx;

  .page-title {
    width: 100%;
    font-size: 12.9rpx;
    font-weight: 400;
    padding: 9.38rpx 9.38rpx 0;
    box-sizing: border-box;
    .title_top_box {
      display: flex;
      align-items: center;
      justify-content: space-between;
      .title_top_title {
        display: flex;
        align-items: center;
      }
      .right-button-box {
        flex-shrink: 0;
        display: flex;
        gap: 14.07rpx;
        .warning_btn {
          background-color: transparent;
        }
        .btn_item {
          height: 26.13rpx;
          box-sizing: border-box;
          padding: 0 14.06rpx;
          line-height: 26.13rpx;
          border-radius: 3.52rpx;
          border: 1px solid #b6b9bf;
          color: #484a4d;
        }
        .btn_recover {
          color: #fff;
          background-color: #2871ff;
        }
        .btn_error {
          color: #ff4c26;
          border-color: #ff4c26;
        }
      }
    }
    .title_content {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      .title_content_item {
        width: 50%;
        margin-top: 11.72rpx;
        color: #6c6e73;
        font-size: 12.2rpx;
      }
    }
  }

  .content {
    max-height: calc(100% - 84.41rpx);
    overflow-y: auto;
    padding: 0 9.38rpx;
    box-sizing: border-box;
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    .node_item_box {
      width: calc(50% - 4.69rpx);
      margin-top: 9.38rpx;
    }
  }
}
</style>
