<template>
  <view class="card-mode" @click="emit('onClick', useItem)">
    <view class="card-title">
      <wd-text :text="useItem?.name" :lines="1" size="14.07rpx" line-height="16.41rpx" color="#242526" />
      <wd-tag :type="stateColor[useItem?.status?.value]" plain>
        {{ useItem?.statusName }}
      </wd-tag>
    </view>
    <view
      v-for="(item, index) in useItem?.useType"
      :key="index"
      class="mode-box"
    >
      {{ item?.title }}:
      <text class="uni-body">
        {{ item?.value || "-" }}
      </text>
    </view>
  </view>
</template>

<script setup>
import { reactive } from 'vue';

defineProps({
  useItem: {
    type: Object,
    default: () => {
      return {
        name: 'xxx',
        status: 1,
        statusName: 'xxx',
        useType: [
          {
            title: 'xxx',
            value: 'xxxx',
          },
          {
            title: '有效期至',
            value: '2024-05-12',
          },
        ],
      };
    },
  },
});
const emit = defineEmits(['onClick']);
const stateColor = reactive({
  1: 'primary',
  2: 'warning',
  3: 'success',
  4: 'danger',
});
</script>

<style lang="scss" scoped>
.card-mode {
  background-color: #fff;
  padding: 9.38rpx 11.72rpx;
  border-radius: 4.69rpx;
  width: 237.3rpx;
  height: 82.03rpx;
  box-sizing: border-box;
  .card-title {
    display: flex;
    justify-content: space-between;
  }
  .mode-box {
    padding-top: 7.03rpx;
    color: var(--bmos-color-text-sub);
    font-size: 11.72rpx;
  }
}
</style>
