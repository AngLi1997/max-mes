<!-- 信息展示组件 -->
<template>
  <view class="info-display-container" :class="{ 'info-display-container-narrow': narrow }">
    <view v-if="isShowTitle" class="info-title-content">
      <slot name="title">
        <BMIcon :name="icon" size="18.75rpx" color="#2871FF" />
        <text class="title">
          {{ title }}
        </text>
      </slot>
    </view>
    <!-- 内容 -->
    <view class="info-content">
      <view
        v-for="item in basicItems"
        :key="item.field"
        class="info-item" :class="{ 'info-content-narrow': narrow, 'info-content-show-one': isShowOne }"
      >
        <text class="label">
          {{ item.label }}:
        </text>
        <wd-tag v-if="item.tag" :type="item.tag" :plain="item.plain ?? true">
          {{ getFiledValue(item) }}
        </wd-tag>
        <text v-else class="value" :style="item.valueStyle">
          {{ getFiledValue(item) }}
        </text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { BMIcon } from '@/BMComponents';

const props = defineProps({
  // 信息标题
  title: {
    type: String,
    default: '信息内容',
  },
  // 是否展示title
  isShowTitle: {
    type: Boolean,
    default: true,
  },
  // bmos-icon名
  icon: {
    type: String,
    default: 'caozuoshouce',
  },
  // 宽和窄两种样式类型,默认宽
  narrow: {
    type: Boolean,
    default: false,
  },
  // 背景色
  background: {
    type: String,
    default: '#FFFFFF',
  },
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
  // 是否一行展示一条数据
  isShowOne: {
    type: Boolean,
    default: false,
  },
});
const getFiledValue = (item) => {
  // 如果item.field是数组，说明是多个字段组合
  if (Array.isArray(item?.field)) {
    return item?.field.map(field => props.infoData[field] ?? '').join(item.hyphen || '-');
  }
  else {
    return props.infoData[(item?.field)] ?? '-';
  }
};
</script>

<style lang="scss" scoped>
.info-display-container {
  width: 100%;
  padding: 9.38rpx;
  border-radius: 4.69rpx;
  box-sizing: border-box;
  background: v-bind('background');

  .info-title-content {
    display: flex;
    align-items: center;
    line-height: 18.75rpx;

    .title {
      margin-left: 5.86rpx;
      font-size: 14.06rpx;
      color: var(--bmos-color-primary);
    }
  }

  .info-content {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;

    .info-item {
      display: flex;
      width: calc(50% - 9.38rpx);
      margin-top: 9.38rpx;
      font-size: 11.72rpx;

      .label {
        color: var(--bmos-color-text-sub);
        white-space: nowrap;
      }

      .value {
        color: var(--bmos-color-text-main);
        margin-left: 5.86rpx;
        overflow-wrap: break-word;
      }
      .wd-tag {
        margin-left: 5.86rpx;
      }
    }

    .info-content-narrow {
      width: 100%;
    }

    .info-content-show-one {
      width: 100%;
    }
  }
}

.info-display-container-narrow {
  width: 50%;
}
</style>
