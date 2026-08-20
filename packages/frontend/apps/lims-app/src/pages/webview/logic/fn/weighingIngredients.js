import { buildUrlQuery } from '@/utils/url';

// 配料称量组件
export const weighingIngredients = async(data) => {
    const params = {
		componentId: data.parent.id,
        configInfo: data.parent.configInfo
	};
    const query = buildUrlQuery(params);
	uni.navigateTo({
		url: `/pages/businessComponents/weighingIngredients/index?${query}`
	});
};
