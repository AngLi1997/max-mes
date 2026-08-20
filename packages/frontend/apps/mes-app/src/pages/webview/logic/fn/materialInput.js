import { urlQueryRef, pageBasicDataRef } from './webViewEventCallbacks.js';
export const materialInput = (data) => {
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
		url: `/pages/businessComponents/materialInput/index?${query}`
	});
};
