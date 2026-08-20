<template>
  <BMLayout>
    <BMBasicPage
      :title="t('工序流程')"
      :default-padding="false"
      :show-buttons="false"
      @left-click="toBack"
    >
      <view class="container">
        <view class="page-title">
          <view class="page_title_box">
            <wd-icon
              class-prefix="bmos-app-icon"
              name="gongxu"
              size="14.06rpx"
              color="#198CFF"
            />
            <view class="page_title_txt">
              <view class="page_title_left">
                {{ t("当前工序") }}
              </view><view style="color: #c2c5cc">
                |
              </view>
              <view class="page_title">
                {{ name }}
              </view>
            </view>
            <wd-tag
              v-if="suspendedState"
              color="#FF4C26"
              bg-color="#FFD5CC"
            >
              {{ t("已暂停") }}
            </wd-tag>
          </view>
          <view class="page_title_btn_box">
            <wd-button
              v-if="pauseFlag && !isProductionHistory"
              v-hasAuth="121010001001016"
              size="small"
              type="info"
              style="margin-right: 14.06rpx;"
              @click="forcedCompletion"
            >
              {{ t("强制完成") }}
            </wd-button>
            <wd-button
              v-if="props.state === '1' && !isProductionHistory"
              type="warning"
              style="background-color: transparent;margin-right: 4.69rpx;"
              size="small"
              @click="redoHandle"
            >
              {{ t("工序重做") }}
            </wd-button>
          </view>
        </view>
        <view class="content">
          <NodeItem
            v-for="(node, index) in allData"
            :key="node.nodeId"
            class="node_item_box"
            :node="node"
            :index="index"
            :completed="completed"
            :completed-index="index - allData.length"
            @click="handleClick(node)"
          />
          <BMNoData v-if="allData?.length === 0" type="emptyData" :text="t('暂无数据')" />
        </view>
        <!-- 签名 -->
        <BMSignModal
          v-model:show="showSign"
          v-model="signValue"
          :label-list="labelList"
          :title="t('是否重做当前工序')"
          :confirm-text="t('重做')"
          :signature-data="curParams"
          @confirm="redoConfirm"
        />
        <!-- 强制开启任务弹窗 -->
        <BMSignModal
          v-model:show="signBoxShow"
          v-model="signopenValue"
          :label-list="labelopenList"
          :title="signTitle"
          :sub-title="signSubTitle"
          :confirm-text="signBoxType === 'start' ? t('开启') : t('完成')"
          :signature-data="curParams"
          @confirm="signConfig"
        />
        <BMMessageBox
          v-model="showMessageBox"
          :title="t('开始执行')"
          :content="t('是否开始当前步骤/任务')"
          @confirm="confirmStart"
        />
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import {
  getHistoryProcessStepNodeApi,
  getProcessPrincipalApi,
  getProcessStepNodeApi,
  processRedoApi,
} from '@/api/productionApi.js';

