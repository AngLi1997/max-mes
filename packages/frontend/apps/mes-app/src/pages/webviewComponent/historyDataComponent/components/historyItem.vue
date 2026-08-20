<template>
  <view class="historical-item">
    <view style="display: flex;align-items: center;">
      <text class="label">
        {{ t("值") }}:
      </text>
      <text v-if="isHandleSignComponent && item.emptyValue" class="value">
        {{
          item.value
        }}
      </text>
      <img
        v-else-if="isHandleSignComponent && item.value"
        class="handle-sign-img"
        :src="getHandleSignSrc(item.value)"
      >
      <wd-button v-else-if="componentData.componentType === 'EQUIPMENT_DATA_DRAW_LIST'" type="text" @click="openImgDetail(item)">
        {{ t('查看详情') }}
      </wd-button>
      <text v-else class="value">
        {{
          item.value && item.value.replace(/\\\"/g, '"')
        }}
      </text>
    </view>
    <view style="display: flex">
      <text class="label">
        {{ t("操作") }}:
      </text>
      <text class="value">
        {{
          item.systemCreate ? t("更新") : operationTypeObj[item.operationType]
        }}
      </text>
    </view>
    <view style="display: flex">
      <text class="label">
        {{ t("操作人") }}:
      </text>
      <text class="value">
        {{
          item.systemCreate ? t("系统") : item.operationUsername
        }}
      </text>
    </view>
    <view v-if="item.reviewUsername" style="display: flex">
      <text class="label">
        {{ t("复核人") }}:
      </text>
      <text class="value">
        {{ item.reviewUsername }}
      </text>
    </view>
    <view style="display: flex">
      <text class="label">
        {{ t("时间") }}:
      </text>
      <text class="value">
        {{ item.operationTime }}
      </text>
    </view>
    <view style="display: flex">
      <text class="label">
        {{ t("备注") }}:
      </text>
      <text class="value">
        {{ item.remark }}
      </text>
    </view>
  </view>
</template>

<script setup>
import { IP_CONFIG } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed } from 'vue';

const props = defineProps({
  item: {
    type: Object,
    default: () => {
      return {};
    },
  },
  componentData: {
    type: Object,
    default: () => {
      return {};
    },
  },
});
const operationTypeObj = {
  save: t('录入'),
  modify: t('修订'),
  systemCreate: t('更新'),
  update: t('更新'),
};
const getHandleSignSrc = (value) => {
  const baseUrl = `http://${getStorageSync(IP_CONFIG) || '172.30.1.160:80'}`;
  return `${baseUrl}/${value.replace(/\\"/g, '"')}`;
};

const isHandleSignComponent = computed(() => {
  return ['HANDLE_SUBMIT_SIGN', 'HANDLE_REVIEW_SIGN'].includes(props.componentData.componentType);
});
const openImgDetail = (item) => {
  const params = {
    value: item.value,
  };
  const query = Object.keys(params)
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
    .join('&');
  uni.navigateTo({
    url: `/pages/businessComponents/equipmentDataDrawDetail/index?${query}`,
  });
};
</script>

<style lang="scss" scoped>
.historical-item {
  width: 100%;
  min-height: 109.03rpx;
  box-sizing: border-box;
  padding: 4.69rpx 9.38rpx;
  border-radius: 5.86rpx;
  background: #f5f6f7;
  font-size: 11.72rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;

  :deep(.wd-button) {
    padding: 0;
    margin: 0;
    height: 11.72rpx;
    line-height: 11.72rpx;
    border-radius: 0 !important;
  }
  .label {
    color: #545659;
    margin-right: 9.38rpx;
    width: 46.89rpx;
    display: inline-block;
    flex-shrink: 0;
  }

  .value {
    color: #242526;
  }
  .handle-sign-img {
    height: 23.44rpx;
    object-fit: contain;
  }
}
</style>
