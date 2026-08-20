<template>
  <BMLayout>
    <BMBasicPage
      :title="t('设备数采绘图')"
      background-color="#F2F3F5"
      :default-padding="false"
      :show-buttons="false"
      @left-click="toBack"
    >
      <template #titleRight>
        <wd-button type="text" size="small" :loading="loading" @click="savePic">
          {{ t('保存图片') }}
        </wd-button>
      </template>
      <view class="content">
        <view class="form_box">
          <BMForm
            ref="formRef"
            v-bind="formProps"
          />
        </view>
        <view id="charts_box_id" class="charts_box">
          <view v-if="selectEquipmentData" class="line_box">
            <view v-if="isShowLine(selectEquipmentData.correctionLineConfig)" class="line_item_box">
              <view class="line deviation" />
              <view class="line_num">
                {{ t('纠偏线') }}
                {{ getHtml(selectEquipmentData.correctionLineConfig) }}
              </view>
            </view>
            <view v-if="isShowLine(selectEquipmentData.warningLineConfig)" class="line_item_box">
              <view class="line alert" />
              <view class="line_num">
                {{ t('警戒线') }}
                {{ getHtml(selectEquipmentData.warningLineConfig) }}
              </view>
            </view>
            <view v-if="isShowLine(selectEquipmentData.standardLineConfig)" class="line_item_box">
              <view class="line standard" />
              <view class="line_num">
                {{ t('标准线') }}
                {{ getHtml(selectEquipmentData.standardLineConfig) }}
              </view>
            </view>
          </view>
          <LEchart
            v-if="isShowChart"
            ref="echartsDom"
            style="height: 257.11rpx"
          />
        </view>
      </view>
      <!-- 选择设备 -->
      <BMRadioModal
        v-model="selectDevice"
        v-model:open="openSelectDevice"
        :title="t('设备选择')"
        :options="deviceListOptions"
        :required="true"
        :field-names="{
          label: 'showLabel',
          value: 'id',
        }"
        @confirm="selectDeviceConfirm"
      />
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import { BMBasicPage, BMForm, BMLayout, BMRadioModal } from '@/BMComponents';
import LEchart from '@/components/l-echart/l-echart.vue';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { useData } from './hooks/useData';

const {
  formRef,
  formProps,
  echartsDom,
  queryInfo,
  openSelectDevice,
  selectDevice,
  deviceListOptions,
  selectEquipmentData,
  isShowChart,
  loading,
  savePic,
  selectDeviceConfirm,
} = useData();

const getHtml = (data) => {
  let str = '';
  if (data.limitType === 0) {
    // 范围
    // 下限
    if (!data.scopeConfig.lowerValue) {
      str += '(-∞,';
    }
    else {
      str += data.scopeConfig.lowerSymbol === 'LESS_THAN' ? '(' : '[';
      str += `${data.scopeConfig.lowerValue},`;
    }
    // 上限
    if (!data.scopeConfig.upperValue) {
      str += '+∞)';
    }
    else {
      str += data.scopeConfig.upperValue;
      str += data.scopeConfig.upperSymbol === 'LESS_THAN' ? ')' : ']';
    }
  }
  else {
    // 数值
    str = `{${data.fixedValue}}`;
  }
  return str;
};

const isShowLine = (data) => {
  if (data.limitType === 0) {
    // 范围
    return data.scopeConfig.lowerValue || data.scopeConfig.lowerSymbol || data.scopeConfig.upperValue || data.scopeConfig.upperSymbol;
  }
  else {
    // 数值
    return !!data.fixedValue;
  }
};

onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e).map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e[key]),
    ]),
  );
  queryInfo.value = query;
  // #endif
  // #ifdef H5
  queryInfo.value = e;
  // #endif
});
// 返回
const toBack = () => {
  uni.navigateBack();
};
</script>

<style scoped lang="scss">
  .content{
    overflow: hidden;
    .form_box {
      margin: 9.38rpx;
      padding: 11.72rpx 11.72rpx 6rpx;
      box-sizing: border-box;
      background-color: #FFF;
      border-radius: 4.69rpx;
    }
    .charts_box {
      margin: 0 9.38rpx 9.38rpx;
      box-sizing: border-box;
      background-color: #FFF;
      border-radius: 4.69rpx;
      height: 287.11rpx;
      .line_box{
        height: 30rpx;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        gap: 7.03rpx;
        color: #6C6E73;
        padding-right: 18.75rpx;
        font-size: 9.38rpx;
        .line_item_box{
          display: flex;
          gap: 4.69rpx;
          align-items: center;
          .line {
            width: 35.16rpx;
            height: .59rpx;
          }
          .deviation {
            background: linear-gradient(to left,transparent 0%,transparent 50%,#FF4C26 50%,#FF4C26 100%);
            background-size: 10px 1px;
          }
          .alert {
            background: linear-gradient(to left,transparent 0%,transparent 50%,#FF9933 50%,#FF9933 100%);
            background-size: 10px 1px;
          }
          .standard {
            background: linear-gradient(to left,transparent 0%,transparent 50%,#59BF78 50%,#59BF78 100%);
            background-size: 10px 1px;
          }
        }
      }
    }
  }
</style>
