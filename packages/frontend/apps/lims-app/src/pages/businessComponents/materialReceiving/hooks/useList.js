import {
	getReceiveRepositoryMaterialBatch,
	postRequisitionRequisitionReceiveComplete
} from '@/api';
import {
	ref
} from 'vue';
import {
	initFillData2
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
export const useList = ({
	UseParams
}) => {
	const {
		seg,
		paramsData
	} = UseParams;
	const needRefresh = ref(false);
	// 多选数据
	const checkboxValue = ref([]);
	// 接收数据
	const formData = ref([]);
	// 物料接收列表 Api
	const receiveListApi = async() => {
		try {
			const apiParams = {
				requisitionPlanId: seg.ordeId
			};
			const res = await getReceiveRepositoryMaterialBatch(apiParams);
			formData.value = res.data;
		handleSelect(formData.value);
		} catch (error) {
			// TODO handle the exception
			error.message && uni.showToast({
				title: error.message,
				icon: 'none',
				duration: 2000
			});
		}
	};
	// 完成
	const complete = async() => {
		try {
			const apiParams = {
				requisitionPlanId: seg.ordeId
			};
			await postRequisitionRequisitionReceiveComplete(apiParams);
			uni.navigateBack();
			initFillData2();
		} catch (error) {
			// TODO handle the exception
			error.message && uni.showToast({
				title: error.message,
				icon: 'none',
				duration: 2000
			});
		}
	};
	const groupChange = (detail) => {
		const model = checkboxValue.value?.map((item) => item.id)?.includes(detail.id);
		if (model) {
			checkboxValue.value = checkboxValue.value.filter((item) => {
				return item.id !== detail.id;
			});
		} else {
			checkboxValue.value.push(detail);
		}
		handleSelect(formData.value);
	};
	// 处理选中加样式
	const handleSelect = (data) => {
		const temp = checkboxValue.value?.map((item) => item.id);
		data.forEach((item) => {
			if (temp.includes(item.id)) {
				item.select = true;
			} else {
				item.select = false;
			}
		});
	};
	const detailsMap = (detail) => {
		const man = {
			...detail,
			requisitionId: seg.ordeId,
			componentId: paramsData.value?.id
		};
		needRefresh.value = true;
		const query = Object.keys(man)
			.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(man[key])}`)
			.join('&');
		uni.navigateTo({
			url: `/pages/businessComponents/materialDetails/index?${query}`
		});
	};
	const estOute = (detail) => {
		if (detail) {
			receiveListApi();
		}
	};
	return {
		needRefresh,
		checkboxValue,
		formData,
		groupChange,
		detailsMap,
		receiveListApi,
		complete,
		estOute
	};
};
