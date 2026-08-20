<template>
	<view class="title">
		<text>{{props.name}}</text>
	</view>
</template>

<script setup>
	const props = defineProps({
		name: {
			type: String,
			default: ''
		}
	})
</script>

<style lang="scss" scoped>
	.title {
		padding: 11.72rpx 0 16.41rpx 24.03rpx;

		text {
			color: #242526;
			font-family: Source Han Sans CN;
			text-align: left;
			font-size: 14.07rpx;
			font-weight: 500;
			line-height: 14.07rpx;
			letter-spacing: 0em;
			text-align: left;
		}
	}
</style>