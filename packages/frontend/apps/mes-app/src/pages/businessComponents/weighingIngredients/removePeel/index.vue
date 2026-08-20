<template>
  <BMLayout>
    <view class="container">
      <BmosNavBar @left-click="toBack">
        <template #left>
          <view class="left-content">
            <uv-icon
              color="#797C80"
              name="fanhui"
              size="14.07rpx"
              custom-prefix="bmos-icon"
            />
            <text class="title">
              {{ t("配料称量") }}
            </text>
          </view>
        </template>
        <template #right>
          <view class="right-content" @click="toResult">
            {{ t("称量结果") }}
          </view>
        </template>
      </BmosNavBar>
      <scroll-view scroll-y="auto" class="content">
        <uv-steps :current="operationType == 2 || !auto ? '3' : '2'">
          <uv-steps-item :title="t('物料信息')" />
          <uv-steps-item :title="t('模式&设备')" />
          <uv-steps-item :title="t('清零&去皮')" />
          <uv-steps-item :title="t('称量')" />
        </uv-steps>

        <view class="material_msg">
          <view class="material_msg_title">
            <uv-row custom-style="margin-bottom: 10px">
              <uv-col span="6">
                <view>
                  <text class="label">
                    {{ t("物料信息") }}：
                  </text>
                  <text>{{ `${detailData.storageMaterialCode}-${detailData.storageMaterialName}` }}</text>
                </view>
              </uv-col>
              <uv-col span="4">
                <view>
                  <text class="label">
                    {{ t("物料批次") }}：
                  </text>
                  <text>{{ detailData.storageMaterialBatchNo }}</text>
                </view>
              </uv-col>
              <uv-col span="2">
                <view class="material_msg_content_btn" @click="addMaterials">
                  <view class="">
                    {{ t("添加物料") }}
                  </view>
                </view>
              </uv-col>
            </uv-row>
          </view>
          <view class="material_msg_content">
            <view class="material_msg_content_box">
              <view class="material_msg_content_item">
                <view class="label">
                  {{ t("物料总量") }}：
                </view>
                {{ detailData.consumeTotalQuantity }}
              </view>
              <view class="line" />
              <view class="material_msg_content_item">
                <view class="label">
                  {{ t("目标量") }}：
                </view>
                {{ detailData.targetTotalQuantity }}
              </view>
              <view class="line" />
              <view class="material_msg_content_item">
                <view class="label">
                  {{ t("已称量") }}：
                </view>
                <view class="green">
                  {{ detailData.weighedQuantity }}
                </view>
              </view>
              <view class="line" />
              <view class="material_msg_content_item">
                <view class="label">
                  {{ t("未称量") }}：
                </view>
                <view class="orange">
                  {{ detailData.unWeighedQuantity }}
                </view>
              </view>
              <view class="line" />
              <view class="material_msg_content_item">
                <view class="label">
                  {{ t("单位") }}：
                </view>
                {{ detailData.unit }}
              </view>
            </view>
            <view class="material_msg_content_btn" @click="completeWeighing">
              {{ t("完成称量") }}
            </view>
          </view>
          <view v-if="!auto" class="material_msg_content">
            <view class="material_msg_content_box">
              <view class="material_msg_content_item">
                <view class="label">
                  {{ t("称量模式") }}：
                </view>
                {{
                  detailData.weighProcess && detailData.weighProcess.value === 1
                    ? t("配料称量")
                    : t("余料称量")
                }}
              </view>
              <view class="line" />
              <view class="material_msg_content_item">
                <view class="label">
                  {{ t("允差范围") }}：
                </view>
                {{ toleranceRange }}
              </view>
              <view class="line" />
              <view class="material_msg_content_item">
                <view class="label">
                  {{ t("目标范围") }}：
                </view>
                <view class="label">
                  {{ targetRange }}
                </view>
              </view>
            </view>
          </view>
        </view>
        <view v-if="auto" class="show_num">
          <view class="show_num_box">
            <view class="show_num_left">
              <view class="show_num_left_label">
                <view>{{ t("称量范围") }}:</view>
                <view>{{ t("称量精度") }}:</view>
                <view>{{ t("称量模式") }}:</view>
              </view>
              <view class="show_num_left_content">
                <view>
                  {{
                    `${selectedBalance.minRange}-${selectedBalance.maxRange}${selectedBalance.unit}`
                  }}
                </view>
                <view>
                  {{ `${selectedBalance.precision}${selectedBalance.unit}` }}
                </view>
                <view>
                  {{
                    detailData.weighProcess && detailData.weighProcess.value === 1
                      ? t("配料称量")
                      : t("余料称量")
                  }}
                </view>
              </view>
            </view>
            <view class="show_num_right">
              {{ weightInfo.weight }}
              <view class="unit">
                {{ detailData.unit }}
              </view>
            </view>
          </view>
          <view class="show_num_content">
            <view class="show_num_weighing">
              <view class="show_num_target">
                <text class="label">
                  {{ t("目标量") }}:
                </text>
                <text class="target_content">
                  {{ detailData.unWeighedQuantity }}
                  {{ detailData.unit }}
                </text>
                <text class="label">
                  {{ t("允差范围") }}:
                </text>
                <text class="target_content">
                  {{ toleranceRange }}
                </text>
              </view>
              <view class="show_num_residue">
                <text class="label">
                  {{ t("剩余量") }}:
                </text>
                <text style="color: #ff9933">
                  {{ remainingAmount }}{{ detailData.unit }}
                </text>
              </view>
            </view>
            <LineProgress
              v-if="operationType === 2"
              ref="lineProgressRef"
              :detail-data="detailData || {}"
              :weight="netWeight"
            />
            <view class="weights_box">
              <uv-row justify="space-between" gutter="10">
                <uv-col span="4">
                  <view class="weights_item">
                    <view class="label">
                      {{ t("皮重") }}
                    </view>
                    <view class="weights_num">
                      {{ operationType === 0 || operationType === 1 ? "" : tare }}
                    </view>
                  </view>
                </uv-col>
                <uv-col span="4">
                  <view class="weights_item">
                    <view class="label">
                      {{ t("净重") }}
                    </view>
                    <view class="weights_num">
                      {{
                        operationType === 0 || operationType === 1
                          ? ""
                          : netWeight
                      }}
                    </view>
                  </view>
                </uv-col>
                <uv-col span="4">
                  <view class="weights_item">
                    <view class="label">
                      {{ t("毛重") }}
                    </view>
                    <view class="weights_num">
                      {{
                        operationType === 0 || operationType === 1
                          ? ""
                          : grossWeight
                      }}
                    </view>
                  </view>
                </uv-col>
              </uv-row>
            </view>
          </view>
        </view>
        <view v-else class="show_manual">
          <uv-row justify="space-between" gutter="10">
            <uv-col span="4">
              <wd-cell :title="t('皮重')" vertical>
                <wd-input
                  v-model="tare"
                  custom-input-class="default-input"
                  :placeholder="t('请输入')"
                  no-border
                  placeholder-style="color: #B6B9BF; font-size: 12.89rpx; font-weight: 500;"
                />
              </wd-cell>
            </uv-col>
            <uv-col span="4">
              <wd-cell :title="t('毛重')" vertical>
                <wd-input
                  v-model="grossWeightManual"
                  custom-input-class="default-input"
                  :placeholder="t('请输入')"
                  no-border
                  placeholder-style="color: #B6B9BF; font-size: 12.89rpx; font-weight: 500;"
                />
              </wd-cell>
            </uv-col>
            <uv-col span="4">
              <wd-cell :title="t('净重')" vertical>
                <wd-input
                  v-model="netWeightManual"
                  custom-input-class="default-input"
                  :placeholder="t('请输入皮重和毛重')"
                  readonly
                  no-border
                  placeholder-style="color: #B6B9BF; font-size: 12.89rpx; font-weight: 500;"
                />
              </wd-cell>
            </uv-col>
          </uv-row>
        </view>
        <view class="scan">
          <uv-row justify="space-between" gutter="10">
            <uv-col span="6">
              <view class="scan-item">
                <wd-input
                  v-model="container"
                  :placeholder="t('容器')"
                  no-border
                  custom-class="custom-input"
                  use-suffix-slot
                  @confirm="getContainerAndStorageInfo('04', container)"
                />
                <!-- #ifdef APP-PLUS -->
                <view class="scan-icon-box" @click.stop="iconClick('04')">
                  <uv-icon name="scan" size="16.41rpx" color="#434C59" />
                </view>
                <!-- #endif -->
                <!-- #ifdef H5 -->
                <view class="scan-icon-box">
                  <wd-button type="text" @click="iconClick('04')">
                    {{ t("确定") }}
                  </wd-button>
                </view>
              <!-- #endif -->
              </view>
            </uv-col>
            <uv-col span="6">
              <view class="scan-item">
                <wd-input
                  v-model="storage"
                  :placeholder="t('货位')"
                  no-border
                  custom-class="custom-input"
                  use-suffix-slot
                  readonly
                  @click="openStorageModal"
                />
                <!-- #ifdef APP-PLUS -->
                <view class="scan-icon-box" @click.stop="iconClick('03')">
                  <uv-icon name="scan" size="16.41rpx" color="#434C59" />
                </view>
                <!-- #endif -->
                <!-- #ifdef H5 -->
                <view class="scan-icon-box">
                  <wd-button type="text" @click="iconClick('03')">
                    {{ t("确定") }}
                  </wd-button>
                </view>
              <!-- #endif -->
              </view>
            </uv-col>
          </uv-row>
        </view>
      </scroll-view>
      <view class="buttons-box">
        <wd-row :gutter="16">
          <wd-col :span="12">
            <wd-button block @click="previousStep">
              {{ t('上一步') }}
            </wd-button>
          </wd-col>
          <wd-col :span="12">
            <wd-button
              v-if="auto"
              block
              :loading="loading"
              @click="submit"
            >
              {{ submitBtnText[operationType] }}
            </wd-button>
            <wd-button
              v-else
              block
              :loading="loading"
              @click="submit"
            >
              {{ t('称量打码') }}
            </wd-button>
          </wd-col>
        </wd-row>
      </view>
    </view>
    <confilrmation
      ref="configRef"
      :detail-data="confirmDetailsData"
      :current-process="currentProcess"
      :component-id="componentId"
      @continue-weighing="continueWeighing"
    />
    <!-- 添加物料弹框 -->
    <addMaterialsPopup
      v-model="addMaterialOpen"
      :detail-data="detailData"
      @confirm="queryWeighDetailByPlanIdAndBatchId"
    />
    <!-- 完成称量签名组件 -->
    <BMSignModal
      v-model:show="signOpen"
      v-model="signValue"
      :title="t('是否直接完成称量')"
      :label-list="labelList"
      :signature-data="signatureData1"
      @confirm="weighFinishSign"
    />
    <!-- 余料称量超出范围签名 -->
    <BMSignModal
      v-model:show="beyondSignOpen"
      v-model="beyondSignValue"
      :title="t('称量超出范围，请签名确认')"
      :label-list="beyondLabelList"
      :signature-data="signatureData2"
      @confirm="beyondSignConfirm"
    />
    <!-- 打印 -->
    <BmosPrinter ref="bmosPrinterInstance" @jump-over="handleWeighPrint" />
    <!-- 选择货位弹窗 -->
    <BMTreeModal
      v-model="storageId"
      v-model:open="showPositionModal"
      :title="t('暂存货位')"
      :tree-data="treePositionData"
      :field-names="{
        name: 'name',
        key: 'id',
        checkKey: 'level.value',
        checkKeyValue: 4,
        parentId: 'parentId',
        children: 'children',
      }"
      @confirm="selectStorage"
    />
  </BMLayout>
