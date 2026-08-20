<template>
  <view class="info-table">
    <view v-if="props.title" class="row_title">
      {{ props.title }}
    </view>
    <view class="row-box">
      <view class="row-col">
        <view v-for="(item, index) in props.details" :key="index" class="col-tr">
          <view class="col_tr_item title">
            {{ item?.title }}
          </view>
          <view
            class="col_tr_item text"
            :style="item?.styleFunc ? item.styleFunc(props.data) : { color: item?.color || '#242526' }"
          >
            {{ props.data?.[item?.dataIndex] }}
          </view>
        </view>
        <view v-if="props.details.length % 2 !== 0" class="col-tr">
          <view class="col_tr_item" style="width: 100%" />
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app';

const props = defineProps({
  details: { // 配置
    type: Array,
    default: () => [
      {
        title: '', // 标题
        dataIndex: '', // 数据字段
        color: '#242526', // 颜色
        styleFunc: (_data) => { // 自定义样式
          return {
            color: '#242526',
          };
        },
      },
    ],
  },
  data: { // 数据源
    type: Object,
    default: () => {},
  },
  title: { // 大标题
    type: String,
    default: '',
  },
});

onLoad(() => {
  // console.log('nfo-table',props);
});
</script>

<style lang="scss" scoped>
.info-table {
  width: 100%;
  background-color: white;
}
.row_title {
  font-size: 14.06rpx;
  margin-bottom: 9.38rpx;
  color: var(--bmos-color-text-main);
}
.row-box {
  width: 100%;
  height: calc(100% - 26.37rpx);
  overflow: auto;
}
.row-col {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  border-right: 1px solid var(--bmos-color-border);
  border-bottom: 1px solid var(--bmos-color-border);
  box-sizing: border-box;
  .col-tr {
    width: 50%;
    min-height: 37.5rpx;
    box-sizing: border-box;
    display: flex;
    .col_tr_item {
      box-sizing: border-box;
      padding: 11.72rpx;
      font-size: 11.72rpx;
      border-left: 1px solid var(--bmos-color-border);
      border-top: 1px solid var(--bmos-color-border);
    }
    .title {
      width: 40%;
      background-color: #f5f6f7;
      box-sizing: border-box;
      color: var(--bmos-color-text-sub);
    }
    .text {
      width: 60%;
      box-sizing: border-box;
    }
  }
}
</style>
