<template>
	<view class="section-container">
		<view class="line">
		</view>
		<view class="content">
			<text>{{title}}</text>
		</view>
	</view>
</template>

<script setup>
	import { toRefs } from "vue";
	const props = defineProps({
		title: {
			type: String,
			default: ''
		}
	})
	const { title } = toRefs(props)
</script>

<style scoped lang="scss">
	.section-container {
		width: 100%;
		height: 32.83rpx;
		display: flex;
		align-items: center;
		.line {
			width: 3.52rpx;
			height: 11.72rpx;
			background: rgba(40, 113, 255, 1);
			border-radius: 4.69rpx;
			margin-right: 4.69rpx;
		}

		.content {
			font-size: 12.9rpx;
			font-weight: 400;
			color: rgba(24, 25, 26, 1);
		}
	}
</style>