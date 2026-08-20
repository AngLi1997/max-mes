<!-- 数据信息展示组件 -->
<template>
  <view class="dataInfo-display-container">
    <view
      v-for="item in basicItems"
      :key="item.field"
      class="dataInfo-content"
    >
      <view>
        {{ item.label }}
      </view>
      <view>
        <text
          class="dataInfo-title"
          :style="{ color: item.color || '' }"
        >
          {{ infoData[item?.field]?.value ?? "-" }}
        </text>
        <wd-tag
          v-if="infoData[item?.field]?.waring"
          custom-class="space"
          type="warning"
        >
          <text style="color: white">
            {{ t('未满足') }}
          </text>
        </wd-tag>
        <wd-tag
          v-if="infoData[item?.field]?.success"
          custom-class="space"
          type="success"
        >
          <text style="color: white">
            {{ t('已满足') }}
          </text>
        </wd-tag>
      </view>
    </view>
  </view>
</template>

<script setup>
import { t } from '@/utils/useBmosI18n.js';

defineProps({
  // 每个内容的描述标题
  basicItems: {
    type: Array,
    default: () => [],
  },
  // 对应内容
  infoData: {
    type: Object,
    default: () => {},
  },
  // 背景色
  background: {
    type: String,
    default: '#f2f7ff',
  },
});
</script>

<style lang="scss" scoped>
  .dataInfo-display-container {
  display: flex;
  width: 100%;
  padding: 7.03rpx 0rpx;
  border-radius: 4.69rpx;
  box-sizing: border-box;
  background: v-bind('background');

  .dataInfo-content {
    flex: 1;
    padding-left: 9.38rpx;
    view:nth-child(1) {
      margin-bottom: 4.69rpx;
      font-size: 10.55rpx;
      color: var(--bmos-color-text-sub);
      overflow-wrap: break-word;
      text-overflow: -o-ellipsis-lastline;
      // 超过两行用…
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      line-clamp: 2;
      -webkit-box-orient: vertical;
    }
    view:nth-child(2) {
      display: flex;
      align-items: center;
      .dataInfo-title {
        margin-right: 11.72rpx;
        font-size: 11.72rpx;
        color: var(--bmos-color-text-main);
      }
    }
  }
  .dataInfo-content:not(:last-child) {
    border-right: 1px solid var(--bmos-color-border);
    margin-right: 18.75rpx;
  }
}
</style>
