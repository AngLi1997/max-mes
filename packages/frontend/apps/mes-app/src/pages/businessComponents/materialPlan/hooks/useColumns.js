import {
	t
} from '@/utils/useBmosI18n.js';
import {
	reactive,
	ref
} from 'vue';

export const useColumns = () => {
	// 是否刷新当前也
	const refreshPage = ref(false);

	const completedPlan = ref(false);
	// 是否满足 预定暂存量+ 合计量
	const isCur = ref(0);
	// 签名参数
	const signatureData = ref({});
	// tabal遮罩
	const loading = ref(false);
	// 选择树数据
	const current = reactive({
		currentList: {},
		active: ''
	});
	// 预订量
	const orderQuantity = ref(0);
	// 待领量
	const pendingQuantity = ref(0);
	// 计划量合计
	const totalPlannedQuantity = ref(0);
	// 理论量合计
	const totalTheoreticalQuantity = ref(0);
	// tab选项卡
	const tabSub = reactive({
		list: [{
			name: t('暂存物料')
		}, {
			name: t('仓库物料')
		}],
		index: 0
	});

	const paramsData = ref({});
	return {
		tabSub,
		loading,
		isCur,
		refreshPage,
		signatureData,
		current,
		orderQuantity,
		paramsData,
		completedPlan,
		pendingQuantity,
		totalPlannedQuantity,
		totalTheoreticalQuantity
	};
};
