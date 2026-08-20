<template>
	<view class="nav-bar-container" :class="{'nav-bar-container-fixed':fixed, 'border-bottom':borderBottom}">
		<view class="left-content" @click="$emit('leftClick')">
			<slot name="left">
			</slot>
		</view>
		<view class="center-content">
			<slot name="center"></slot>
		</view>
		<view class="right-content" @click="$emit('rightClick')">
			<slot name="right"></slot>
		</view>
	</view>
</template>
<script setup>
	import { toRefs } from "vue"
	const props = defineProps({
		background: {
			type: String,
			default: '#FFFFFF'
		},
		fixed: {
			type: Boolean,
			default: true
		},
		borderBottom: {
			type: Boolean,
			default: true
		}
	})
	const { background, fixed, borderBottom } = toRefs(props)
</script>

<style lang="scss" scoped>
	.nav-bar-container {
		display: flex;
		width: 100%;
		height: 46.89rpx;
		padding: 12.31rpx 9.38rpx 12.31rpx 9.38rpx;
		box-sizing: border-box;
		justify-content: space-between;
		align-items: center;
		background: v-bind('background');


		&-fixed {
			position: fixed;
			top: 0;
			left: 0;
			z-index: 99;
		}
	}

	.border-bottom {
		border: 1px solid #E1E3E5;
	}
</style>