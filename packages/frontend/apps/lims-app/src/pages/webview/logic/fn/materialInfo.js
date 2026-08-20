export const materialInfo = async(data, isUpdate = 1) => {
	const params = {
		...data.parent,
    curFieldId: data.fieldId,
    isUpdate // 1: 新增 2: 修改
	};
	const query = Object.keys(params)
		.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
		.join('&');
		uni.navigateTo({
			url: `/pages/businessComponents/materialInfo/index?${query}`
		});
};
