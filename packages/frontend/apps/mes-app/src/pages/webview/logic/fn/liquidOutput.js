// 配液产出
export const liquidOutput = async(data) => {
    const params = {
		componentId: data.parent.id
	};
	const query = Object.keys(params)
		.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
		.join('&');
	uni.navigateTo({
		url: `/pages/businessComponents/liquidOutput/index?${query}`
	});
};
