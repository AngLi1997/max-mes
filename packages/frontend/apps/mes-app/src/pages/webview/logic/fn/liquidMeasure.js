// 配液量取
export const liquidMeasure = async(data) => {
    const params = {
		componentId: data.parent.id
	};
	const query = Object.keys(params)
		.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
		.join('&');
	uni.navigateTo({
		url: `/pages/businessComponents/liquidMeasure/index?${query}`
	});
};
