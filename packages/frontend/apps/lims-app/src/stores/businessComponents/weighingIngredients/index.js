import { defineStore } from 'pinia';
import { ref } from 'vue';
import {
  queryIngredientPlanByIdApi,
  queryWeighDetailByPlanIdAndBatchIdApi
} from '@/api/weighingIngredientsApi.js';
export const useWeighingIngredientsStore = defineStore(
  'weighingIngredients',
  () => {
    // 未完成的配料单列表
    const ingredientsOptions = ref([]);
    // 选中的配料单
    const selectedIngredients = ref(null);
    // 配料单详情
    const ingredientsDetails = ref({});
    // 物料信息
    const materialInfo = ref(null);
    // 称量信息
    const weighingIngredientsData = ref(null);
    // 选中的秤具
    const selectedBalance = ref(null);
    // 称量详情
    const weighingDetailsData = ref({});
    // 称量人列表
    const weighingPersonList = ref([]);
    // 称量复核人列表
    const reCheckerList = ref([]);
    // 配料称量首页物料件表格
    const tableData = ref([]);

    const setIngredientsOptions = (data) => {
      ingredientsOptions.value = data;
    };

    const setSelectedIngredients = (data) => {
      selectedIngredients.value = data;
    };

    const setMaterialInfo = (data) => {
      materialInfo.value = data;
    };

    const setWeighingIngredientsData = (data) => {
      weighingIngredientsData.value = data;
    };

    const setSelectedBalance = (data) => {
      selectedBalance.value = data;
    };

    // 获取配料单详情
    const getIngredientsDetails = async(params) => {
      try {
        const res = await queryIngredientPlanByIdApi({
          id: selectedIngredients.value.id,
          ...params
        });
        ingredientsDetails.value = res.data;
      } catch (error) {
        error.message &&
          uni.showToast({
            title: error.message,
            icon: 'none'
          });
      }
    };

    // 查询称量详情
    const queryWeighDetailByPlanIdAndBatchId = async() => {
      const { planId, batchId } = {
        batchId: materialInfo.value
          ? materialInfo.value.storageMaterialBatchId
          : '',
        planId: selectedIngredients.value.id
      };
      const res = await queryWeighDetailByPlanIdAndBatchIdApi({
        planId,
        batchId
      });
      weighingDetailsData.value = res.data;
    };

    // 设置称量复核人列表
    const setReCheckerList = (data) => {
      reCheckerList.value = data;
    };

    // 设置称量人列表
    const setWeighingPersonList = (data) => {
      weighingPersonList.value = data;
    };

    const setTableData = (data) => {
      tableData.value = data;
    };
    const pushTableData = (data) => {
      tableData.value.push(data);
    };

    const initWeighingIngredientsStore = async() => {
      ingredientsOptions.value = [];
      selectedIngredients.value = null;
      ingredientsDetails.value = {};
      materialInfo.value = null;
      weighingIngredientsData.value = null;
      selectedBalance.value = null;
      weighingDetailsData.value = {};
      reCheckerList.value = [];
      weighingPersonList.value = [];
      tableData.value = [];
    };

    return {
      ingredientsOptions,
      selectedIngredients,
      ingredientsDetails,
      materialInfo,
      weighingIngredientsData,
      selectedBalance,
      weighingDetailsData,
      reCheckerList,
      weighingPersonList,
      tableData,
      setIngredientsOptions,
      setSelectedIngredients,
      setMaterialInfo,
      setWeighingIngredientsData,
      setSelectedBalance,
      getIngredientsDetails,
      queryWeighDetailByPlanIdAndBatchId,
      setReCheckerList,
      setWeighingPersonList,
      setTableData,
      pushTableData,
      initWeighingIngredientsStore
    };
  }
);
