import {
	requisitionRepositoryAvailableQuantityList,
	requisitionReceiveRepositoryReserveBatch
} from '@/api';
import {
	nextTick,
	ref,
	reactive
} from 'vue';
import {
	t
} from '@/utils/useBmosI18n.js';
import Big from 'big.js';

// 设置保留小数点后10位，根据需求调整
Big.RM = Big.DP = 10;
export const useTable = ({
	UseSubTab,
	showNotify
}) => {
	const {
		currentList,
		orderQuantity,
		selectedData
	} = UseSubTab;
	// 加载
	const loading = ref(false);
	const isRefreshPage = ref(false);
	// 表格数据
	const tableData = ref([]);
	const tableRef = ref();
	const tableProps = reactive({
		pagination: false,
		type: 'selection',
		tableColProps: [{
			label: t('物料编码'),
			prop: 'mergeCode'
		}, {
			label: t('物料规格'),
			prop: 'specification'
		}, {
			label: t('库存量'),
			prop: 'inventoryQuantity'
		}, {
			label: t('单位'),
			prop: 'unitName'
		}, {
			label: t('领料量'),
			prop: 'reservedQuantity',
			showInputNumber: (row) => {
				return row.reserved;
			}
		}, {
			label: t('剩余量'),
			prop: 'theoreticalQuantity'
		}, {
			label: t('供应商'),
			prop: 'supplier'
		}, {
			label: t('生产商'),
			prop: 'producer'
		}]
	});
	const selectionChange = (selectedRows) => {
		const selected = [];
		tableData.value.forEach((item) => {
			item.reserved = false;
			item.theoreticalQuantity = calculate(item.inventoryQuantity, 0);
			selectedRows?.forEach((el) => {
				if (el.id === item.id) {
					item.reserved = true;
					// item.reservedQuantity = item.inventoryQuantity
					item.theoreticalQuantity = calculate(item.inventoryQuantity, item.reservedQuantity);
					selected.push(item);
				}
			});
		});
		Promise.all(selected).then((res) => {
			selectedData.value = res;
			addUpTo();
		});
	};
	// tables数据
	const materialList = async() => {
		loading.value = true;
		try {
			const getParams = {
				formulaMaterialId: currentList.value?.id,
				requisitionPlanId: currentList.value?.requisitionPlanId
			};
			const res = await requisitionRepositoryAvailableQuantityList(getParams);
			tableData.value = res.data.map(item => {
				const reservedQuantity = item.reserved ? item.reservedQuantity : item.inventoryQuantity;
				return {
					...item,
					reserved: item.reserved || false,
					reservedQuantity: reservedQuantity,
					theoreticalQuantity: calculate(item.inventoryQuantity, reservedQuantity)
				};
			});
			// 勾选的index下标
			const tableIndex = [];
			res.data.map((item, index) => {
				item.reserved && tableIndex.push(index);
			});
			nextTick(() => {
				tableRef.value?.toggleRowSelection(tableIndex, true);
				loading.value = false;
			});
		} catch (error) {
			// TODO handle the exception
			loading.value = false;
			error.message && showNotify({
				type: 'danger',
				message: error.message
			});
		}
	};
	// 修改表格
	const modifyTable = (res, num) => {
		console.log(res, num);
		tableData.value.forEach((item, index) => {
			if (item.id.includes(res.id)) {
				if (Number(num) < Number(item.inventoryQuantity)) {
					item.reservedQuantity = num;
					item.theoreticalQuantity = calculate(item.inventoryQuantity, num);
					selectedData.value.forEach(ls => {
						if (ls.id.includes(res.id)) {
							ls.reservedQuantity = num;
							ls.theoreticalQuantity = item.theoreticalQuantity;
							addUpTo();
						}
					});
				} else {
					item.reservedQuantity = item.inventoryQuantity;
					item.theoreticalQuantity = calculate(item.inventoryQuantity, item.reservedQuantity);
				}
			}
		});
	};
	// 提交
	const submit = async() => {
		if (selectedData.value.length === 0) return showNotify({
			type: 'warning',
			message: t('请勾选物料')
		});
		try {
			selectedData.value.forEach(item => {
				item['materialMergeCode'] = item.mergeCode;
				item['plannedQuantity'] = item.reservedQuantity;
			});
			const completeParams = {
				formulaMaterialId: currentList.value?.id,
				materialReservedList: selectedData.value,
				requisitionPlanId: currentList.value?.requisitionPlanId
			};
			await requisitionReceiveRepositoryReserveBatch(completeParams);
			setTimeout(() => {
				isRefreshPage.value = true;
				uni.$emit('refreshPage', isRefreshPage.value);
				uni.navigateBack();
			}, 1000);
		} catch (error) {
			error.message && showNotify({
				type: 'danger',
				message: error.message
			});
		}
	};
	// 计算剩余量
	const calculate = (num, moisture) => {
		return new Big(num || 0).minus(new Big(moisture || 0)).toString();
	};
	// 合计
	const addUpTo = () => {
		orderQuantity.value = selectedData.value.map(item => item.reservedQuantity || 0).reduce((sum,
			value) =>
			sum.plus(new Big(value)), new Big(0)).toString();
	};
	return {
		tableRef,
		tableProps,
		tableData,
		selectionChange,
		materialList,
		modifyTable,
		submit
	};
};
