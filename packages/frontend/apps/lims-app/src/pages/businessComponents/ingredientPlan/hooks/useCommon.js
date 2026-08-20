import {
	ref,
	reactive
} from 'vue';
export const useCommon = () => {
	const signatureData = ref({});
	const signValue = ref({
		loginName1: '',
		password1: '',
		userId1: '',
		remark: ''
	});
	const paramsData = ref({});
	const current = reactive({
		currentList: {},
		active: '',
		currentHeight: 0
	});
	// 配料总量
	const ingredientQuantity = ref('-');
	const subDisabled = ref(false);
	return {
		current,
		paramsData,
		ingredientQuantity,
		subDisabled,
		signatureData,
		signValue
	};
};
