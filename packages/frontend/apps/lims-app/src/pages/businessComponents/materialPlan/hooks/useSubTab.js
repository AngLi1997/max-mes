import {
	requisitionDetailApi
} from '@/api';
import {
	t
} from '@/utils/useBmosI18n.js';
import {
	getCurrentCopyRecordItem
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import {
	ref,
	nextTick
} from 'vue';

export const useSubTab = ({
	UseTable,
	UseColumns,
	showNotify
}) => {
	const {
		getPage
	} = UseTable;
	const {
		loading,
		current,
		paramsData,
		pendingQuantity,
		tabSub,
		refreshPage,
		completedPlan
	} = UseColumns;
	const splitSigning = ref([]);
	const change = ({ value }) => {
		const data = splitSigning.value?.find(item => item.id === value);
		if (data) {
			current.active = data.id;
			current.currentList = {
				...data,
				name: current.currentList?.name,
				// componentId:current.currentList?.componentId,
				requisitionPlanId: current.currentList?.requisitionPlanId,
				processId: paramsData.value?.processId,
				productPlanId: current.currentList?.productPlanId
			};
			getPage();
		}
	};
	const tabChange = (val) => {
		tabSub.index = val;
		getPage();
		confirm();
	};

	// 跳转处理界面
	const toMaterial = () => {
		refreshPage.value = true;
		const params = { ...current.currentList, pendingQuantityNum: pendingQuantity.value };
		const query = Object.keys(params)
			.map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
			.join('&');
		switch (tabSub.index) {
			case 0:
				uni.navigateTo({
					url: `/pages/businessComponents/lockMaterial/index?${query}`
				});
				break;
			case 1:
				if (completedPlan.value) return showNotify({
					type: 'success',
					message: t('领料计划已完成')
				});
				uni.navigateTo({
					url: `/pages/businessComponents/addMaterials/index?${query}`
				});
				break;
		}
	};
	const confirm = () => {
		loading.value = true;
		nextTick(() => {
			loading.value = false;
		}, 1000);
	};
	// 领料api
	const reqDetailApi = async() => {
		try {
			const {
				version
			} = getCurrentCopyRecordItem();
			const res = await requisitionDetailApi({
				...paramsData.value,
				componentId: paramsData.value?.id,
				copyVersion: version
			});
			splitSigning.value = res.data?.materialList;
			current.currentList = {
				...res.data?.materialList[0],
				name: res.data.name,
				// componentId: paramsData.value?.id,
				requisitionPlanId: res.data.id,
				processId: paramsData.value?.processId,
				productPlanId: res.data?.productPlanId
			};
			current.active = res.data?.materialList[0]?.id;
			completedPlan.value = res.data.completedPlan || false;
			await getPage();
		} catch (error) {
			error.message && showNotify({
				type: 'danger',
				message: error.message
			});
		}
	};
	return {
		current,
		tabSub,
		splitSigning,
		change,
		tabChange,
		toMaterial,
		reqDetailApi
	};
};
