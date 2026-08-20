
/**
 * 数值组件,趋势分析
 * @param {*} data 
 */
export const toTrendAnalysis = (data) => {
	const params = {
		...data
	};
	const query = Object.keys(params)
		.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
		.join('&');
    uni.navigateTo({
        url: `/pages/webviewComponent/TrendAnalysis/index?${query}`
    });
};
