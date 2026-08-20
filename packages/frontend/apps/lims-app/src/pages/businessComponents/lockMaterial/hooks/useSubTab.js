import {
	ref,
	reactive
} from 'vue';
import Big from 'big.js';
// 初始化 big.js 设置
Big.RM = Big.DP = 10; // 设置保留小数点后10位，根据需求调整
export const useSubTab = () => {
	const params = reactive({
		whole: 0,
		wholeTotal: 0,
		selectTotal: 0,
		selectData: []
	});
	const currentList = ref({});
	// 合计
	const addUpTo = () => {
		params.selectTotal = params.selectData.map(item => item.theoreticalQuantity || 0).reduce((sum, value) =>
			sum.plus(new Big(value)), new Big(0)).toString();
			// console.log(params.whole,params.selectTotal)
		params.wholeTotal = new Big(params.whole).minus(params.selectTotal).toString();
		params.wholeTotal = params.wholeTotal >= 0 ? params.wholeTotal : null;
	};
	return {
		params,
		currentList,
		addUpTo
	};
};
