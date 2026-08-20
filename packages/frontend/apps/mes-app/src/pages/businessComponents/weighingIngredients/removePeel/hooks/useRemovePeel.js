import { getStorageConfigTreeApi, reqPrintStorageMaterialTagApi } from '@/api';
import {
  scanWeighContainerCodeApi,
  scanWeighPositionCodeApi,
  weighAndPrintApi,
  weighFinishApi,
} from '@/api/weighingIngredientsApi.js';
import { initFillData2 } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { useWeighingIngredientsStore } from '@/stores/businessComponents/weighingIngredients/index.js';
import { isEmptyObject } from '@/utils/func.js';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import Big from 'big.js';
import { storeToRefs } from 'pinia';
import { computed, onMounted, onUnmounted, ref } from 'vue';

export const useRemovePeel = ({
  weightInfo,
  getReadings,
  props,
  auto,
  isBackMessage,
  selectedBalance,
}) => {
  const weighingIngredientsStore = useWeighingIngredientsStore();
  const { weighingDetailsData: detailData } = storeToRefs(
    weighingIngredientsStore,
  );
  const { queryWeighDetailByPlanIdAndBatchId } = weighingIngredientsStore;

  const toleranceRange = computed(() => {
    if (!isEmptyObject(detailData.value)) {
      let min = detailData.value.minTolerance || 0;
      let max = detailData.value.maxTolerance || 0;
      if (!detailData.value.toleranceTypeEnum) {
        return '';
      }
      let type = detailData.value.toleranceTypeEnum.value;
      if (detailData.value.weighProcess.value === 2) {
        min = detailData.value.oddMinTolerance || 0;
        max = detailData.value.oddMaxTolerance || 0;
        if (!detailData.value.oddToleranceTypeEnum) {
          return '';
        }
        type = detailData.value.oddToleranceTypeEnum.value;
      }
      if (type === 0) {
        return `-${min}%~${max}%`;
      }
      return `-${min}~${max}${detailData.value.unit}`;
    }
    return '';
  });
  const showPositionModal = ref(false);
  const treePositionData = ref([]);
  const lineProgressRef = ref();
  const openStorageModal = async () => {
    const res = await getStorageConfigTreeApi();
    treePositionData.value = res.data;
    showPositionModal.value = true;
  };
  const selectStorage = (data) => {
    if (data) {
      storageId.value = data.id;
      storage.value = data.name;
    }
    else {
      storageId.value = '';
      storage.value = '';
    }
  };
  const loading = ref(false);
  // 余料称量超出签名
  const beyondSignOpen = ref(false);
  // 称量结果弹窗数据
  const confirmDetailsData = ref({});
  // 当前称量状态
  const currentProcess = ref(0);
  // 打印实例
  const bmosPrinterInstance = ref(null);
  const configRef = ref();
  // 03货位 04容器
  // 容器
  const container = ref('');
  // 容器id
  const containerId = ref('');
  // 货位
  const storage = ref('');
  // 货位id
  const storageId = ref('');
  const operationType = ref(0); // 0清零 1去皮 2称量打码
  const submitBtnText = ref([t('清零'), t('去皮'), t('称量打码')]);

  const timer = ref(null);

  // 皮重
  const tare = ref();
  // 净重
  const netWeight = computed(() => {
    return weightInfo.weight;
  });
  // 毛重
  const grossWeight = computed(() => {
    try {
      return Big(netWeight.value)
        .plus(tare.value)
        .toString();
    }
    catch (error) {
      return '';
    }
  });

  // 手动模式净重=毛重-皮重

  const netWeightManual = computed(() => {
    try {
      return Big(grossWeightManual.value)
        .minus(tare.value)
        .toString();
    }
    catch (error) {
      return '';
    }
  });
  // 手动模式毛重
  const grossWeightManual = ref();

  //   剩余量 = 目标量与秤具示数的差值
  const remainingAmount = computed(() => {
    try {
      return operationType.value === 2
        ? Big(detailData.value.unWeighedQuantity || 0)
            .minus(netWeight.value)
            .toString()
        : '0';
    }
    catch (error) {
      return '0';
    }
  });
  // 称量类型 配料称量、余料称量
  const weighingType = computed(() => {
    return detailData.value.weighProcess.value === 1
      ? 'toleranceDiff'
      : 'oddToleranceDiff';
  });
  // 称量范围
  const weighingRange = computed(() => {
    return detailData.value[weighingType.value];
  });
  // 误差最大值
  const maxRange = computed(() => {
    return weighingRange.value[2] || weighingRange.value[1];
  });
  // 称量打码
  const weighAndPrint = async (skipVerification = false) => {
    if (!skipVerification) {
      // 校验称量人是否与当前登录人相同
      const currentUser = getStorageSync(USER_INFO) || {};
      const { userId } = currentUser;
      if (userId !== detailData.value.weigherId) {
        uni.showToast({
          title: t('登录账号与称量人不符'),
          icon: 'none',
        });
        return;
      }
      if (Number(netWeight.value) <= 0 && auto.value) {
        uni.showToast({ title: t('称量结果必须大于0'), icon: 'none' });
        return;
      }
      const result = auto.value ? netWeight.value : netWeightManual.value;
      if (Number(maxRange.value) < Number(result)) {
        if (detailData.value.weighProcess.value === 1) {
          uni.showToast({ title: t('超出批次目标量范围'), icon: 'none' });
          return;
        }
        if (detailData.value.weighProcess.value === 2) {
          beyondSignValue.value = {
            userName1: '',
            loginName1: '',
            password1: '',
            userId1: '',
          };
          beyondSignOpen.value = true;
          return;
        }
      }
    }
    const device = bmosPrinterInstance.value.print();
    if (device) {
      const res = await handleWeighPrint();
      if (res) {
        reqPrintStorageMaterialTagApi({
          deviceId: device.id,
          sceneId:
            detailData.value.categoryType.value === 0 ? 121001001 : 121002001,
          body: {
            no: confirmDetailsData.value.no,
          },
        });
        return true;
      }
      return false;
    }
    else {
      return false;
    }
  };

  const beyondSignValue = ref({
    userName1: '',
    loginName1: '',
    password1: '',
    userId1: '',
  });
  const signatureData2 = computed(() => {
    return {
      containerId: containerId.value,
      grossWeight: auto.value ? grossWeight.value : grossWeightManual.value,
      ingredientPlanId: detailData.value.ingredientPlanId,
      materialPositionId: storageId.value,
      netWeight: auto.value ? netWeight.value : netWeightManual.value,
      storageMaterialBatchId: detailData.value.storageMaterialBatchId,
      tareWeight: tare.value,
      unitId: detailData.value.unitId,
      weighMode: props.auto === '0' ? 1 : 2,
      weighProcess: detailData.value.weighProcess
        ? detailData.value.weighProcess.value
        : '',
    };
  });
  // 调用称量打码接口
  const handleWeighPrint = async () => {
    loading.value = true;
    currentProcess.value = detailData.value.weighProcess.value;
    const params = {
      ...signatureData2.value,
      deviceId: selectedBalance.value?.balanceId || undefined,
    };
    try {
      const res = await weighAndPrintApi(params);
      confirmDetailsData.value = res.data;
      initFillData2();
      await queryWeighDetailByPlanIdAndBatchId();
      configRef.value.open();
      loading.value = false;
      if (res.code === 0) {
        return true;
      }
      return false;
    }
    catch (error) {
      loading.value = false;
      error.message && uni.showToast({ title: error.message, icon: 'none' });
      return false;
    }
  };

  // 超出签名确认
  const beyondSignConfirm = async () => {
    weighAndPrint(true);
  };

  // 继续称量
  const continueWeighing = () => {
    if (detailData.value.weighProcess.value === 3) {
      uni.navigateTo({
        url: `/pages/businessComponents/weighingIngredients/index?componentId=${
          props.componentId
        }`,
      });
    }
    else {
      operationType.value = 0;
      container.value = '';
      containerId.value = '';
      storage.value = '';
      storageId.value = '';
      tare.value = '';
      grossWeightManual.value = '';
    }
  };

  const signValue = ref({
    userName1: '',
    loginName1: '',
    password1: '',
    userId1: '',
  });

  const signatureData1 = computed(() => {
    return {
      ingredientPlanId: detailData.value.ingredientPlanId,
      storageMaterialBatchId: detailData.value.storageMaterialBatchId,
    };
  });
  // 完成称量
  const weighFinishSignConfirm = async () => {
    const params = {
      ...signatureData1.value,
      finisherId: signValue.value.userId1,
    };
    await weighFinishApi(params);
    if (detailData.value.weighProcess.value === 2) {
      // 返回配料称量首页
      uni.navigateTo({
        url: `/pages/businessComponents/weighingIngredients/index?componentId=${
          props.componentId
        }`,
      });
    }
    else {
      queryWeighDetailByPlanIdAndBatchId();
    }
  };
  // 获取容器/货位信息
  const getContainerAndStorageInfo = async (type, code) => {
    if (code === '') {
      return;
    }
    try {
      if (type === '03') {
        const res = await scanWeighPositionCodeApi({
          code,
        });
        storage.value = `${res.data.fullName}`;
        storageId.value = res.data.id;
      }
      if (type === '04') {
        const res = await scanWeighContainerCodeApi({
          code,
        });
        container.value = `${res.data.deviceCode} - ${res.data.deviceName}`;
        containerId.value = res.data.deviceId;
      }
    }
    catch (error) {
      error.message && uni.showToast({ title: error.message, icon: 'none' });
    }
  };

  onMounted(() => {
    timer.value = setInterval(() => {
      if (weightInfo.open && isBackMessage.value) {
        getReadings();
      }
    }, 250);
  });
  onUnmounted(() => {
    if (timer.value) {
      clearInterval(timer.value);
      timer.value = null;
    }
  });

  return {
    loading,
    container,
    storage,
    storageId,
    detailData,
    operationType,
    submitBtnText,
    tare,
    netWeight,
    netWeightManual,
    grossWeightManual,
    grossWeight,
    remainingAmount,
    toleranceRange,
    configRef,
    showPositionModal,
    treePositionData,
    bmosPrinterInstance,
    confirmDetailsData,
    currentProcess,
    beyondSignOpen,
    signValue,
    beyondSignValue,
    signatureData1,
    signatureData2,
    weighAndPrint,
    handleWeighPrint,
    continueWeighing,
    queryWeighDetailByPlanIdAndBatchId,
    weighFinishSignConfirm,
    getContainerAndStorageInfo,
    openStorageModal,
    selectStorage,
    beyondSignConfirm,
    lineProgressRef,
  };
};
