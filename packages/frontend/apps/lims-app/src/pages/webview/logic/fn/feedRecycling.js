import { urlQueryRef, pageBasicDataRef } from './webViewEventCallbacks.js';
import { H5AppNavigateBack, showTakePhotoPopupRef } from '@/pages/webview/utils/index.js';
export const feedRecycling = (data) => {
	console.log('feedRecycling', urlQueryRef.value, pageBasicDataRef.value);
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
		url: `/pages/businessComponents/feedRecycling/index?${query}`
	});
};
