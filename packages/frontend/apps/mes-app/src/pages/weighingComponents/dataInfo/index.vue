<!-- 数据信息展示组件 -->
<template>
  <view class="dataInfo-display-container">
    <template v-for="item in basicItems" :key="item.field">
      <view v-if="item.type === 'text'" class="dataInfo-content">
        <view class="bmos-ellipsis-1">
          {{ item.label }}
        </view>
        <view>
          <text class="dataInfo-title bmos-ellipsis-1" :style="{ color: item.color || '' }">
            {{ getFiledValue(item) }}
          </text>
        </view>
      </view>
      <view v-else-if="item.type === 'button'" class="button">
        <wd-button type="text" @click="item.click">
          {{ item.label }}
        </wd-button>
      </view>
    </template>
  </view>
</template>

<script setup>
const props = defineProps({
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
    return item?.field.map(field => props.infoData[field] ?? '').join(item.hyphen || '-');
  }
  else {
    return props.infoData[(item?.field)] ?? '-';
  }
};
</script>

<style lang="scss" scoped>
.dataInfo-display-container {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 7.03rpx 0rpx;
  box-sizing: border-box;
  .dataInfo-content {
    flex: 1;
    padding: 0 9.38rpx;
    view:nth-child(1) {
      margin-bottom: 4.69rpx;
      font-size: 10.55rpx;
      color: var(--bmos-color-text-sub);
    }
    view:nth-child(2) {
      display: flex;
      align-items: center;
      .dataInfo-title {
        font-size: 12.89rpx;
        color: var(--bmos-color-text-main);
      }
    }
  }
  .dataInfo-content:not(:last-child) {
    border-right: 1px solid var(--bmos-color-border);
    margin-right: 14.06rpx;
  }
  .button {
    min-width: 35.16rpx;
    max-width: 117.19rpx;
    margin-right: 9.38rpx;
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
</style>
