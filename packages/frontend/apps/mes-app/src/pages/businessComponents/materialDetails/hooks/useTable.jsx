import {
	getRequisitionReceiveMaterialList
} from '@/api';
import { t } from '@/utils/useBmosI18n.js';

import {
	nextTick,
	ref,
	computed
} from 'vue';
import Big from 'big.js';
// 设置保留小数点后10位，根据需求调整
Big.RM = Big.DP = 10;
export const useTable = ({
	UseSubTab
}) => {
	const {
		paramsData
	} = UseSubTab;
	// 是否修改表单
	const isModifySelected = ref([]);
	// ref
	const materialDetailsTable = ref();
	// 出库总量
	const outboundQuantity = ref(0);
	const tableRef = ref();
	const tableProps = computed(() => {
		return {
		trProps: (row) => {
			return {
			class: row.isDisabled ? 'confirm' : 'undetermined'
			};
		},
		selectionProps: (row) => { // 已接收了的不能再勾
			return {
			disabled: row.isDisabled
			};
		},
		pagination: false,
		type: 'selection',
		data: tableData.value,
	tableColProps: [
			{
			label: t('物料件号'),
			prop: 'materialNo'
			}, {
			label: t('出库量'),
			prop: 'quantity'
			}, {
			label: t('单位'),
			prop: 'unitName'
			}, {
			label: t('货位'),
			prop: 'cargoPositionName'
			}
		]
		};
	});
	const tableData = ref([]);
	// 已选数据
	const selectedList = ref([]);
	const selectionChange = (selectedRows) => {
        const model = selectedRows;
        selectedList.value = model;
		isModifySelected.value = model.filter(item => !item.isDisabled);
		addUpTo(model);
	};
	// 物料详情列表Api
	const apiDetailsList = async() => {
		try {
			const apiParams = {
				receivedBatchId: paramsData.value?.id,
				requisitionId: paramsData.value?.requisitionId
			};
			const res = await getRequisitionReceiveMaterialList(apiParams);
			tableData.value = res.data.map(item => {
				return {
					...item,
					isDisabled: item.cargoPositionId ? true : false
				};
			});
			checkTable(tableData.value);
		} catch (error) {
			// TODO handle the exception
			error.message && uni.showToast({
				title: error.message,
				icon: 'none',
				duration: 2000
			});
		}
	};
	// 勾选表格
	const checkTable = (arr) => {
		const tableIndex = [];
		arr.map((item, index) => {
				item.isDisabled && tableIndex.push(index);
			});
			nextTick(() => {
				tableRef.value?.toggleRowSelection(tableIndex, true);
			});
	};
	// 合计
	const addUpTo = (arr) => {
		// 出库总量
		outboundQuantity.value = arr.map(item => item.quantity || 0).reduce((sum, value) =>
			sum.plus(new Big(value)), new Big(0)).toString();
	};
	return {
		tableRef,
		tableProps,
		isModifySelected,
		materialDetailsTable,
		tableData,
		selectedList,
		outboundQuantity,
		selectionChange,
		apiDetailsList
	};
};