</template>

<script setup>
import { BMLayout, BMSignModal, BMTreeModal } from '@/BMComponents/index.js';
import BmosNavBar from '@/components/BmosNavBar/index.vue';
import BmosPrinter from '@/components/BmosPrinter/index.vue';
import { throttle } from '@/utils/func.js';
import { t } from '@/utils/useBmosI18n.js';
import { useScan } from '@/utils/useScan.js';
import { computed, ref } from 'vue';
import addMaterialsPopup from './component/addMaterialsPopup.vue';
import confilrmation from './component/confirmation.vue';
import LineProgress from './component/lineProgress.vue';
import { useRemovePeel, useSocket } from './hooks/index.js';

const props = defineProps({
  auto: {
    type: String,
    default: 'false',
  },
  componentId: {
    type: String,
    default: '',
  },
});

const { bmosScanCode } = useScan();

const labelList = computed(() => {
  return [
    {
      label: t('操作人'),
      signatureAction: detailData.value.weighProcess
        && detailData.value.weighProcess.value === 1
        ? 66
        : 80,
      menuId:
          detailData.value.weighProcess
          && detailData.value.weighProcess.value === 1
            ? '121010001002005'
            : '121010001002006',
    },
  ];
});
const beyondLabelList = computed(() => {
  return [
    {
      label: t('操作人'),
      signatureAction: 64,
      menuId: '121010001002007',
    },
  ];
});
  // 手动称量目标范围
