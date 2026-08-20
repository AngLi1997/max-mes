import {
  ingredientAvailableBoundMaterialBatch,
  ingredientBindMaterialBatch,
  postingredientcalCulateBatch,
} from '@/api';
import {
  t,
} from '@/utils/useBmosI18n.js';
import Big from 'big.js';
import {
  computed,
  nextTick,
  ref,
} from 'vue';

// 设置保留小数点后10位，根据需求调整
Big.RM = Big.DP = 10;
export const useTable = ({
  UseSubTab,
}) => {
  const {
    currentList,
    orderQuantity,
    theoryAmount,
    selectedData,
  } = UseSubTab;
  const addMaterialsTableRef = ref();
  // 加载
  const loading = ref(false);
  const isRefreshPage = ref(false);
  // 表格数据
  const tableData = ref([]);
  const tableRef = ref();
  const tableProps = computed(() => {
    return {
      selectionProps: (row) => {
        return {
          disabled: row.isDisabled,
        };
      },
      pagination: false,
      type: 'selection',
      data: tableData.value,
      tableColProps: [
        {
          label: t('物料批号'),
          prop: 'materialBatchNo',
        },
        {
          label: t('水分%'),
          prop: 'hydration',
        },
        {
          label: t('含量%'),
          prop: 'noHydrationContent',
        },
        {
          label: t('物料量'),
          prop: 'materialQuantity',
        },
        {
          label: t('配料量'),
          prop: 'ingredientQuantity',
        },
        {
          label: t('剩余量'),
          prop: 'remainingQuantity',
        },
        {
          label: t('单位'),
          prop: 'unitName',
        },
        {
          label: t('有效期至'),
          prop: 'expiredDate',
          width: '190',
        },
        {
          label: t('供应商'),
          prop: 'supplier',
        },
        {
          label: t('生产商'),
          prop: 'producer',
        },
        {
          label: t('原厂批号'),
          prop: 'originalBatchNo',
        },
        {
          label: t('原始编码'),
          prop: 'originalCode',
        },
        {
          label: t('报告单编号'),
          prop: 'reportNo',
        },
        {
          label: t('放行单编号'),
          prop: 'licenceNo',
        },
      ],
    };
  });
  // 勾选表格的方法
  const checkTable = (arr) => {
    const tableIndex = [];
    arr.map((item, index) => {
      item.bound && tableIndex.push(index);
      return item;
    });
    nextTick(() => {
      tableRef.value?.toggleRowSelection(tableIndex, true);
    });
  };
  // 计算配料量
  const ingredientQuantityApi = async (data) => {
    if (data.length === 0)
      return false;
    try {
      const model = {
        ingredientPlanId: currentList.value?.ingredientPlanId,
        formulaMaterialId: currentList.value?.id,
        materialBatchIdList: data,
      };
      const res = await postingredientcalCulateBatch(model);
      return res.data;
    }
    catch (error) {
      // TODO handle the exception
      error.message && uni.showToast({
        title: '计算配料失败',
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };
  // 计算剩余量
  const calculate = (totalNum, current) => {
    return new Big(totalNum || 0).minus(new Big(current || 0)).toString();
  };
  // 选择
  const selectionChange = (selectedRows) => {
    selectedData.value = selectedRows;
    Promise.all(selectedData.value).then(() => {
      const successMessage = selectedData.value.map(el => el.materialBatchId);
      Promise.all(successMessage).then(async (data) => {
        if (data.length === 0) {
          orderQuantity.value = 0;
          theoryAmount.value = 0;
          selectedData.value = [];
          return tableData.value = tableData.value.map((item) => {
            return { ...item, ingredientQuantity: 0, isDisabled: false, theoreticalQuantity: 0, remainingQuantity: calculate(item.materialQuantity, 0) };
          });
        }
        else {
          const res = await ingredientQuantityApi(data);
          orderQuantity.value = res.ingredientTotalQuantity;// 配料总量
          theoryAmount.value = res.chosenTheoreticalQuantity;// 已选理论量
          const motData = [];
          if (res) {
            tableData.value = tableData.value.map((item) => {
              const itemId = res.ingredientQuantityList.find(el => el.materialBatchId === item.materialBatchId);
              if (itemId) {
                // 使用扩展操作符或 Object.assign 来合并对象  isDisabled判断是否禁用该行勾选框
                const el = { ...item, ...itemId, isDisabled: false, remainingQuantity: calculate(item.materialQuantity, itemId.ingredientQuantity) };
                motData.push(el);
                return el;
              }
              else {
                return { ...item, ingredientQuantity: 0, isDisabled: Number(currentList.value.theoreticalQuantity) <= Number(theoryAmount.value), theoreticalQuantity: 0, remainingQuantity: calculate(item.materialQuantity, 0) };
              }
            });
            Promise.all(motData).then((trs) => {
              selectedData.value = trs;
            });
          }
          else {
            orderQuantity.value = 0;
            theoryAmount.value = 0;
            selectedData.value = [];
            tableData.value = tableData.value.map((item) => {
              return {
                ...item,
                isDisabled: false,
              };
            });
          }
        }
      });
    });
  };
  // tables数据(初始化)
  const materialList = async () => {
    loading.value = true;
    try {
      const getParams = {
        batchId: currentList.value?.batchId,
        formulaMaterialId: currentList.value?.id,
        ingredientPlanId: currentList.value?.ingredientPlanId,
      };
      const res = await ingredientAvailableBoundMaterialBatch(getParams);
      tableData.value = res.data.map((item) => {
        return {
          ...item,
          remainingQuantity: calculate(item.materialQuantity, item.ingredientQuantity),
          isDisabled: false,
          checked: item.bound || false,
        };
      });
      checkTable(tableData.value);// 回显表格
      loading.value = false;
    }
    catch (error) {
      // TODO handle the exception
      loading.value = false;
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };

  // 提交
  const submit = async () => {
    if (selectedData.value.length === 0) {
      return uni.showToast({
        title: t('请勾选物料'),
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
    if (Number(currentList.value.theoreticalQuantity) > Number(theoryAmount.value)) {
      return uni.showToast({
        title: t('理论用量未满足，无法完成物料配料'),
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
    try {
      const completeParams = {
        formulaMaterialId: currentList.value?.id,
        ingredientPlanId: currentList.value?.ingredientPlanId,
        materialBatchList: selectedData.value,
      };
      await ingredientBindMaterialBatch(completeParams);
      uni.showLoading({
        title: t('保存中...'),
        mask: true,
      });
      setTimeout(() => {
        isRefreshPage.value = true;
        uni.hideLoading();
        uni.$emit('IngredientPlan', isRefreshPage.value);
        uni.navigateBack();
      }, 1000);
    }
    catch (error) {
      // TODO handle the exception
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };

  return {
    tableRef,
    tableProps,
    loading,
    tableData,
    addMaterialsTableRef,
    selectionChange,
    materialList,
    submit,
  };
};
