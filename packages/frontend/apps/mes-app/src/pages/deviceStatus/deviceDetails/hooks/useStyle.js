import {
	computed
} from "vue"
export const useStyle = () => {
	//标题样式
	const listStyle = computed(() => {
		return {
			backgroundColor: 'transparent'
		}
	});
	//按钮样式
	const buttonStyle = computed(() => {
		return {
			display: 'flex',
			padding: '7.03rpx 14.07rpx',
			justifyContent: 'center',
			alignItems: 'center',
		}
	})
	//按钮字体样式
	const buttonTextStyle = computed(() => {
		return {
			fontSize: '11.72rpx',
			fontStyle: 'normal',
			fontWeight: 513,
		}
	})
	//圆角
	const listCustomStyle = computed(() => {
		return {
			borderRadius:'4.69rpx'
		}
	})
	return {
		listStyle,
		buttonStyle,
		buttonTextStyle,
		listCustomStyle
	}
}