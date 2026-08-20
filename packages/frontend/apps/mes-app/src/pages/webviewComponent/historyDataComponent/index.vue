<template>
  <BMLayout>
    <BMModal
      v-model="open"
      :title="t('历史数据')"
      size="small"
      position="right"
      closable
      hidden-button
      @close="historyDataComponentClose"
      @click-modal="historyDataComponentClose"
    >
      <view class="history-container">
        <view>
          <wd-button
            v-if="
              (!componentData.formulaId
                && (!viewOnly
                  && (componentData.componentType === 'SUBMIT_SIGN'
                    || componentData.componentType === 'REVIEW_SIGN')))
                || (productionRevision
                  && ['SUBMIT_SIGN', 'REVIEW_SIGN'].includes(
                    componentData.componentType,
                  ))
            "
            block
            style="margin-bottom: 9.38rpx;"
            @click="continueSign"
          >
            {{ t("继续签名") }}
          </wd-button>
          <wd-button v-if="showButton" block @click="dataRevisionHandleClick">
            {{ buttonText }}
          </wd-button>
          <wd-button
            v-if="
              componentData.componentType === 'NUMBER'
                && hasPermission('121010001001013')
            "
            style="margin-top: 9.38rpx;"
            block
            type="info"
            @click="toTrendAnalysis"
          >
            {{ t("趋势分析") }}
          </wd-button>
        </view>
        <scroll-view scroll-y="auto" class="history-box">
          <HistoryItem
            v-for="(item, index) in historyDataList"
            :key="index"
            :item="item"
            :component-data="componentData"
            style="margin-bottom: 9.38rpx;"
          />
        </scroll-view>
      </view>
    </BMModal>

    <!-- 数据修订弹窗 -->
    <DataRevisionModal
      v-model="showDateRevision"
      :component-data="componentData"
      @update="getFieldDataList"
    />
    <BMSignModal
      v-model:show="showHandleSignPopup"
      v-model="handleSignValue"
      :label-list="handleSignLabelList"
      :title="t('数据修订')"
      :signature-data="curParams"
      :show-remark="true"
      :remark-required="true"
      @confirm="handleSignConfirm"
    />
    <BMModal
      v-model="showTipPopup"
      :show-title="false"
      size="small"
      custom-class="tip-popup"
      :close-on-click-modal="false"
      :confirm-text="t('去录入')"
      @confirm="confirmTipPopup"
      @cancel="cancelTipPopup"
    >
      <view class="tip">
        {{ t("您还没有录入手写签名") }}
      </view>
    </BMModal>
    <!-- 继续签名弹窗 -->
    <BMSignModal
      v-model:show="showContinueSignPopup"
      v-model="handleContinueSignValue"
      v-model:current-time="handleContinueSignCurrentTimeValue"
      :label-list="handleContinueSignLabelList"
      :title="t('继续签名')"
      :signature-data="continueSignParams"
      @confirm="handleContinueSignConfirm"
    />
  </BMLayout>
</template>

