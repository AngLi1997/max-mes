export const productOutput = async(data) => {
	const params = {
		...data.parent,
		curFieldId: data.fieldId
	};
	try {
		const query = Object.keys(params)
		.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
		.join('&');
	uni.navigateTo({
		url: `/pages/businessComponents/productOutput/index?${query}`
	});
	} catch (error) {
		//
	}
};
