import {
	urlQueryRef,
	pageBasicDataRef
} from './webViewEventCallbacks.js';
export const batchQuantityPick = (data) => {
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
		url: `/pages/businessComponents/materialPlan/index?${query}`
	});
};
