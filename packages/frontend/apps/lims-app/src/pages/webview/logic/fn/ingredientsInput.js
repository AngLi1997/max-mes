export const ingredientsInput = async(data, isUpdate = 1) => {
	const params = {
		...data.parent,
		curFieldId: data.fieldId,
		isUpdate
	};
	const query = Object.keys(params)
		.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
		.join('&');
	uni.navigateTo({
		url: `/pages/businessComponents/ingredientsInput/index?${query}`
	});
};
