import {
  getIngredientWeighProcessApi,
  makeSureWeighApi,
  postSingerListWithPermissionCodeAndComponentApi,
  queryPendingIngredientPlanListApi,
  scanWeighMaterialCodeApi,
} from '@/api/weighingIngredientsApi.js';
import {
  getCurrentCopyRecordItem,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';

import { useWeighingIngredientsStore } from '@/stores/businessComponents/weighingIngredients/index.js';
import { buildUrlQuery } from '@/utils/url';
import { t } from '@/utils/useBmosI18n.js';
import { onShow } from '@dcloudio/uni-app';
import Big from 'big.js';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref } from 'vue';

export const usePageInfo = (data) => {
  const weighingIngredientsStore = useWeighingIngredientsStore();
  const {
    ingredientsOptions,
    selectedIngredients,
    ingredientsDetails,
    materialInfo,
    weighingIngredientsData,
    reCheckerList,
    tableData,
  } = storeToRefs(weighingIngredientsStore);
  const {
    setIngredientsOptions,
    setSelectedIngredients,
    getIngredientsDetails,
    setMaterialInfo,
    setWeighingIngredientsData,
    setReCheckerList,
    setWeighingPersonList,
    pushTableData,
    setTableData,
    initWeighingIngredientsStore,
  } = weighingIngredientsStore;
  const query = ref({});

  // 表格标题
  const tableLabel = ref([
    {
      label: '',
      align: 'center',
      dataIndex: 'BMOSDelete',
      width: '80',
    },
    {
      label: t('物料件号'),
      align: 'left',
      dataIndex: 'no',
      width: '190',
    },
    {
      label: t('物料量'),
      align: 'left',
      dataIndex: 'quantity',
      width: '190',
    },
    {
      label: t('单位'),
      align: 'left',
      dataIndex: 'unit',
      width: '190',
    },
    {
      label: t('水分%'),
      align: 'left',
      dataIndex: 'hydration',
      width: '190',
    },
    {
      label: t('含量%'),
      align: 'left',
      dataIndex: 'noHydrationContent',
      width: '190',
    },
    {
      label: t('有效期至'),
      align: 'left',
      dataIndex: 'expiredDate',
      width: '190',
    },
    {
      label: t('原厂批号'),
      align: 'left',
      dataIndex: 'factoryBatchNo',
      width: '190',
    },
    {
      label: t('供应商'),
      align: 'left',
      dataIndex: 'supplier',
      width: '190',
    },
  ]);

  // 总量
  const totalQuantity = computed(() => {
    return tableData.value.reduce((total, item) => {
      return Big(total)
        .plus(item.quantity)
        .toString();
    }, 0);
  });

  // 配料单选择弹框实例
  const ingredientsSelect = ref(null);

  // 选择称量物料弹框显示
  const showMaterialPopup = ref(false);
  // 签名弹框显示
  const signOpen = ref(false);
  // 签名弹框数据
  const signValue = ref({
    userName1: '',
    userName2: '',
    loginName1: '',
    loginName2: '',
    password1: '',
    password2: '',
    userId1: '',
    userId2: '',
    remark: '',
  });

  // 签名数据
  const signatureData = computed(() => {
    return {
      componentId: query.value.componentId,
      consumeStorateMaterialIdList: tableData.value.map(item => item.id),
      copyVersion: getCurrentCopyRecordItem().version,
      ingredientPlanId: selectedIngredients.value
        ? selectedIngredients.value.id
        : '',
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      productPlanId: urlQueryRef.value.productPlanId,
      storageMaterialBatchId: materialInfo.value
        ? materialInfo.value.storageMaterialBatchId
        : '',
      weigherId: signValue.value.userId1 || undefined,
      reCheckerId: signValue.value.userId2 || undefined,
      remark: signValue.value.remark || undefined,
    };
  });

  // 获取未完成的配料单
  const getIngredientsOptions = async () => {
    const res = await queryPendingIngredientPlanListApi({
      batchNo: urlQueryRef.value.batchNo,
      productPlanId: urlQueryRef.value.productPlanId,
    });
    setIngredientsOptions(res.data);
    if (ingredientsOptions.value.length === 1) {
      ingredientsConfirm(ingredientsOptions.value[0]);
      return;
    }
    ingredientsSelect.value.open();
  };

  // 选择配料单确认
  const ingredientsConfirm = (data) => {
    setSelectedIngredients(data);
    getIngredientsDetails({
      componentId: query.value.componentId,
      procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
    });
  };
  // 取消选择配料单
  const ingredientsCancel = () => {
    uni.navigateBack();
  };

  const chooseMaterial = () => {
    if (
      weighingIngredientsData.value
      && weighingIngredientsData.value.pendingStorageMaterialBatchId
    ) {
      uni.showToast({
        title: t('配料批次处于称量中，无法切换'),
        icon: 'none',
      });
      return;
    }
    if (ingredientsDetails.value && ingredientsDetails.value.length === 0) {
      uni.showToast({
        title: t('无配料批次未称量'),
        icon: 'none',
      });
      return;
    }
    showMaterialPopup.value = true;
  };
  // 物料选择批次弹框确认
  const materialPopupConfirm = (data) => {
    showMaterialPopup.value = false;
    if (
      materialInfo.value
      && data
      && materialInfo.value.storageMaterialBatchId === data.storageMaterialBatchId
    ) {
      return;
    }
    setMaterialInfo(data);
    setTableData([]);
  };

  // 物料件号搜索
  const searchMaterialCode = async (params) => {
    try {
      const res = await scanWeighMaterialCodeApi(params);
      if (tableData.value.some(item => item.id === res.data.id)) {
        data.toast.show(t('物料件已添加，不能重复添加'));
        return;
      }
      if (res.data) {
        pushTableData(res.data);
      }
    }
    catch (error) {
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
      });
    }
  };

  // 确认称量
  const signConfirm = async () => {
    try {
      await makeSureWeighApi(signatureData.value);
      await getIngredientWeighProcess();
      signOpen.value = false;
      goModeDevice();
    }
    catch (error) {
      uni.showToast({
        title: error.message,
        icon: 'none',
      });
    }
  };
  // 跳转到称量模式选择页面
  const goModeDevice = () => {
    uni.redirectTo({
      url: `/pages/businessComponents/weighingIngredients/modeDevice/index?${buildUrlQuery(query.value)}`,
    });
  };

  // 查询配料称量信息
  const getIngredientWeighProcess = async () => {
    const params = {
      componentId: query.value.componentId,
      copyVersion: getCurrentCopyRecordItem().version,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      productPlanId: urlQueryRef.value.productPlanId,
    };
    const res = await getIngredientWeighProcessApi(params);
    setWeighingIngredientsData(res.data);
  };

  // 获取复核人列表
  const getRecheckerList = async () => {
    try {
      const res = await postSingerListWithPermissionCodeAndComponentApi({
        permissionCode: '121010001002004',
        componentId: query.value.componentId,
        procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
        productPlanId: urlQueryRef.value.productPlanId,
      });
      const list = res.data || [];
      setReCheckerList(list);
    }
    catch (error) {
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
      });
    }
  };

  // 获取称量人列表
  const getWeighingPersonList = async () => {
    try {
      const res = await postSingerListWithPermissionCodeAndComponentApi({
        componentId: query.value.componentId,
        procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
        productPlanId: urlQueryRef.value.productPlanId,
      });
      const list = (res.data || []);
      setWeighingPersonList(list);
    }
    catch (error) {
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
      });
    }
  };

  const first = ref(true);
  onShow(async () => {
    if (!first.value) {
      await getIngredientWeighProcess();
    }
  });
  onMounted(async () => {
    first.value = false;
    getRecheckerList();
    getWeighingPersonList();
    initWeighingIngredientsStore();
    await getIngredientWeighProcess();
    if (weighingIngredientsData.value === null) {
      await getIngredientsOptions();
    }
    else {
      setSelectedIngredients({
        id: weighingIngredientsData.value.ingredientPlanId,
      });
      setMaterialInfo(
        weighingIngredientsData.value.pendingStorageMaterialBatchId
          ? {
              storageMaterialBatchId:
                weighingIngredientsData.value.pendingStorageMaterialBatchId,
              unit:
                weighingIngredientsData.value.pendingStorageMaterialBatchUnit,
              unitId:
                weighingIngredientsData.value.pendingStorageMaterialBatchUnitId,
            }
          : null,
      );
      if (selectedIngredients.value.id) {
        getIngredientsDetails({
          componentId: query.value.componentId,
          procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
        });
      }
    }
  });

  return {
    query,
    tableLabel,
    tableData,
    ingredientsSelect,
    ingredientsOptions,
    selectedIngredients,
    ingredientsDetails,
    showMaterialPopup,
    materialInfo,
    totalQuantity,
    signOpen,
    signValue,
    signatureData,
    weighingIngredientsData,
    reCheckerList,
    ingredientsConfirm,
    ingredientsCancel,
    materialPopupConfirm,
    searchMaterialCode,
    signConfirm,
    goModeDevice,
    chooseMaterial,
  };
};
