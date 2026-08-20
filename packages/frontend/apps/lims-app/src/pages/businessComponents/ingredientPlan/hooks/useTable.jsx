import {
	ingredientBoundMaterialBatch
} from '@/api';
import {
	ref,
	nextTick,
	computed
} from 'vue';
import {
	t
} from '@/utils/useBmosI18n.js';
import Big from 'big.js';

// 设置保留小数点后10位，根据需求调整
Big.RM = Big.DP = 10;
export const useTable = ({
	UseCommon
}) => {
	const {
		current,
		ingredientQuantity
	} = UseCommon;
	const tableData = ref([]);
	const tableRef = ref();
	const tableProps = computed(() => {
		const tableColProps = [
			{
				label: t('物料批号'),
				prop: 'materialBatchNo'
			},
			{
				label: t('水分%'),
				prop: 'hydration'
			},
			{
				label: t('含量%'),
				prop: 'noHydrationContent'
			}, {
				label: t('配料量'),
				prop: 'ingredientQuantity'
			}, {
				label: t('单位'),
				prop: 'unitName'
			},
			{
				label: t('有效期至'),
				prop: 'expiredDate'
			}, {
				label: t('供应商'),
				prop: 'supplier'
			}, {
				label: t('生产商'),
				prop: 'producer'
			}, {
				label: t('原厂批号'),
				prop: 'originalBatchNo'
			}, {
				label: t('原始编码'),
				prop: 'originalCode'
			}, {
				label: t('报告单编号'),
				prop: 'reportNo'
			}, {
				label: t('放行单编号'),
				prop: 'licenceNo'
			}
		];
		return {
			pagination: false,
			tableColProps
		};
	});
	// table详情
	const materialTable = async() => {
		try {
			const data = {
				formulaMaterialId: current.currentList?.id,
				ingredientPlanId: current.currentList?.ingredientPlanId
			};
			const res = await ingredientBoundMaterialBatch(data);
			tableData.value = res.data;
			nextTick(() => {
				addUpTo();
			});
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
	// 合计
	const addUpTo = () => {
		// 配料总量
		ingredientQuantity.value = tableData.value.map(item => item.ingredientQuantity || 0).reduce((sum,
			value) =>
			sum.plus(new Big(value)), new Big(0)).toString();
	};
	// 处理刷新
	const estIngredientPlan = (data) => {
		if (data) {
			materialTable();
		}
	};
	return {
		tableRef,
		tableProps,
		tableData,
		materialTable,
		estIngredientPlan
	};
};
