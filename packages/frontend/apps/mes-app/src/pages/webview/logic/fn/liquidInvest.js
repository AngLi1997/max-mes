export const liquidInvest = async(data, isUpdate = 1) => {
	const params = {
		...data.parent,
		curFieldId: data.fieldId,
		isUpdate
	};
	console.log('===============', data);
	
	const query = Object.keys(params)
		.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
		.join('&');
	uni.navigateTo({
		url: `/pages/businessComponents/liquidInvest/index?${query}`
	});
};