const targetRange = computed(() => {
  const key
      = detailData.value.weighProcess && detailData.value.weighProcess.value === 1
        ? 'toleranceDiff'
        : 'oddToleranceDiff';
  const weighingRange = detailData.value[key];
  return `${weighingRange[0] || weighingRange[1]}~${
    weighingRange[2] || weighingRange[1]
  }`;
});
const auto = ref(props.auto === '0');
const {
  getReadings,
  clearZero,
  removePeel,
  weightInfo,
  selectedBalance,
  isBackMessage,
  clearZeroMessage,
  removePeelMessage,
} = useSocket({ auto });
const {
  loading,
  container,
  storage,
  storageId,
  detailData,
  operationType,
  submitBtnText,
  tare,
  netWeight,
  grossWeight,
  netWeightManual,
  grossWeightManual,
  remainingAmount,
  toleranceRange,
  configRef,
  treePositionData,
  showPositionModal,
  bmosPrinterInstance,
  confirmDetailsData,
  currentProcess,
  beyondSignOpen,
  signValue,
  beyondSignValue,
  signatureData1,
  signatureData2,
  weighAndPrint,
  handleWeighPrint,
  continueWeighing,
  queryWeighDetailByPlanIdAndBatchId,
  weighFinishSignConfirm,
  getContainerAndStorageInfo,
  openStorageModal,
  selectStorage,
  beyondSignConfirm,
  lineProgressRef,
} = useRemovePeel({
  weightInfo,
  props,
  getReadings,
  clearZero,
  removePeel,
  auto,
  isBackMessage,
  selectedBalance,
});

