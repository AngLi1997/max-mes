<template>
  <view class="line_progress">
    <view class="line_box">
      <uv-line-progress
        :show-text="false"
        :percentage="percentage"
        :active-color="activeColor"
      />
      <view class="range_box">
        <view
          v-if="weighingRange[0] !== 0 && weighingRange[0] !== null && weighingRange[0] !== '0'"
          class="range_item"
          :style="{ left: `${leftNum0}%` }"
        >
          <view class="range_item_line min_line" />
          <view class="range_item_num">{{ weighingRange[0] }}</view>
        </view>
        <view class="range_item" :style="{ left: `${leftNum1}%` }">
          <view class="range_item_line target_line" />
          <view class="range_item_num">{{ weighingRange[1] }}</view>
        </view>
        <view
          v-if="weighingRange[2] !== 0 && weighingRange[2] !== null && weighingRange[2] !== '0'"
          class="range_item"
          :style="{ left: `${leftNum2}%` }"
        >
          <view class="range_item_line max_line" />
          <view class="range_item_num">{{ weighingRange[2] }}</view>
        </view>
      </view>
    </view>
  </view>
</template>
<script setup>
  import { computed, ref, watch } from 'vue';
  const props = defineProps({
    // 当前值
    weight: {
      type: Number,
      default: 0
    },
    detailData: {
      type: Object,
      default: () => ({})
    }
  });

  const percentage = ref(100); // 进度条百分比
  const activeColor = computed(() => {
    if (Number(props.weight) > Number(maxRange.value)) {
      return '#FF4C26';
    } else if (Number(minRange.value) > Number(props.weight)) {
      return '#FF9933';
    } else {
      return '#59BF78';
    }
  });
  // 称量类型 配料称量、余料称量
  const weighingType = computed(() => {
    return props.detailData?.weighProcess?.value === 1
      ? 'toleranceDiff'
      : 'oddToleranceDiff';
  });
  // 称量范围
  const weighingRange = computed(() => {
    return props.detailData[weighingType.value];
  });
  
  // 误差最小值
  const minRange = computed(() => {
    return weighingRange.value[0] || weighingRange.value[1];
  });
  // 误差最大值
  const maxRange = computed(() => {
    return weighingRange.value[2] || weighingRange.value[1];
  });
  // 目标值
  const targetRange = computed(() => {
    return weighingRange.value[1];
  });

  // 进度条最大值
  const progressMax = computed(() => {
    return Number(maxRange.value) * 1.2;
  });

  const leftNum0 = computed(() => {
    if (weighingRange.value[0]) {
      return Number(weighingRange.value[0]) / Number(progressMax.value) * 100;
    }
    return '0';
  });

  const leftNum1 = computed(() => {
    if (weighingRange.value[1]) {
      return Number(weighingRange.value[1]) / Number(progressMax.value) * 100;
    }
    return '0';
  });

  const leftNum2 = computed(() => {
    if (weighingRange.value[2]) {
      return Number(weighingRange.value[2]) / Number(progressMax.value) * 100;
    }
    return '0';
  });

  watch(
    () => props.weight,
    () => {
      if (Number(props.weight) > Number(progressMax.value)) {
        percentage.value = 100;
        return;
      } else if (Number(props.weight) < 0) {
        percentage.value = 0;
      } else {
        percentage.value = Number(props.weight) / Number(progressMax.value) * 100;
      }
    },
    { immediate: true }
  );
  defineExpose({
    maxRange
  });
</script>
<style lang="scss" scoped>
.line_progress {
  width: 100%;
  .range_box {
    display: flex;
    position: relative;
    height: 22.27rpx;
    .range_item {
      position: absolute;
      width: 60rpx;
      text-align: center;
      margin-left: -30rpx;
      font-size: 10.55rpx;
      .range_item_line {
        width: 1rpx;
        height: 5rpx;
        background-color: #bbbdbf;
        margin: auto;
      }
    }
  }
}
:deep(.uv-line-progress__line) {
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
}
</style>
