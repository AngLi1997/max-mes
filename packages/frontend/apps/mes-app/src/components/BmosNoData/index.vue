<template>
  <view class="no-data-container" :class="{'position-class': position}">
    <uv-image :src="url" width="117.23rpx" height="117.23rpx" />
    <text class="text">{{ text }}</text>
    <text v-if="subText" class="text">{{ subText }}</text>
  </view>
</template>

<script setup>
  import { computed } from 'vue';
  const props = defineProps({
    type: {
      type: String,
      default: ''
    },
    text: {
      type: String,
      default: ''
    },
    subText: {
      type: String,
      default: ''
    },
    position: {
      type: Boolean,
      default: true
    }
  });
  const imageUrlObj = {
    'emptyProductionBefore': 'emptyProductionBefore',
    'emptyRelatedBatches': 'emptyRelatedBatches',
    'emptyTodo': 'emptyTodo',
    'emptyWorkbench': 'emptyWorkbench',
    'emptyPhoto': 'emptyPhoto',
    'emptyHistoryBatches': 'emptyHistoryBatches'
  };
  const url = computed(() => {
    return `/static/noData/${imageUrlObj[props.type]}.png`;
  });
</script>

<style lang="scss" scoped>
	.no-data-container {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;

		.text {
			color: #545659;
			font-size: 11.72rpx;
			font-weight: 400;
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