import { coerceActiveApi, coerceProcedureCompleteApi } from '@/api/todoApi.js';
import { getStepGroupUserApi, reqActiveStepApi } from '@/api/webViewApi.js';
import {
  BMBasicPage,
  BMLayout,
  BMMessageBox,
  BMSignModal,
} from '@/BMComponents';
import BMNoData from '@/BMComponents/NoData/index.vue';
import { t } from '@/utils/useBmosI18n.js';
import { queryParams } from '@climblee/uv-ui/libs/function/index.js';
import { onShow } from '@dcloudio/uni-app';
import { onMounted, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import NodeItem from '../component/NodeItem.vue';

const props = defineProps({
  id: {
    type: String,
    default: '',
  },
  name: {
    type: String,
    default: '',
  },
  nodeId: {
    type: String,
    default: '',
  },
  processId: {
    type: String,
    default: '',
  },
  processVersion: {
    type: String,
    default: '',
  },
  processInstanceId: {
    type: String,
    default: '',
  },
  productName: {
    type: String,
    default: '',
  },
  batchNo: {
    type: String,
    default: '',
  },
  productPlanId: {
    type: String,
    default: '',
  },
  productionHistory: {
    type: String,
    default: '',
  },
  productionRevision: {
    type: String,
    default: '',
  },
  nodeFunction: {
    type: String,
    default: '',
  },
  procedureChangeNumber: {
    type: String,
    default: '',
  },
  processChangeNumber: {
    type: String,
    default: '',
  },
});
const { showNotify } = useNotify();
const isProductionHistory = ref(false);
const isProductionRevision = ref(false);
const suspendedState = ref(null);
const showSign = ref(false);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const labelList = ref([
  {
    label: t('操作人'),
    signatureAction: 10,
  },
  {
    label: t('复核人'),
    signatureAction: 11,
    options: [],
  },
]);
const signopenValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const labelopenList = ref([
  {
    label: t('操作人'),
    signatureAction: 111,
  },
]);
const curParams = ref();
const signBoxShow = ref(false);
const allData = ref([]);
const signTitle = ref('');
const signSubTitle = ref('');
const signBoxType = ref('start');// start强制开启, complete强制完成

const showMessageBox = ref(false);
const messageContent = ref('');
const messageItem = ref(null);
const pauseFlag = ref(false);
const conditionString = ref('');

// 重做方法
const redoHandle = () => {
  showSign.value = true;
  curParams.value = {
    executionId: props.id,
    procedureModelId: props.procedureModelId,
    planId: props.productPlanId,
    state: props.state,
  };
};
const toBack = () => {
  uni.navigateBack();
};
  // 获取工序步骤节点
const getProcessStepNode = async () => {
  allData.value = [];
  const api = isProductionHistory.value
    ? getHistoryProcessStepNodeApi
    : getProcessStepNodeApi;
  try {
    const res = await api({
      executionId: props.id,
      planId: props.productPlanId,
      procedureModelId: props.procedureModelId,
      procedureChangeNumber: props.procedureChangeNumber,
      processChangeNumber: props.processChangeNumber,
      state: props.state,
      ...(props.nodeFunction
        ? {
            nodeFunction: props.nodeFunction,
          }
        : {}),
    });
    if (isProductionHistory.value) {
      // 历史
      allData.value = res.data || [];
      if (!res.data || res.data.length === 0) {
        showNotify({
          type: 'warning',
          message: t('工序无步骤/任务节点信息'),
        });
      }
    }
    else {
      allData.value = res.data?.nodeList || [];
      pauseFlag.value = res.data.pauseFlag;
      conditionString.value = res.data.conditionString;
      if (!res.data || res.data.length === 0) {
        showNotify({
          type: 'warning',
          message: t('工序无步骤/任务节点信息'),
        });
      }
    }
  }
  catch (error) {
    error?.message
    && showNotify({
      type: 'warning',
      message: error.message,
    });
  }
};
onShow(() => {
  console.log('onShow-工序流程页面显示');
  isProductionHistory.value = props.productionHistory === 'true';
  isProductionRevision.value = props.productionRevision === 'true';
  getProcessStepNode();
  suspendedState.value = props.executePaused === 'true';
});

// 重做确认
const redoConfirm = async () => {
  const params = {
    executionId: props.id,
    procedureModelId: props.procedureModelId,
    planId: props.productPlanId,
    state: props.state,
    procedureChangeNumber: props.procedureChangeNumber,
    processChangeNumber: props.processChangeNumber,
  };
  try {
    await processRedoApi(params);
    showSign.value = false;
    getProcessStepNode();
  }
  catch (e) {
    uni.showToast({
      title: e.message,
      icon: 'none',
    });
  }
};

// 获取工序负责人
const getProcessPrincipal = async () => {
  const res = await getProcessPrincipalApi({
    processId: props.processId,
    nodeId: props.nodeId,
    processVersion: props.processVersion,
  });
  labelList.value[1].options = (res.data || []).map((item) => {
    const { userName, loginName, userId } = item;
    return {
      label: userName,
      value: loginName,
      id: userId,
    };
  });
};
const handleClick = async (node, jump = false) => {
  const taskId = '';
  const {
    nodeId,
    processInstanceId,
    executionId,
    activeState,
    nodeFunction,
    planId,
    procedureStepModelId,
    productionLineId,
    state,
  } = node;
  const { processId, processVersion, batchNo, productPlanId, executePaused, productName, productMergeCode }
      = props;
  const params = {
    processInstanceId,
    batchNo,
    nodeId,
    processId,
    productName,
    productMergeCode,
    processVersion,
    productPlanId,
    taskId,
    executionId,
    executePaused,
    nodeFunction: nodeFunction.value,
    procedureChangeNumber: props.procedureChangeNumber,
    processChangeNumber: props.processChangeNumber,
    procedureModelId: props.procedureModelId,
    planId,
    procedureStepModelId,
    productionLineId,
    state,
  };
  if (activeState || jump) {
    // 如果是生产历史状态，复用暂停的参数
    if (isProductionHistory.value) {
      params.executePaused = true;
    }
    // 如果是生产修订,添加修订参数
    if (isProductionRevision.value) {
      params.revision = true;
    }
    if (nodeFunction.value === '3' || nodeFunction.value === '4') {
      // 3工序换班4工艺换班
      uni.navigateTo({
        url: `/pages/processChange/index${queryParams(params)}`,
      });
    }
    else if (nodeFunction.value === '6') {
      // 请验
      uni.navigateTo({
        url: `/pages/pleaseVerify/index${queryParams(params)}`,
      });
    }
    else {
      uni.navigateTo({
        url: `/pages/webview/index${queryParams(params)}`,
      });
    }
  }
  else {
    messageItem.value = node;
    showMessageBox.value = true;
  }
};
const confirmStart = async () => {
  if (!messageItem.value) {
    return;
  }
  try {
    const { procedureStepModelId, executionId } = messageItem.value;
    const { productPlanId } = props;
    await reqActiveStepApi({
      procedureStepModelId,
      planId: productPlanId,
      executionId,
    });
    handleClick(messageItem.value, true);
  }
  catch (error) {
    messageContent.value = error.message;
    const {
      nodeId,
      nodeFunction,
      processChangeNumber,
      procedureChangeNumber,
    } = messageItem.value;
    const { productPlanId } = props;
    // 操作人列表
    const res = await getStepGroupUserApi({
      nodeId,
      productPlanId,
      nodeFunction: nodeFunction.value,
      processChangeNumber,
      procedureChangeNumber,
    });
    labelopenList.value[0].options = (res.data || []).map((item) => {
      const { userName, loginName, userId } = item;
      return {
        label: `${userName}`,
        value: loginName,
        id: userId,
        userName,
      };
    });
    signBoxType.value = 'start';
    signTitle.value = t('未满足执行条件');
    labelopenList.value[0].signatureAction = 111;
    labelopenList.value[0].disabled = false;
    signSubTitle.value = t('未满足执行条件：') + messageContent.value + t('是否开启当前步骤/任务？');
    signBoxShow.value = true;
  }
};
const signConfig = async () => {
  try {
    if (signBoxType.value === 'start') {
      // 强制开启工序步骤
      const { procedureStepModelId, executionId, planId } = messageItem.value;
      await coerceActiveApi({
        executionId,
        planId,
        procedureStepModelId,
        userId: signopenValue.value.userId1,
      });
      signBoxShow.value = false;
      handleClick(messageItem.value, true);
    }
    else {
      // 强制完成工序
      await coerceProcedureCompleteApi({
        executionId: props.id,
        planId: props.productPlanId,
        procedureModelId: props.procedureModelId,
        procedureChangeNumber: props.procedureChangeNumber,
        processChangeNumber: props.processChangeNumber,
        state: props.state,
        ...(props.nodeFunction
          ? {
              nodeFunction: props.nodeFunction,
            }
          : {}),
      });
      signBoxShow.value = false;
      uni.navigateBack({
        delta: 2,
      });
    }
  }
  catch (error) {
    if (error?.code === 0) {
      return;
    }
    error?.message
    && showNotify({
      type: 'warning',
      message: error.message,
    });
  }
};
  // 工序强制完成按钮点击
const forcedCompletion = () => {
  signBoxType.value = 'complete';
  signTitle.value = t('未满足完成条件');
  signSubTitle.value = `${t('未满足完成条件')}：${conditionString.value},${t('是否完成当前工序？')}`;
  labelopenList.value[0].options = undefined;
  labelopenList.value[0].signatureAction = 127;
  labelopenList.value[0].disabled = true;
  curParams.value = {
    executionId: props.id,
    planId: props.productPlanId,
    procedureModelId: props.procedureModelId,
    procedureChangeNumber: props.procedureChangeNumber,
    processChangeNumber: props.processChangeNumber,
    state: props.state,
    ...(props.nodeFunction
      ? {
          nodeFunction: props.nodeFunction,
        }
      : {}),
  };
  signBoxShow.value = true;
};
onMounted(() => {
  getProcessPrincipal();
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
    padding: 9.38rpx;
    width: 100%;
    font-size: 12.9rpx;
    font-weight: 400;
    display: flex;
    align-items: center;
    box-sizing: border-box;
    justify-content: space-between;
    .page_title_box {
      display: flex;
      align-items: center;
      .page_title_txt {
        display: flex;
        margin: 0 9.38rpx;
        .page_title_left {
          color: #6c6e73;
          margin-right: 9.38rpx;
        }
        .page_title {
          margin-left: 9.38rpx;
        }
      }
    }
  }

  .content {
    height: calc(100% - 84.41rpx);
    overflow-y: auto;
    padding: 11.72rpx 9.38rpx 0;
    box-sizing: border-box;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    align-content: flex-start;
    flex-wrap: wrap;
    .node_item_box {
      width: calc(50% - 17.69rpx);
      margin-bottom: 9.38rpx;
    }
  }
}

.sign_box {
  padding: 0 9.38rpx;
  .sign_title {
    color: #9da0a6;
    text-align: center;
    margin-bottom: 14.06rpx;
  }
}
.sign_btn_box {
  display: flex;
  justify-content: space-between;
  .sign_btn {
    height: 33.98rpx;
    line-height: 33.98rpx;
    text-align: center;
    font-size: 12.89rpx;
    border-radius: 4.69rpx;
    width: calc(50% - 4.69rpx);
  }
  .sign_btn_cancle {
    border: 1px solid #b6b9bf;
  }
  .sign_btn_config {
    color: #ff4c26;
    border: 1px solid #ff4c26;
  }
}
</style>
