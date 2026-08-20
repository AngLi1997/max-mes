import {
	ingredientComplete,
	reqUserListByAuthCodeAndPlanIdApi
} from '@/api';
import {
	ref
} from 'vue';
import {
	urlQueryRef,
	pageBasicDataRef,
	getCurrentCopyRecordItem,
	initFillData2
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import {
	t
} from '@/utils/useBmosI18n.js';
export const useModel = ({
	UseCommon,
	UseTable,
	toast
}) => {
	const showSign = ref(false);
	const labelList = ref([{
		label: t('计划人'),
		signatureAction: 55,
		options: [],
        currentUser: true
	}]);
	const getUserList = async() => {
		try {
			const { data } = await reqUserListByAuthCodeAndPlanIdApi({
				permissionCode: '121010001002003',
				productPlanId: urlQueryRef.value?.productPlanId
			});
			labelList.value[0].options = data?.map(item => {
				return {
					label: `${item.userName}`,
					value: item.loginName,
					id: item.userId
				};
			});
		} catch (error) {
			labelList.value[0].options = [];
		}
	};
	const {
		paramsData,
		current,
		subDisabled,
		signatureData,
		signValue
	} = UseCommon;
	const signSubmit = async() => {
		try {
			await ingredientComplete(signatureData.value);
			uni.showLoading({
				title: `${t('保存中')}...`,
				mask: true
			});
			showSign.value = false;
			uni.hideLoading();
			uni.navigateBack();
			initFillData2();
		} catch (error) {
			// TODO handle the exception
			error.message && uni.showToast({
				title: error.message,
				icon: 'none',
				duration: 2000,
				mask: true
			});
		}
	};
	// 提交表格
	const submit = () => {
		signValue.value = {
			loginName1: '',
			password1: '',
			userId1: '',
			remark: ''
		};
		if (subDisabled.value) return toast.show(t('配料计划已完成'));
		signatureData.value = {
			batchNo: urlQueryRef.value?.batchNo,
			componentId: paramsData.value?.id,
			copyVersion: getCurrentCopyRecordItem()?.version,
			procedureStepId: pageBasicDataRef.value?.procedureStepId,
			procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
			processId: urlQueryRef.value?.processId,
			processVersion: urlQueryRef.value?.processVersion,
			productPlanId: urlQueryRef.value?.productPlanId,
			recordItemId: pageBasicDataRef.value?.recordItemId,
			recordVersionId: pageBasicDataRef.value?.recordVersionId,
			ingredientPlanId: current.currentList?.ingredientPlanId
		};
		showSign.value = true;
	};
	return {
		labelList,
		showSign,
		submit,
		signSubmit,
		getUserList
	};
};
