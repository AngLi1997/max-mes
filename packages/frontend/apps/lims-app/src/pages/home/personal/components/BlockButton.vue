<template>
  <view class="button-box">
    <view class="content" :class="{'content-bottom-line':bottomBorder}" @click="emit('row-click')">
      <text class="label" @click.stop="(e)=>emit('left-click',e)">{{ label }}</text>
      <view class="right-text" @click="emit('click')">
        {{ rightText }}
				&nbsp;&nbsp;
        <uv-icon color="#797C80" name="jiantou-you" size="14.07rpx" custom-prefix="bmos-icon" />
      </view>
    </view>
  </view>
</template>

<script setup>
  const props = defineProps({
    label: {
      type: String,
      default: ''
    },
    rightText: {
      type: String,
      default: ''
    },
    bottomBorder: {
      type: Boolean,
      default: true
    }
  });
  const emit = defineEmits(['click', 'left-click', 'row-click']);
</script>

<style lang="scss" scoped>
	.button-box {
		width: 100%;
		height: 46.89rpx;
		border-radius: 4.69rpx;
		background: #ffffff;
		padding: 0 9.38rpx;
		box-sizing: border-box;

		.content {
			display: flex;
			justify-content: space-between;
			align-items: center;
			height: 100%;
			box-sizing: border-box;

			.label {
				color: #242526;
				font-size: 12.9rpx;
			}

			.right-text {
				min-width: 58.62rpx;
				font-size: 10.55rpx;
				color: #909398;
				display: flex;
				justify-content: flex-end;
				align-items: center;
			}
		}

		.content-bottom-line {
			border-bottom: 1px solid #E1E3E5;
		}
	}
</style>
