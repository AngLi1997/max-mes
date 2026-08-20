import {
  getRequisitionQuantityCalculate,
  requisitionReceiveRepositoryBatchList,
  requisitionReceiveRepositoryReserveBatch,
} from '@/api';
import {
  debounce,
} from '@/utils/func';
import {
  t,
} from '@/utils/useBmosI18n.js';
import Big from 'big.js';
import {
  nextTick,
  reactive,
  ref,
} from 'vue';

// 设置保留小数点后10位，根据需求调整
Big.RM = Big.DP = 10;
export const useTable = ({
  UseSubTab,
  showNotify,
}) => {
  const {
    currentList,
    orderQuantity,
    selectedData,
  } = UseSubTab;
  // 加载
  const isRefreshPage = ref(false);
  // 表格数据
  const tableData = ref([]);
  const tableRef = ref();
  const tableProps = reactive({
    pagination: false,
    type: 'selection',
    tableColProps: [{
      label: t('物料批号'),
      prop: 'materialBatchNo',
    }, {
      label: t('库存量'),
      prop: 'availableQuantity',
    }, {
      label: t('单位'),
      prop: 'unitName',
    }, {
      label: t('计划量'),
      prop: 'plannedQuantity',
      showInputNumber: (row) => {
        return row.reserved;
      },
    }, {
      label: t('理论量'),
      prop: 'theoreticalQuantity',
    }, {
      label: `${t('水分')}%`,
      prop: 'hydration',
    }, {
      label: `${t('含量')}%`,
      prop: 'noHydrationContent',
    }, {
      label: t('供应商'),
      prop: 'supplier',
    }, {
      label: t('生产商'),
      prop: 'producer',
    }, {
      label: t('原厂批号'),
      prop: 'originBatchNo',
    }, {
      label: t('有效期至'),
      prop: 'expiredDate',
    }],
  });
  const selectionChange = async (selectedRows) => {
    const promises = tableData.value.map(async (item) => {
      item.reserved = false;
      item.theoreticalQuantity = '';
      if (selectedRows?.findIndex(ls => ls.id === item.id) > -1) {
        item.reserved = true;
        const plannedQuantity = item.plannedQuantity;
        const mse = await ingredientQuantityApi(item, plannedQuantity);
        item.plannedQuantity = plannedQuantity;
        item.theoreticalQuantity = mse;
        return item;
      }
      return item;
    });
    // 使用 Promise.all 等待所有 promise 完成，并收集结果
    const processedItems = await Promise.all(promises);
    tableData.value = processedItems;
    // 过滤出被选择的项目
    const selectedItems = processedItems.filter(item => item.reserved === true);
    // 更新 selected 引用
    selectedData.value = selectedItems;
    addUpTo();
  };
  // tables数据
  const materialList = async () => {
    try {
      const getParams = {
        formulaMaterialId: currentList.value?.id,
        requisitionPlanId: currentList.value?.requisitionPlanId,
      };
      const res = await requisitionReceiveRepositoryBatchList(getParams);
      const successMessage = res.data.map(async (item) => {
        const plannedQuantity = item.reserved ? item.plannedQuantity : item.availableQuantity;
        // const mse = await ingredientQuantityApi(item, plannedQuantity);
        return {
          ...item,
          reserved: item.reserved || false,
          plannedQuantity,
          theoreticalQuantity: '',
        };
      });
      Promise.all(successMessage).then((ls) => {
        tableData.value = ls;
        // 勾选的index下标
        const tableIndex = [];
        res.data.map((item, index) => {
          item.reserved && tableIndex.push(index);
        });
        nextTick(() => {
          tableRef.value?.toggleRowSelection(tableIndex, true);
        });
      });
    }
    catch (error) {
      error.message && showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  const isTableModify = ref(false);
  const updateRow = ({ row, value }) => {
    isTableModify.value = true;
    modifyTable(row, value);
  };
  // 修改表格
  const modifyTable = debounce(async (res, num) => {
    isTableModify.value = true;
    // 使用find找到匹配的项
    const item = tableData.value.find(item => item.id === res.id);
    if (!item) {
      console.error('No item found with id:', res.id);
      return;
    }
    item.plannedQuantity = res.plannedQuantity;
    let numTo = Number(num);
    // 检查num是否大于availableQuantity
    if (numTo > Number(item.availableQuantity)) {
      item.plannedQuantity = item.availableQuantity;
      numTo = item.availableQuantity;
    }
    // 调用API并更新theoreticalQuantity
    const numMac = await ingredientQuantityApi(item, numTo);
    item.theoreticalQuantity = numMac;
    // 更新selectedData
    const selectedItem = selectedData.value.find(ls => ls.id === res.id);
    if (selectedItem) {
      selectedItem.plannedQuantity = item.plannedQuantity;
      selectedItem.theoreticalQuantity = item.theoreticalQuantity;
    }
    await addUpTo();
    isTableModify.value = false;
  }, 500);
  // 提交
  const submit = async () => {
    if (isTableModify.value) {
      return showNotify({
        type: 'warning',
        message: t('理论量未计算完成'),
      });
    }
    if (selectedData.value.length === 0) {
      return showNotify({
        type: 'warning',
        message: t('请勾选物料'),
      });
    }
    try {
      const completeParams = {
        formulaMaterialId: currentList.value?.id,
        materialReservedList: selectedData.value,
        requisitionPlanId: currentList.value?.requisitionPlanId,
      };
      await requisitionReceiveRepositoryReserveBatch(completeParams);
      setTimeout(() => {
        isRefreshPage.value = true;
        uni.$emit('refreshPage', isRefreshPage.value);
        uni.navigateBack();
      }, 1000);
    }
    catch (error) {
      // TODO handle the exception
      error.message && showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  // 计算配料量
  const ingredientQuantityApi = async (data, plannedQuantity) => {
    try {
      const model = {
        formulaMaterialId: currentList.value?.id,
        hydration: data?.hydration || 0,
        noHydrationContent: data?.noHydrationContent || 100,
        quantity: plannedQuantity || 0,
      };
      const res = await getRequisitionQuantityCalculate(model);
      return res.data;
    }
    catch (error) {
      // TODO handle the exception
      error.message && showNotify({
        type: 'warning',
        message: t('计算配料失败'),
      });
    }
  };
  // 合计
  const addUpTo = () => {
    orderQuantity.value = selectedData.value.map(item => item.theoreticalQuantity || 0).reduce((sum, value) =>
      sum.plus(new Big(value)), new Big(0)).toString();
    return orderQuantity.value;
  };
  return {
    tableRef,
    tableProps,
    tableData,
    selectionChange,
    materialList,
    updateRow,
    submit,
  };
};