<script setup>
import { BMLayout, BMModal, BMSignModal } from '@/BMComponents/index.js';
import { useSubNvueLinster } from '@/pages/webview/hooks/useSubNvueLinster.js';
import { batchQuantityPick } from '@/pages/webview/logic/fn/batchQuantityPick.js';
// 清场执行
import { clearanceExecution } from '@/pages/webview/logic/fn/clearanceExecution.js';
// 清场检查
import { clearanceInspection } from '@/pages/webview/logic/fn/clearanceInspection.js';
// 清场信息
import { clearingInformation } from '@/pages/webview/logic/fn/clearingInformation.js';
import { equipmentDataAcquisition } from '@/pages/webview/logic/fn/equipmentDataAcquisition.js';
// 设备数采绘图
import { equipmentDataDraw } from '@/pages/webview/logic/fn/equipmentDataDraw.js';
import { equipmentInfo } from '@/pages/webview/logic/fn/equipmentInfo.js';
import { feedRecycling } from '@/pages/webview/logic/fn/feedRecycling.js';
import { historyDataComponentClose } from '@/pages/webview/logic/fn/index.js';
import { ingredientsInput } from '@/pages/webview/logic/fn/ingredientsInput.js';
import { ingredientsPlan } from '@/pages/webview/logic/fn/ingredientsPlan.js';
// 检验结果组件
import { inspectionResults } from '@/pages/webview/logic/fn/inspectionResults.js';
// 配液投入
import { liquidInvest } from '@/pages/webview/logic/fn/liquidInvest.js';
// 配液量取
import { liquidMeasure } from '@/pages/webview/logic/fn/liquidMeasure.js';
// 配液产出
import { liquidOutput } from '@/pages/webview/logic/fn/liquidOutput.js';
// 配液计划
import { liquidPlan } from '@/pages/webview/logic/fn/liquidPlan.js';
import { materialInfo } from '@/pages/webview/logic/fn/materialInfo.js';
import { materialInput } from '@/pages/webview/logic/fn/materialInput.js';
import { materialQuantityPick } from '@/pages/webview/logic/fn/materialQuantityPick.js';
import { materialReservation } from '@/pages/webview/logic/fn/materialReservation.js';
import { outputWeighing } from '@/pages/webview/logic/fn/outputWeighing.js';
import { pickingReceiving } from '@/pages/webview/logic/fn/pickingReceiving.js';
import { productOutput } from '@/pages/webview/logic/fn/productOutput.js';
import {
  isBusinessComponent,
  productionRevision,
  viewOnly,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
// 称量数据
import { weighingData } from '@/pages/webview/logic/fn/weighingData.js';
import { weighingIngredients } from '@/pages/webview/logic/fn/weighingIngredients.js';
import {
  getSignFormat,
} from '@/pages/webview/utils/fns.js';
import { usePermissionStore } from '@/stores/permission.js';
import { t } from '@/utils/useBmosI18n.js';
import { onShow } from '@dcloudio/uni-app';
import { computed, nextTick, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import DataRevisionModal from './components/dataRevisionModal';
import HistoryItem from './components/historyItem.vue';
import {
  useContinueSign,
  useHandleSign,
  useHistoryData,
} from './hooks/index.js';

const { hasPermission } = usePermissionStore();
const { showNotify } = useNotify();

const open = ref(true);
const showDateRevision = ref(false);
const componentData = ref({});
const signFormat = ref('yyyy-MM-dd HH:mm:ss');
const { historyDataList, getFieldDataList } = useHistoryData({
  componentData,
});

const {
  showHandleSignPopup,
  handleSignValue,
  handleSignLabelList,
  curParams,
  showTipPopup,
  confirmTipPopup,
  cancelTipPopup,
  handleSignConfirm,
  openHandleSignPopup,
} = useHandleSign({
  componentData,
  getFieldDataList,
  showNotify,
});
const {
  showContinueSignPopup,
  handleContinueSignValue,
  handleContinueSignLabelList,
  handleContinueSignCurrentTimeValue,
  continueSignParams,
  continueSign,
  handleContinueSignConfirm,
} = useContinueSign({
  componentData,
  getFieldDataList,
  showNotify,
  signFormat,
});
  // 按钮文字
const buttonText = computed(() => {
  switch (componentData.value.componentType) {
    case 'BATCH_QUANTITY_PICK':
      return t('批次量领料');
    case 'MATERIAL_QUANTITY_PICK':
      return t('按物料领料');
    case 'PICKING_RECEIVING':
      return t('领料接收');
    case 'INGREDIENTS_PLAN':
      return t('配料计划');
    case 'WEIGHING_INGREDIENTS':
      return t('配料称量');
    case 'PRODUCT_OUTPUT':
      return t('成品产出');
    case 'EQUIPMENT_INFO':
      return t('设备信息');
    case 'EQUIPMENT_DATA_ACQUISITION':
      return t('设备数采');
    case 'INGREDIENTS_INPUT':
      return t('配料投入');
    case 'FEED_RECYCLE':
      return t('生产投料');
    case 'OUTPUT_WEIGHING':
      return t('中间品产出');
    case 'MATERIAL_RESERVE':
      return t('物料预定');
    case 'CLEAN_CHECK':
      return t('清场检查');
    case 'CLEAN_INFO':
      return t('清场信息');
    case 'CLEAN_IMPLEMENT':
      return t('清场执行');
    case 'LIQUID_PREPARATION_PLAN':
      return t('配液计划');
    case 'LIQUID_PREPARATION_MEASURE':
      return t('配液量取');
    case 'LIQUID_PREPARATION_INPUT':
      return t('配液投入');
    case 'LIQUID_PREPARATION_OUTPUT':
      return t('配液产出');
    case 'MATERIAL_INFO':
      return t('物料件信息');
    case 'WEIGHING_DATA':
      return t('称量数据');
    case 'EQUIPMENT_DATA_DRAW_LIST':
      return t('设备数采绘图');
    case 'MATERIAL_INPUT':
      return t('物料投入');
    case 'INSPECTION_RESULTS':
      return t('检验结果');
    default:
      return t('数据修订');
  }
});
  // 是否展示按钮
const showButton = computed(() => {
  return (
    (!componentData.value.formulaId
      && (!viewOnly.value && !isBusinessComponent(componentData.value)))
    || (productionRevision.value
      && [
        'TEXT',
        'NUMBER',
        'SELECT',
        'RADIO',
        'CHECKBOX',
        'TIME',
        'DATE',
        'HANDLE_SUBMIT_SIGN',
        'HANDLE_REVIEW_SIGN',
        'SUBMIT_SIGN',
        'REVIEW_SIGN',
      ].includes(componentData.value.componentType))
  );
});

useSubNvueLinster('page-historyDataComponent', async (data) => {
  componentData.value = data;
  getFieldDataList(data);
  signFormat.value = await getSignFormat();
});
onShow(() => {
  if (componentData.value.fieldId) {
    nextTick(() => {
      setTimeout(() => {
        historyDataComponentClose();
      }, 500);
    });
  }
});

// 跳转至趋势分析页面
const toTrendAnalysis = () => {
  const params = {
    id: componentData.value.fieldId,
  };
  const query = Object.keys(params)
    .map(
      key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`,
    )
    .join('&');
  uni.navigateTo({
    url: `/pages/webviewComponent/TrendAnalysis/index?${query}`,
  });
};

// 打开数据修订弹窗
const dataRevisionHandleClick = () => {
  if (!componentData.value.hasRight) {
    uni.showToast({
      title: t('缺少工位操作权限'),
      icon: 'none',
    });
    return;
  }
  switch (componentData.value.componentType) {
    case 'BATCH_QUANTITY_PICK':
      batchQuantityPick(componentData.value);
      break;
    case 'MATERIAL_QUANTITY_PICK':
      materialQuantityPick(componentData.value);
      break;
    case 'PICKING_RECEIVING':
      pickingReceiving(componentData.value);
      break;
    case 'INGREDIENTS_PLAN':
      ingredientsPlan(componentData.value);
      break;
    case 'WEIGHING_INGREDIENTS':
      weighingIngredients(componentData.value);
      break;
    case 'OUTPUT_WEIGHING':
      outputWeighing(componentData.value);
      break;
    case 'PRODUCT_OUTPUT':
      productOutput(componentData.value);
      break;
    case 'EQUIPMENT_INFO':
      equipmentInfo(componentData.value, 2);
      break;
    case 'EQUIPMENT_DATA_ACQUISITION':
      equipmentDataAcquisition(componentData.value, 2);
      break;
    case 'INGREDIENTS_INPUT':
      ingredientsInput(componentData.value, 2);
      break;
    case 'FEED_RECYCLE':
      feedRecycling(componentData.value);
      break;
    case 'MATERIAL_RESERVE':
      materialReservation(componentData.value);
      break;
    case 'CLEAN_CHECK':
      clearanceInspection(componentData.value);
      break;
    case 'CLEAN_INFO':
      clearingInformation(componentData.value);
      break;
    case 'CLEAN_IMPLEMENT':
      clearanceExecution(componentData.value);
      break;
    case 'LIQUID_PREPARATION_PLAN':
      liquidPlan(componentData.value);
      break;
    case 'MATERIAL_INPUT':
      materialInput(componentData.value, 2);
      break;
    case 'LIQUID_PREPARATION_INPUT':
      liquidInvest(componentData.value, 2);
      break;
    case 'HANDLE_SUBMIT_SIGN':
    case 'HANDLE_REVIEW_SIGN':
      openHandleSignPopup();
      break;
    case 'LIQUID_PREPARATION_MEASURE':
      liquidMeasure(componentData.value);
      break;
    case 'LIQUID_PREPARATION_OUTPUT':
      liquidOutput(componentData.value);
      break;
    case 'MATERIAL_INFO':
      historyDataComponentClose();
      materialInfo(componentData.value, 2);
      break;
    case 'WEIGHING_DATA':
      weighingData(componentData.value);
      break;
    case 'EQUIPMENT_DATA_DRAW_LIST':
      equipmentDataDraw(componentData.value);
      break;
    case 'INSPECTION_RESULTS':
      inspectionResults(componentData.value);
      break;
    default:
      showDateRevision.value = true;
      break;
  }
};
</script>

<style lang="scss" scoped>
.history-container {
  width: 269.53rpx;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  .history-box {
    height: 100%;
    padding-top: 9.38rpx;
    box-sizing: border-box;
    flex: 1;
    overflow: auto;
  }
}
</style>
