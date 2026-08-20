<!-- 称量数据-称量 -->
<template>
  <BMLayout>
    <BMBasicPage
      :title="t('称量')"
      :confirm-text="t('确定')"
      :cancel-text="t('上一步')"
      :default-padding="false"
      :loading="confirmLoading"
      @left-click="toBack"
      @confirm="handleNextStep"
      @cancel="handlePreviousStep"
    >
      <template #titleRight>
        <wd-button type="text" @click="toResult">
          {{ t("称量结果") }}
        </wd-button>
      </template>
      <view style="height: 100%">
        <view class="steps-box">
          <wd-steps :active="1" align-center>
            <wd-step :title="t('设备&模式')" />
            <wd-step :title="t('称量')" />
          </wd-steps>
        </view>
        <view class="material-weighing-content">
          <view class="machine-box">
            <view v-if="auto" class="weighing-machine-content">
              <view class="display">
                <view class="left">
                  <view class="scale-info">
                    {{ t("称量范围") }}：{{
                      `${selectedBalance.minRange ? `${selectedBalance.minRange}-${selectedBalance.maxRange}` : '-'}${selectedBalance.unit}`
                    }}
                  </view>
                  <view class="scale-info">
                    {{ t("称量精度") }}：{{
                      selectedBalance.precision + selectedBalance.unit
                    }}
                  </view>
                </view>
                <view class="right">
                  <text class="number">
                    {{ weightInfo.weight }}
                  </text>
                  <text class="unit">
                    {{ selectedBalance.unit }}
                  </text>
                </view>
              </view>
            </view>
            <view v-else>
              <BMForm ref="formRef" v-bind="formProps" />
            </view>
          </view>
        </view>
      </view>
    </BMBasicPage>
    <!-- 称量结果确认 -->
    <BMModal
      v-model="showWeighingResult"
      :title="t('结果确认')"
      size="large"
      :closable="false"
      :close-on-click-modal="false"
      :cancel-text="t('完成称量')"
      cancel-button-type="success"
      :confirm-text="t('继续称量')"
      @cancel="handleResultFinish"
      @confirm="handleResultConfirm"
    >
      <view class="result-content">
        <view class="result-table-title">
          {{ t("称量信息") }}
        </view>
        <view class="result-table-box">
          <BMTable ref="resultTableRef" v-bind="resultTableProps" />
        </view>
      </view>
    </BMModal>
  </BMLayout>
</template>

<script setup>
import {
  BMBasicPage,
  BMForm,
  BMLayout,
  BMModal,
  BMTable,
} from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { useMaterialWeighing } from './hooks/useMaterialWeighing.jsx';

const {
  auto,
  query,
  selectedBalance,
  formRef,
  formProps,
  weightInfo,
  showWeighingResult,
  resultTableRef,
  resultTableProps,
  confirmLoading,
  toBack,
  handlePreviousStep,
  handleNextStep,
  toResult,
  handleResultFinish,
  handleResultConfirm,
} = useMaterialWeighing();

onLoad((e) => {
  query.value = e;
});
</script>

<style lang="scss" scoped>
.steps-box {
  background-color: var(--bmos-bg-color);
}
.material-weighing-content {
  margin-top: 18.75rpx;
  padding: 0 9.38rpx;
  box-sizing: border-box;
  .machine-box {
    margin-bottom: 4.69rpx;
    .weighing-machine-content {
      border-radius: 5.86rpx;
      box-shadow: 0px 0px 3px 0.5px #00000033;
      background-color: #edeff2;
      height: 100%;
      overflow-y: auto;
      .display {
        color: var(--bmos-color-white);
        background-color: #333333;
        padding: 9.96rpx 9.38rpx;
        border-radius: 5.86rpx;
        display: flex;
        justify-content: space-between;
        .left {
          display: flex;
          flex-direction: column;
          justify-content: flex-end;
          font-size: 9.38rpx;
          color: var(--bmos-color-text-placeholder);
          .scale-info {
            margin-top: 9.38rpx;
            height: 11.72rpx;
          }
        }
        .right {
          height: 62.11rpx;
          .number {
            font-family: Digital Numbers;
            font-size: 48.05rpx;
          }
          .unit {
            font-size: 23.44rpx;
            vertical-align: bottom;
          }
        }
      }
    }
  }
}

.result-content {
  height: 280.08rpx;
  .result-table-title {
    margin: 0 0 9.38rpx;
    font-size: 14.06rpx;
    color: var(--bmos-color-text-title);
  }
  .result-table-box {
    height: 253.13rpx;
  }
}
</style>