// 完成称量签名组件
const signOpen = ref(false);

// 添加物料弹窗
const addMaterialOpen = ref(false);

// 扫描
const iconClick = async (typeCode) => {
  const success = async (res) => {
    const { result } = res;
    if (!result) {
      return;
    }
    const type = result.slice(0, 2);
    const code = result.slice(2);
    if (typeCode === '03') {
      if (type !== '03' || !code) {
        uni.showToast({
          title: t('请扫描正确的货位号'),
          icon: 'none',
        });
        return;
      }
    }
    if (typeCode === '04') {
      if (type !== '04' || !code) {
        uni.showToast({
          title: t('请扫描正确的容器号'),
          icon: 'none',
        });
        return;
      }
    }
    getContainerAndStorageInfo(type, code);
  };
    // #ifdef APP-PLUS
  bmosScanCode({
    success,
    fail: (err) => {
      uni.showToast({
        title: t('扫码失败'),
        icon: 'none',
      });
    },
  });
  // #endif
  // #ifdef H5
  success({ result: typeCode === '03' ? storage.value : container.value });
  // #endif
};
  // 返回
const toBack = () => {
  uni.navigateBack();
};
  // 上一步
const previousStep = () => {
  switch (operationType.value) {
    case 0:
      uni.navigateBack();
      break;
    case 1:
      operationType.value = 0;
      break;
    case 2:
      operationType.value = 0;
      tare.value = 0;
      break;
    default:
      break;
  }
};

const clearApi = throttle(() => {
  clearZeroMessage.value = false;
  setTimeout(() => {
    if (clearZeroMessage.value) {
      clearZero();
      operationType.value = 1;
    }
    else {
      uni.showToast({
        title: t('秤具连接异常'),
        icon: 'none',
      });
    }
  }, 250);
}, 300);

