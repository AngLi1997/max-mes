import {
  getMesUnitListDownExtendBound,
  getStorageConfigTreeApi,
  reqFreeWeightWeighAndPrint,
  reqPrintStorageMaterialTagApi,
  reqProductMaterialDetail,
  scanWeighContainerCodeApi,
  scanWeighPositionCodeApi,
} from '@/api';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { useMaterialWeighingStore } from '@/stores/workbench/materialWeighing/index.js';
import { numberValidator } from '@/utils/numberValidator.js';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { useBalanceSocket } from '@/utils/useBalanceSocket';
import { t } from '@/utils/useBmosI18n.js';
import { useMathJs } from '@/utils/useMathJs.js';
import { isEmpty } from 'lodash-es';
import { storeToRefs } from 'pinia';
import { computed, onMounted, onUnmounted, reactive, ref, watch } from 'vue';
import { useNotify } from 'wot-design-uni';
import { useConst } from '../../hooks/useConst.js';

export const useRemovePeel = () => {
  const { math } = useMathJs();
  const { showNotify } = useNotify();
  const materialWeighingStore = useMaterialWeighingStore();
  const weighingMachineStore = useWeighingMachineStore();
  const { selectedBalance } = storeToRefs(weighingMachineStore);
  const { detailData, storeSignValue } = storeToRefs(materialWeighingStore);
  const { stepList, weighingMachineOutputColumns } = useConst();

  // 03货位 04容器
  // 容器
  const container = ref('');
  // 容器id
  const containerId = ref('');
  // 货位id
  const storageId = ref('');
  // 货位名称
  const storage = ref('');
  const showStorageModal = ref(false);
  const treeStorageData = ref([]);
  // 打印实例
  const bmosPrinterInstance = ref(null);
  const timer = ref(null);
  const loading = ref(false);
  const query = ref({});
  const submitBtnText = ref([t('清零'), t('去皮'), t('称量打码')]);
  const weighingMachineValue = reactive({
    tareWeight: '', // 皮重
    netWeight: '', // 净重
    grossWeight: '', // 毛重
    unitId: '', // 单位
  });

  // 称量结果数据弹窗
  const showWeighingResult = ref(false);

  // 称量结果数据
  const confirmDetailsData = ref([]);

  // 操作类型
  const operationType = ref(0); // 0清零 1去皮 2称量打码
  const isAuto = computed(() => {
    return query.value.mode === '0' || query.value.mode === '3';
  });
  const stepActive = computed(() => {
    return operationType.value === 2 || !isAuto.value ? 3 : 2;
  });

  const confirmText = computed(() => {
    return isAuto.value ? submitBtnText.value[operationType.value] : t('称量打码');
  });

  const socketParams = computed(() => {
    return {
      auto: isAuto.value,
      batchNo: detailData.value.storageMaterialBatchNo,
      productName: detailData.value.materialName,
    };
  });
  const formRef = ref();
  const formProps = reactive({
    schemas: [
      {
        field: 'tareWeight',
        component: 'Input',
        label: t('皮重'),
        colProps: {
          span: 6,
        },
        vIf: () => {
          return query.value.mode === '1';
        },
        componentProps: ({ formModel, formInstance }) => {
          return {
            type: 'number',
            onInput: () => {
              setNetWeight(formModel, formInstance);
            },
            onBlur: () => {
              formInstance.validate(['tareWeight']);
            },
          };
        },
        dynamicRules: () => {
          return [
            {
              validator: (val) => {
                return numberValidator(val, false);
              },
            },
          ];
        },
      },
      {
        field: 'grossWeight',
        component: 'Input',
        label: t('毛重'),
        colProps: {
          span: 6,
        },
        vIf: () => {
          return query.value.mode === '1';
        },
        componentProps: ({ formModel, formInstance }) => {
          return {
            type: 'number',
            onInput: () => {
              setNetWeight(formModel, formInstance);
            },
            onBlur: () => {
              formInstance.validate(['grossWeight']);
            },
          };
        },
        dynamicRules: ({ formModel }) => {
          return [
            {
              validator: (val) => {
                // 毛重需大于皮重
                if (Number(val) <= Number(formModel.tareWeight)) {
                  return Promise.reject(t('毛重需大于皮重'));
                }
                return numberValidator(val, false);
              },
            },
          ];
        },
      },
      {
        field: 'netWeight',
        component: 'Input',
        label: t('净重'),
        colProps: {
          span: 6,
        },
        vIf: () => {
          return query.value.mode === '1';
        },
        componentProps: {
          disabled: true,
          placeholder: t('计算回显'),
        },
      },
      {
        field: 'unitId',
        component: 'BMFormSelect',
        label: t('单位'),
        colProps: {
          span: 6,
        },
        vIf: () => {
          return query.value.mode === '1';
        },
        componentProps: ({ formModel }) => {
          return {
            request: async () => {
              let unitList = [];
              if (isEmpty(detailData.value?.materialId)) {
                return;
              }
              const { data } = await getMesUnitListDownExtendBound({
                materialId: detailData.value?.materialId,
              });
              const { data: materialDetail } = await reqProductMaterialDetail(detailData.value?.materialId);
              unitList = data.map(item => ({
                label: item.extendUnitName,
                id: item.id,
                expression: item.expression,
              }));

              unitList.unshift({
                label: materialDetail.unitName,
                id: materialDetail.unitId,
                isUnit: true,
                expression: t('标准单位'),
              });
              return unitList;
            },
            title: t('选择单位'),
            fieldNames: {
              label: 'label',
              value: 'id',
            },
            subLabel: 'expression',
            onConfirm: (data) => {
              if (data.isUnit) {
                formModel.unitExtendId = '';
              }
              else {
                formModel.unitExtendId = data.id;
              }
            },
            onClear: () => {
              formModel.unitExtendId = '';
              formModel.unitId = '';
            },
          };
        },
        dynamicRules: () => {
          return [
            {
              validator: (val) => {
                if (!val)
                  return Promise.reject(t('请选择单位'));

                return Promise.resolve();
              },
            },
          ];
        },
      },
    ],
  });
  const {
    weightInfo,
    isBackMessage,
    getReadings,
    clearZero,
    removePeel,
    clearZeroMessage,
    removePeelMessage,
  } = useBalanceSocket(socketParams);
  const weighingMachineProps = computed(() => {
    return {
      weighingName: t('增重称量'),
      weight: weightInfo.weight,
      actionNumber: operationType.value,
      mode: query.value.mode,
      unitId: detailData.value.unitId,
    };
  });

  // 称量结果表格
  const resultTableProps = computed(() => {
    return {
      pagination: false,
      data: confirmDetailsData.value,
      border: true,
      tableColProps: weighingMachineOutputColumns,
    };
  });
  const onPositionScanSelect = async () => {
    try {
      const res = await getStorageConfigTreeApi();
      treeStorageData.value = res.data;
      showStorageModal.value = true;
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  const confirmStorage = (data) => {
    storage.value = data.name || '';
    storageId.value = data.id || '';
  };

  function setNetWeight(formModel, formInstance) {
    const { tareWeight, grossWeight } = formModel;
    if (grossWeight && tareWeight) {
      const a = math.bignumber(formModel.tareWeight);
      const b = math.bignumber(formModel.grossWeight);
      const c = math.subtract(b, a);
      formInstance.setFormModel('netWeight', c.toString());
    }
    else {
      formInstance.setFormModel('netWeight', '');
    }
  }

  // 称量打码
  const weighAndPrint = async () => {
    // 判断是否是正数
    if (isAuto.value && !math.isPositive(Number(weighingMachineValue.netWeight))) {
      showNotify({
        message: '称量结果必须大于0',
        type: 'danger',
      });
      return;
    }

    // 校验称量人是否与当前登录人相同
    const currentUser = getStorageSync(USER_INFO) || {};
    const { userId } = currentUser;
    if (userId !== storeSignValue.value.userId1) {
      showNotify({
        message: t('登录账号与称量人不符'),
        type: 'danger',
      });
      return;
    }
    const device = bmosPrinterInstance.value.print();
    if (device) {
      await handleWeighPrint();
      if (confirmDetailsData.value.length > 0) {
        confirmDetailsData.value.forEach((item) => {
          reqPrintStorageMaterialTagApi({
            deviceId: device.id,
            sceneId: detailData.value?.categoryType === '0' ? 121001012 : 121002016,
            body: {
              no: item.storageMaterialNo,
            },
          });
        });
      }
    }
  };

  // 初始化数据状态
  const initDataStatus = () => {
    operationType.value = 0;
    weighingMachineValue.grossWeight = '';
    weighingMachineValue.netWeight = '';
    weighingMachineValue.tareWeight = '';
    container.value = '';
    storage.value = '';
    if (!isAuto.value) {
      formRef.value.setFormModels({
        tareWeight: '',
        grossWeight: '',
        netWeight: '',
        size: 1,
        unitId: '',
        quantity: '',
      });
    }
    if (query.value.mode === '1') {
      formRef.value.setFormModels({
        unitId: detailData.value.unitExtendId ? detailData.value.unitExtendId : detailData.value.unitId,
      });
    }
  };
  // 调用称量打码接口
  async function handleWeighPrint() {
    loading.value = true;
    const data = {
      batchNo: detailData.value.relevanceMaterialBatchNo,
      containerId: containerId.value,
      deviceId: selectedBalance.value?.balanceId || undefined,
      expiredDate: detailData.value.expiredDate,
      grossWeight: weighingMachineValue.grossWeight,
      materialId: detailData.value.materialId,
      materialPositionId: storageId.value,
      netWeight: weighingMachineValue.netWeight,
      productId: detailData.value.relevanceMaterialId,
      reCheckerId: storeSignValue.value.userId2,
      storageMaterialBatchNo: detailData.value.storageMaterialBatchNo,
      tareWeight: weighingMachineValue.tareWeight,
      unitId: query.value.mode === '1' ? weighingMachineValue.unitId : selectedBalance.value.unitId,
      weighMode: query.value.mode === '0' ? 1 : 2,
      weigherId: storeSignValue.value.userId1,
    };
    try {
      const res = await reqFreeWeightWeighAndPrint(data);
      confirmDetailsData.value = [res.data];
      initDataStatus();
      showWeighingResult.value = true;
    }
    catch (error) {
      error.message && showNotify({
        type: 'danger',
        message: error.message,
      });
    }
    loading.value = false;
  };

  // 继续称量
  const continueWeighing = () => {
    showWeighingResult.value = false;
    initDataStatus();
  };
  // 回到第一页
  const toConfirm = () => {
    uni.navigateTo({
      url: `/pages/materialWeighing/confirmOutputBatch/index?continue=1`,
    });
  };

  // 获取容器/货位信息
  const getContainerAndStorageInfo = async (type, code) => {
    if (code === '') {
      return;
    }
    try {
      if (type === 'storage') {
        const res = await scanWeighPositionCodeApi({
          code,
        });
        storage.value = `${res.data.fullName}`;
        storageId.value = res.data.id;
      }
      if (type === 'container') {
        const res = await scanWeighContainerCodeApi({
          code,
        });
        container.value = `${res.data.deviceCode} - ${res.data.deviceName}`;
        containerId.value = res.data.deviceId;
      }
    }
    catch (error) {
      error.message && showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  const onScanSuccess = (code, type) => {
    getContainerAndStorageInfo(type, code);
  };
  const onScanFail = () => {
    showNotify({
      type: 'danger',
      message: t('扫描失败'),
    });
  };
  const onScanComplete = () => {
    console.log('onScanComplete');
  };
  const onScanConfirm = (code, type) => {
    getContainerAndStorageInfo(type, code);
  };

  // 返回
  const toBack = () => {
    uni.navigateBack();
  };

  // 上一步
  const previousStep = () => {
    switch (operationType.value) {
      case 0:
        uni.navigateBack();
        break;
      case 1:
        operationType.value = 0;
        break;
      case 2:
        operationType.value = 0;
        weighingMachineValue.tareWeight = '';
        weighingMachineValue.netWeight = '';
        weighingMachineValue.grossWeight = '';
        break;
      default:
        break;
    }
  };

  // 下一步
  const submit = async () => {
    if (isAuto.value) {
      // 称量称量
      switch (operationType.value) {
        case 0:
          clearZeroMessage.value = false;
          setTimeout(() => {
            if (clearZeroMessage.value) {
              clearZero();
              operationType.value = 1;
            }
            else {
              showNotify({
                message: t('秤具连接异常'),
                type: 'danger',
              });
            }
          }, 250);
          break;
        case 1:
          removePeelMessage.value = false;
          setTimeout(() => {
            if (removePeelMessage.value) {
              if (weightInfo.weight < 0) {
                showNotify({
                  message: t('称量值为负, 去皮失败'),
                  type: 'danger',
                });
                return;
              }
              removePeel();
              operationType.value = 2;
              weighingMachineValue.tareWeight = weightInfo.weight;
            }
            else {
              showNotify({
                message: t('秤具连接异常'),
                type: 'danger',
              });
            }
          }, 250);
          break;
        case 2:
          await weighAndPrint();
          break;
        default:
          break;
      }
    }
    else {
      const values = await formRef.value.validate();
      if (query.value.mode === '1') {
        weighingMachineValue.tareWeight = values.tareWeight;
        weighingMachineValue.grossWeight = values.grossWeight;
        weighingMachineValue.netWeight = values.netWeight;
        weighingMachineValue.unitId = values.unitId;
      }
      await weighAndPrint();
    }
  };

  onMounted(async () => {
    if (isAuto.value) {
      timer.value = setInterval(() => {
        if (weightInfo.open && isBackMessage.value) {
          getReadings();
        }
      }, 250);
    }
    if (query.value.mode === '1') {
      formRef.value.setFormModel('unit', detailData.value.unit);
      formRef.value.setFormModel('unitId', detailData.value.unitExtendId ? detailData.value.unitExtendId : detailData.value.unitId);
    }
  });
  onUnmounted(() => {
    if (timer.value) {
      clearInterval(timer.value);
      timer.value = null;
    }
  });

  // 是称量模式,且操作类型为2时
  watch(() => [weightInfo.weight, weighingMachineValue.tareWeight], () => {
    if (isAuto.value && operationType.value === 2) {
      weighingMachineValue.netWeight = weightInfo.weight;
      try {
        const a1 = math.bignumber(weighingMachineValue.netWeight);
        const e1 = math.bignumber(weighingMachineValue.tareWeight);
        weighingMachineValue.grossWeight = math.add(a1, e1).toString();
      }
      catch (_error) {
        weighingMachineValue.netWeight = '';
        weighingMachineValue.grossWeight = '';
      }
    }
  });
  // 监听storage的变化
  watch(() => storage.value, (newVal) => {
    if (!newVal) {
      storageId.value = '';
    }
  });
  // 监听container的变化
  watch(() => container.value, (newVal) => {
    if (!newVal) {
      containerId.value = '';
    }
  });
  return {
    loading,
    query,
    isAuto,
    stepList,
    stepActive,
    formRef,
    formProps,
    operationType,
    confirmText,
    detailData,
    weighingMachineValue,
    weighingMachineProps,
    bmosPrinterInstance,
    showWeighingResult,
    resultTableProps,
    container,
    storage,
    storageId,
    showStorageModal,
    treeStorageData,
    handleWeighPrint,
    continueWeighing,
    toConfirm,
    onPositionScanSelect,
    confirmStorage,
    onScanSuccess,
    onScanFail,
    onScanComplete,
    onScanConfirm,
    toBack,
    previousStep,
    submit,
  };
};
