<!-- 信息展示组件 -->
<template>
  <view class="info-display-container">
    <!-- 内容 -->
    <view class="info-content">
      <slot name="icon">
        <view class="info-icon">
          <wd-icon
            v-if="isShowIcon"
            class-prefix="bmos-app-icon"
            :name="icon"
            size="14.07rpx"
            color="#2871FF"
          />
        </view>
      </slot>
      <view v-if="title" class="info-title">
        {{ title }}
      </view>
      <template v-for="item in basicItems" :key="item.field">
        <view v-if="item.type === 'text'" class="text-item">
          <text class="bmos-ellipsis-1">
            {{ item.label }}
          </text>：<text class="bmos-ellipsis-1">
            {{ getFiledValue(item) }}
          </text>
        </view>
        <view v-else-if="item.type === 'button'" class="button">
          <wd-button type="text" @click="item.click">
            {{ item.label }}
          </wd-button>
        </view>
      </template>
    </view>
  </view>
</template>

<script setup>
const props = defineProps({
  // 是否展示icon
  isShowIcon: {
    type: Boolean,
    default: true,
  },
  // bmos-icon名
  icon: {
    type: String,
    default: 'xinxi',
  },
  // 标题
  title: {
    type: String,
    default: '',
  },
  // 背景色
  background: {
    type: String,
    default: '#F2F7FF',
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
});
const getFiledValue = (item) => {
  // 如果item.field是数组，说明是多个字段组合
  if (Array.isArray(item?.field)) {
    return item?.field.map(field => props.infoData[field] || '').join(item.hyphen || '-');
  }
  else {
    return props.infoData[(item?.field)] ?? '-';
  }
};
</script>

<style lang="scss" scoped>
.info-display-container {
  width: 100%;
  padding: 7.03rpx 9.38rpx;
  border-radius: 4.69rpx;
  box-sizing: border-box;
  background: v-bind('background');
  .info-content {
    display: flex;
    align-items: center;
    height: 18.75rpx;
    .info-icon {
      width: 18.75rpx;
      height: 18.75rpx;
      background-color: #d9e5ff;
      border-radius: 4.69rpx;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .info-title {
      font-size: 12.89rpx;
      color: var(--bmos-color-text-main);
      margin-left: 4.69rpx;
      margin-right: 9.38rpx;
    }
    .text-item {
      display: flex;
      align-items: center;
      flex: 1;
      font-size: 11.72rpx;
      line-height: 14.06rpx;
      white-space: nowrap;
      margin-left: 11.72rpx;
      color: var(--bmos-color-text-sub);
      text:nth-child(1) {
        white-space: nowrap;
        display: block;
        max-width: 93.75rpx;
        flex-shrink: 0;
      }
      text:nth-child(2) {
        margin-left: 4.69rpx;
        color: var(--bmos-color-text-main);
        overflow-wrap: break-word;
      }
    }
    .button {
      min-width: 35.16rpx;
      max-width: 117.19rpx;
      flex-shrink: 0;
      margin-left: 18.75rpx;
      :deep(.wd-button) {
        width: 100%;
        .wd-button__text {
          display: block;
          overflow: hidden;
          text-overflow: ellipsis;
          /* 限制在一个块元素显示的文本的行数 */
          /* -webkit-line-clamp 其实是一个不规范属性，使用了WebKit的CSS扩展属性，该方法适用于WebKit浏览器及移动端；*/
          -webkit-line-clamp: 1;
          /* 设置或检索伸缩盒对象的子元素的排列方式 */
          -webkit-box-orient: vertical;
        }
      }
    }
  }
}
</style>
