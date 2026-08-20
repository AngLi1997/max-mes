import {
	requisitionReservedAvailableMaterialApi,
	requisitionStorageReserve
} from '@/api';
import {
	ref,
	reactive,
	nextTick
} from 'vue';
import {
	t
} from '@/utils/useBmosI18n.js';

export const useTable = ({
	UseSubTab,
	showNotify
}) => {
	const {
		params,
		currentList,
		addUpTo
	} = UseSubTab;
	// 表格数据
	const tableData = ref([]);
	const tableRef = ref();
	const tableProps = reactive({
		pagination: false,
		type: 'selection',
		tableColProps: [{
			label: t('物料批号'),
			prop: 'materialBatchNo'
		}, {
			label: t('物料件号'),
			prop: 'materialNo'
		}, {
			label: t('物料量'),
			prop: 'quantity'
		}, {
			label: t('理论量'),
			prop: 'theoreticalQuantity'
		}, {
			label: t('单位'),
			prop: 'unitName'
		}, {
			label: t('水分') + '%',
			prop: 'hydration'
		}, {
			label: t('含量') + '%',
			prop: 'noHydrationContent'
		}, {
			label: t('暂存货位'),
			prop: 'materialPositionName'
		}, {
			label: t('货位编码'),
			prop: 'materialPositionCode'
		}, {
			label: t('供应商'),
			prop: 'supplier'
		}, {
			label: t('有效期至'),
			prop: 'expiredDate'
		}]
	});
	const selectionChange = (selectedRows) => {
		params.selectData = selectedRows;
		addUpTo();
	};
	const materialApi = async() => {
		try {
			const paramsModel = {
				productPlanId: currentList.value?.productPlanId,
				materialId: currentList.value?.materialId,
				formulaMaterialId: currentList.value?.id
			};
			const res = await requisitionReservedAvailableMaterialApi(paramsModel);
			tableData.value = res.data;
			params.whole = currentList.value?.theoreticalQuantity;
			// params.wholeTotal = params.whole;
			// 勾选的index下标
			const tableIndex = [];
			res.data.map((item, index) => {
				item.reserved && tableIndex.push(index);
			});
			nextTick(() => {
				tableRef.value?.toggleRowSelection(tableIndex, true);
			});
		} catch (error) {
			// TODO handle the exception
		}
	};
	// 提交
	const submit = async() => {
		if (params.selectData.length === 0) return showNotify({
			type: 'warning',
			message: t('请勾选要预定的物料')
		});
		try {
			const data = {
				batchId: currentList.value?.productPlanId,
				processId: currentList.value?.processId,
				materialId: currentList.value?.materialId,
				storageMaterialIdList: params.selectData.map(item => item.storageMaterialId)
			};
			await requisitionStorageReserve(data);
			uni.navigateBack();
		} catch (error) {
			// TODO handle the exception
			error.message && showNotify({
				type: 'danger',
				message: error.message
			});
		}
	};
	return {
		tableRef,
    tableProps,
		tableData,
		selectionChange,
		materialApi,
		submit
	};
};
