import { buildUrlQuery } from '@/utils/url';

export const outputWeighing = async(data) => {
	try {
		const params = {
			componentId: data.parent.id,
            configInfo: data.parent.configInfo
		};
		const query = buildUrlQuery(params);
		uni.navigateTo({
			url: `/pages/businessComponents/outputWeighing/confirmOutputBatch/index?${query}`
		});
	} catch (error) {
		error.message && uni.showToast({
			title: error.message,
			icon: 'error',
			duration: 2000,
			mask: true
		});
	}
};