const removePeelApi = throttle(() => {
  removePeelMessage.value = false;
  setTimeout(() => {
    if (removePeelMessage.value) {
      if (weightInfo.weight < 0) {
        uni.showToast({
          title: t('称量值为负, 去皮失败'),
          icon: 'none',
        });
        return;
      }
      removePeel();
      operationType.value = 2;
      tare.value = weightInfo.weight;
    }
    else {
      uni.showToast({
        title: t('秤具连接异常'),
        icon: 'none',
      });
    }
  }, 250);
}, 300);

// 下一步
const submit = async () => {
  if (auto.value) {
    switch (operationType.value) {
      case 0:
        clearApi();
        break;
      case 1:
        removePeelApi();
        break;
      case 2:
        await weighAndPrint();
        break;
      default:
        break;
    }
  }
  else {
    if (isNaN(Number(tare.value)) || !tare.value) {
      uni.showToast({
        title: t('请输入正确的皮重'),
        icon: 'none',
      });
      return;
    }
    else {
      if (Number(tare.value) < 0) {
        uni.showToast({
          title: t('皮重不能小于0'),
          icon: 'none',
        });
        return;
      }
    }
    if (isNaN(Number(grossWeightManual.value))) {
      uni.showToast({
        title: t('请输入正确的毛重'),
        icon: 'none',
      });
      return;
    }
    else {
      if (Number(grossWeightManual.value) < 0) {
        uni.showToast({
          title: t('毛重不能小于0'),
          icon: 'none',
        });
        return;
      }
    }
    // 毛重需大于皮重
    if (Number(grossWeightManual.value) <= Number(tare.value)) {
      uni.showToast({
        title: t('毛重需大于皮重'),
        icon: 'none',
      });
      return;
    }
    await weighAndPrint();
  }
};

// 添加物料
const addMaterials = () => {
  addMaterialOpen.value = true;
};
  // 完成称量
const completeWeighing = () => {
  signValue.value = {
    userId1: '',
    userName1: '',
    loginName1: '',
    password1: '',
  };
  signOpen.value = true;
};
  // 完成称量签名确认
const weighFinishSign = () => {
  signOpen.value = false;
  weighFinishSignConfirm();
};
  // 跳转称量结果
const toResult = () => {
  uni.navigateTo({
    url: `/pages/businessComponents/weighingIngredients/weighingResults/index?componentId=${props.componentId}`,
  });
};
</script>

