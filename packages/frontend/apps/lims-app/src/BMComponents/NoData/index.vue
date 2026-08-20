<template>
  <view class="no-data-container" :class="{ 'position-class': position }">
    <wd-img width="99.61rpx" height="90.31rpx" :src="url" />
    <text class="text">
      {{ text }}
    </text>
    <text v-if="subText" class="text">
      {{ subText }}
    </text>
  </view>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  type: {
    type: String,
    default: '',
  },
  text: {
    type: String,
    default: '',
  },
  subText: {
    type: String,
    default: '',
  },
  position: {
    type: Boolean,
    default: true,
  },
});
const url = computed(() => {
  return `/static/noData/${props.type}.png`;
});
</script>

<style lang="scss" scoped>
.no-data-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  .text {
    color: var(--bmos-color-text-sub);
    font-size: 11.72rpx;
    font-weight: 400;
    line-height: 23.44rpx;
  }
}
.position-class {
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -58.62rpx;
  margin-left: -58.62rpx;
}
</style>
