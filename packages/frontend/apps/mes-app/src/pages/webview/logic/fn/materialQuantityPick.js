import {
	urlQueryRef,
	pageBasicDataRef
} from './webViewEventCallbacks.js';
export const materialQuantityPick = (data) => {
	console.log('materialQuantityPick', urlQueryRef.value, pageBasicDataRef.value);
	console.log('物料量领料-组件数据', data);
	const params = {
		...data.parent,
		processId: urlQueryRef.value?.processId,
		productPlanId: urlQueryRef.value?.productPlanId,
		procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId
	};
	const query = Object.keys(params)
		.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
		.join('&');
	uni.navigateTo({
		url: `/pages/businessComponents/materialPicking/index?${query}`
	});
};
