const styleMaps = new Map([
	['primary', {
		'border-radius': '4.69rpx',
		'background-color': '#2871FF',
		'font-size': '12.9rpx',
		'font-weight': 400,
		'text-align': 'center',
		color: '#FFFFFF',
	}],
	['default', {
		color: '#545659',
		border: "1px solid #E1E3E5",
		'border-radius': '4.69rpx',
		'background-color': '#FFFFFF',
		'font-size': '12.9rpx',
		'font-weight': 400,
	}],
	['error', {
		color: '#FF4C26',
		border: "1px solid #FF4C26",
		'border-radius': '4.69rpx',
		'background-color': '#FFFFFF',
		'font-size': '12.9rpx',
		'font-weight': 400,
	}]
])

export function getCustomStyle(key, params = {}) {
	const defalutStyle = styleMaps.get(key) || styleMaps.get('default') || {}
	return Object.assign({}, defalutStyle, params)
}