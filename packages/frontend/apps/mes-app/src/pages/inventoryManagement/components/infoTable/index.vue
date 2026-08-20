<template>
  <view>
    <view class="row_title">{{ title }}</view>
    <view class="row-box">
      <view class="row-col">
        <view v-for="(item, index) in details" :key="index" class="col-tr">
          <view class="col_tr_item title">{{ item.title }}</view>
          <view
            class="col_tr_item text"
            :style="{ color: item.color || '#242526' }"
          >
            {{ getText(item) }}
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
  const props = defineProps({
    details: {
      type: Array,
      default: () => []
    },
    data: {
      type: Object,
      default: () => {}
    },
    title: {
      type: String,
      default: ''
    }
  });

  const getText = (item) => {
    return item.isUnit
      ? `${props.data[item.dataIndex]} ${props.data[item.unitName]}`
      : props.data[item.dataIndex];
  };
</script>

<style lang="scss" scoped>
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
    height: 37.5rpx;
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