<style lang="scss" scoped>
.container {
  padding-top: 46.89rpx;
  width: 100%;
  overflow: hidden;
  box-sizing: border-box;
  background: #ffffff;

  .left-content {
    display: flex;

    .title {
      font-size: 15.24rpx;
      font-weight: 500;
      line-height: 22.27rpx;
      letter-spacing: 0em;
      color: #18191a;
      margin-left: 14.65rpx;
    }
  }

  .right-content {
    font-size: 15.24rpx;
    color: #2871ff;
  }

  & > .content {
    position: relative;
    width: 100%;
    height: calc(100vh - 46.89rpx - 63.31rpx);
    padding: 5.86rpx 9.38rpx 9.38rpx;
    box-sizing: border-box;
    font-size: 14rpx;
    font-weight: normal;
    background: linear-gradient(to bottom, rgba(255, 255, 255, 1), rgba(234, 235, 236, 1));

    .material_msg {
      width: 100%;
      margin: 4.69rpx 0;
      border-radius: 4.69rpx;
      font-size: 14rpx;
      background-color: #ffffff;

      .label {
        color: #6c6e73;
      }

      .material_msg_title {
        min-height: 32.23rpx;
        // line-height: 32.23rpx;
        width: 100%;
        margin: auto;
        border-bottom: 1rpx solid #e1e3e5;

        .material_msg_content_btn {
          display: flex;
          justify-content: flex-end;

          view {
            width: 100rpx;
            height: 30rpx;
            line-height: 30rpx;
            border-radius: 30rpx;
            background-color: #2871ff;
            color: #fff;
            text-align: center;
          }
        }
      }

      .material_msg_content {
        display: flex;
        align-items: center;
        height: 32.23rpx;
        justify-content: space-between;

        .material_msg_content_box {
          display: flex;
          height: 100%;
          align-items: center;

          .material_msg_content_item {
            display: flex;
          }

          .line {
            margin: 0 10rpx;
            width: 1rpx;
            height: 20rpx;
            background-color: #e1e3e5;
          }

          .green {
            color: #59bf78;
          }

          .orange {
            color: #ff9933;
          }
        }

        .material_msg_content_btn {
          color: #2871ff;
          width: 100rpx;
          text-align: center;
        }
      }
    }

    .show_num {
      width: 100%;
      height: 202.73rpx;
      margin: 5rpx auto;
      border-radius: 4.69rpx;
      background-color: #fff;

      .label {
        color: #6c6e73;
        margin-right: 10rpx;
      }

      .show_num_box {
        height: 100rpx;
        border-radius: 8rpx;
        box-sizing: border-box;
        border: 6rpx solid #999999;
        background-color: #333333;
        display: flex;
        justify-content: space-between;
        align-items: flex-end;

        .show_num_left {
          color: #c2c5cc;
          display: flex;
          align-items: center;
          height: 100%;
          padding-left: 20rpx;

          & > view > view {
            line-height: 24rpx;
          }

          .show_num_left_label {
            width: 80rpx;
          }
        }

        .show_num_right {
          color: #fff;
          height: 100%;
          font-size: 50rpx;
          line-height: 80rpx;
          padding-right: 20rpx;
          display: flex;
          align-items: flex-end;

          .unit {
            height: 80rpx;
            line-height: 90rpx;
            margin-left: 10rpx;
            font-size: 30rpx;
          }
        }
      }

      .show_num_content {
        padding: 0 9.38rpx 9.38rpx;

        .show_num_weighing {
          display: flex;
          align-items: center;
          justify-content: space-between;
          line-height: 22.27rpx;

          .target_content {
            padding-right: 40rpx;
          }
        }
      }

      .weights_box {
        width: 100%;

        .weights_item {
          line-height: 40rpx;
          background-color: #f7f8fa;
          border-radius: 7rpx;
          padding: 0 10rpx;
          display: flex;
          align-items: center;

          .weights_num {
            color: #18191a;
            font-weight: 700;
          }
        }
      }
    }

    .show_manual {
      width: 100%;
      height: 79.69rpx;
      margin: 5rpx auto;
      border-radius: 4.69rpx;
      background-color: #fff;
      :deep(.default-input) {
        width: 100%;
        height: 37.5rpx;
        display: flex;
        align-items: center;
        padding: 11.13rpx;
        box-sizing: border-box;
        border-radius: 4.69rpx;
        margin-right: 15.82rpx;
      }
    }

    .scan {
      width: 100%;
      box-sizing: border-box;
      .scan-item {
        height: 35.16rpx;
        line-height: 35.16rpx;
        border-radius: 4.69rpx;
        background-color: #fff;
        display: flex;
        align-items: center;
        .custom-input {
          flex: 1;
          border: none;
          :deep(.wd-input__body) {
            height: 100%;
            padding: 7.03rpx 9.38rpx;
            box-sizing: border-box;
            border-bottom: none;
            .wd-input__value {
              height: 100%;
            }
          }
        }
        .scan-icon-box {
          // #ifdef H5
          width: 46.88rpx;
          // #endif
          // #ifdef APP-PLUS
          width: 31.64rpx;
          // #endif
          height: 21.09rpx;
          display: flex;
          align-items: center;
          border-left: 1px solid #e1e3e5;
          padding-left: 9.38rpx;
          box-sizing: border-box;
        }
      }
    }

    .col-but {
      padding: 11.72rpx;
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      column-gap: 14.07rpx;
      margin: 5rpx auto 0;
      background-color: #fff;

      .but {
        width: 100%;
        padding: 2.38rpx 14.07rpx;
      }

      .cancel {
        color: var(---, #6c6e73);
        border: 1.17rpx solid var(----, #bbbdbf);
      }
    }
  }

  .flex-zy {
    justify-content: space-between;
  }
  .buttons-box {
    height: 63.31rpx;
    line-height: 63.31rpx;
    width: 100%;
    background-color: #ffffff;
    position: fixed;
    bottom: 0;
    left: 0;
    padding: 10.55rpx 9.38rpx 0;
    box-sizing: border-box;
  }
}
</style>
