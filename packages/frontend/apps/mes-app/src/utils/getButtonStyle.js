import { getCustomStyle } from '@/utils/getCustomStyle.js'


export const getDefaultButtonStyles = (buttonParams = {}, textParams = {}) => {
	const customStyle = getCustomStyle('default', Object.assign({}, buttonParams))
	const customTextStyle = Object.assign({
		color: '#545659',
		fontSize: '14.07rpx'
	}, textParams)

	return {
		customStyle,
		customTextStyle,
	}
}
export const getParamyButtonStyles = (buttonParams = {}, textParams = {}) => {
	const customTextStyle = Object.assign({
		color: '#fff',
		fontSize: '14.07rpx'
	}, textParams)

	const customStyle = getCustomStyle('primary', Object.assign({}, buttonParams))

	return {
		customStyle,
		customTextStyle,
	}
}
export const getErrorButtonStyles = (buttonParams = {}, textParams = {}) => {
	const customTextStyle = Object.assign({
		color: '#FF4C26',
		fontSize: '14.07rpx'
	}, textParams)

	const customStyle = getCustomStyle('error', Object.assign({}, buttonParams))

	return {
		customStyle,
		customTextStyle,
	}
}
export const getErrorPrimaryButtonStyles = (buttonParams = {}, textParams = {}) => {
	const customTextStyle = Object.assign({
		color: '#FF4C26',
		fontSize: '14.07rpx'
	}, textParams)

	const customStyle = getCustomStyle('error', Object.assign({
		'background-color': '#FF4C26',
		'color': '#ffffff'
	}, buttonParams))

	return {
		customStyle,
		customTextStyle,
	}
}