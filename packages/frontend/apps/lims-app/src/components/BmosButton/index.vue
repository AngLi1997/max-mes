<template>
	<uv-button v-bind="$attrs" :customStyle="customStyle" :customTextStyle="customTextStyle">
		{{text}}
		<uv-icon v-if="icon" class="svg-class" :name="iconObj[icon]" custom-prefix="bmos-icon" size="11.72rpx"
			:color="color"></uv-icon>
	</uv-button>
</template>

<script setup>
	import { useAttrs } from 'vue';
	import { getDefaultButtonStyles, getParamyButtonStyles, getErrorButtonStyles,
		getErrorPrimaryButtonStyles } from '@/utils/getButtonStyle.js'

	const fnMap = new Map([
		['default', getDefaultButtonStyles],
		['primary', getParamyButtonStyles],
		['error', getErrorButtonStyles],
		['errorPrimary', getErrorPrimaryButtonStyles]
	])
	const props = defineProps({
		text: {
			type: String,
			default: ''
		},
		type: {
			type: String,
			default: 'default'
		},
		height: {
			type: String,
			default: '42.2rpx'
		},
		size: {
			type: String,
			default: ''
		},
		icon: {
			type: String,
			default: ''
		},
		color: {
			type: String,
			default: '#797C80'
		}
	})
	const sizeObj = {
		small: '32.83rpx',
	}
	const iconObj = {
		sort: 'paixu',
		filter: 'shaixuan'
	}
	const height = props.size ? sizeObj[props.size] : props.height
	const {
		customStyle,
		customTextStyle
	} = fnMap.get(props.type)({ height })
</script>

<style lang="scss" scoped>
	.svg-class {
		margin-left: 4.69rpx;
	}
</style>