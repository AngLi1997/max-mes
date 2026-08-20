import {
  getMesUnitListDownExtendBound,
  getOutputWeighProcessApi,
  getStorageConfigTreeApi,
  reqPrintStorageMaterialTagApi,
  scanWeighContainerCodeApi,
  scanWeighPositionCodeApi,
  weighAndPrintOutputApi,
} from '@/api';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { useOutputWeighingStore } from '@/stores/businessComponents/outputWeighing/index.js';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { throttle } from '@/utils/func.js';
import { numberValidator } from '@/utils/numberValidator.js';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { useBalanceSocket } from '@/utils/useBalanceSocket';
import { t } from '@/utils/useBmosI18n.js';
import { useMathJs } from '@/utils/useMathJs.js';
import { storeToRefs } from 'pinia';
import { computed, onMounted, onUnmounted, reactive, ref, watch } from 'vue';
import { useNotify } from 'wot-design-uni';
import { useConst } from '../../hooks/useConst.js';

export const useRemovePeel = () => {
  const { math } = useMathJs();
  const { showNotify } = useNotify();
  const outputWeighingStore = useOutputWeighingStore();
  const weighingMachineStore = useWeighingMachineStore();
  const { detailData } = storeToRefs(outputWeighingStore);
  const { selectedBalance } = storeToRefs(weighingMachineStore);
  const { setDetailData } = outputWeighingStore;
  const { stepList, manualOutputColumns, weighingMachineOutputColumns } = useConst();

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
  const submitBtnText = ref([t('清零'), t('去皮'), t('产出打码')]);
  const weighingMachineValue = reactive({
    tareWeight: '', // 皮重
    netWeight: '', // 净重
    grossWeight: '', // 毛重
  });

  // 称量结果数据弹窗
  const showWeighingResult = ref(false);

  // 称量结果数据
  const confirmDetailsData = ref([]);

  // 单位选项
  const unitOptions = ref([]);

  // 操作类型
  const operationType = ref(0); // 0清零 1去皮 2产出打码
  const isAuto = computed(() => {
    return query.value.mode === '0' || query.value.mode === '3';
  });
  const stepActive = computed(() => {
    return operationType.value === 2 || !isAuto.value ? 3 : 2;
  });

  const confirmText = computed(() => {
    return isAuto.value ? submitBtnText.value[operationType.value] : t('产出打码');
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
          span: 7,
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
          };
        },
        dynamicRules: () => {
          return [
            {
              validator: (val) => {
                if (!val)
                  return Promise.reject(t('请输入皮重'));
                if (Number(val) < 0) {
                  return Promise.reject(t('皮重不能为负数'));
                }
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
          span: 7,
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
          };
        },
        dynamicRules: ({ formModel }) => {
          return [
            {
              validator: (val) => {
                if (!val)
                  return Promise.reject(t('请输入毛重'));
                // 毛重需大于皮重
                if (Number(val) <= Number(formModel.tareWeight)) {
                  return Promise.reject(t('毛重需大于皮重'));
                }
                return numberValidator(val, true);
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
          span: 7,
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
        field: 'unit',
        component: 'Input',
        label: t('单位'),
        colProps: {
          span: 3,
        },
        vIf: () => {
          return query.value.mode === '1';
        },
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'quantity',
        component: 'Input',
        label: t('物料量'),
        colProps: {
          span: 14,
        },
        vIf: () => {
          return query.value.mode === '2';
        },
        componentProps: () => {
          return {
            type: 'number',
          };
        },
        dynamicRules: () => {
          return [
            {
              validator: (val) => {
                if (!val)
                  return Promise.reject(t('请输入物料量'));
                return numberValidator(val, true);
              },
            },
          ];
        },
      },
      {
        field: 'unitId',
        component: 'BMFormSelect',
        label: t('单位'),
        colProps: {
          span: 5,
        },
        vIf: () => {
          return query.value.mode === '2';
        },
        componentProps: () => {
          return {
            request: async () => {
              await getUnitOptions();
              return unitOptions.value;
            },
            title: t('选择单位'),
            fieldNames: {
              name: 'label',
              key: 'value',
            },
            subLabel: 'subLabel',
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
      {
        field: 'size',
        component: 'Input',
        label: t('产出件数'),
        colProps: {
          span: 5,
        },
        vIf: () => {
          return query.value.mode === '2';
        },
        componentProps: () => {
          return {
            type: 'number',
          };
        },
        dynamicRules: () => {
          return [
            {
              validator: (val) => {
                if (!val)
                  return Promise.reject(t('请输入产出件数'));

                // 产出件数正正数,最大数量99
                try {
                  if (!math.isInteger(val) || !math.isPositive(val) || !math.smallerEq(val, 99)) {
                    return Promise.reject(t('产出件数正整数,最大数量99'));
                  }
                }
                catch (error) {
                  return Promise.reject(t('请输入正确的产出件数'));
                }

                if (Number(val) > 1 && containerId.value) {
                  Promise.reject(t('容器只能绑定单件物料件，请多次产出'));
                }
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
      weighingName: query.value.mode === '0' ? t('产出称量') : t('扫码去皮'),
      weight: weightInfo.weight,
      actionNumber: operationType.value,
      mode: query.value.mode,
      unitId: detailData.value.unitId,
    };
  });
  const params = computed(() => {
    return {
      componentId: query.value.componentId,
      copyVersion: getCurrentCopyRecordItem().version,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      productPlanId: urlQueryRef.value.productPlanId,
    };
  });

  // 称量结果表格
  const resultTableProps = computed(() => {
    return {
      pagination: false,
      data: confirmDetailsData.value,
      border: true,
      tableColProps: query.value.mode === '2' ? manualOutputColumns : weighingMachineOutputColumns,
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

  // 查询称量详情
  const queryWeighDetail = async () => {
    try {
      const res = await getOutputWeighProcessApi({
        ...params.value,
      });
      setDetailData(res.data);
    }
    catch (error) {
      error.message && showNotify({
        message: error.message,
        type: 'danger',
      });
    }
  };

  // 产出打码
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
    if (userId !== detailData.value.weigherId) {
      showNotify({
        message: t('登录账号与产出人不符'),
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
            sceneId: 121002006,
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
  };
  // 调用产出打码接口
  async function handleWeighPrint() {
    loading.value = true;
    const data = {
      byPiece: query.value.mode === '2',
      containerId: containerId.value,
      grossWeight: weighingMachineValue.grossWeight,
      materialPositionId: storageId.value,
      netWeight: weighingMachineValue.netWeight,
      tareWeight: weighingMachineValue.tareWeight,
      unitId: detailData.value.unitId,
      weighMode: query.value.mode === '0' || query.value.mode === '3' ? 1 : 2,
      outputWeighProcessId: detailData.value.id,
      deviceId: selectedBalance.value?.balanceId || undefined,
    };
    if (query.value.mode === '2') {
      const values = await formRef.value.getFormValues();
      data.quantity = values.quantity;
      data.unitId = values.unitId;
      data.size = values.size;
    }
    try {
      const res = await weighAndPrintOutputApi(data);
      confirmDetailsData.value = res.data;
      initFillData2();
      await queryWeighDetail();
      initDataStatus();
      showWeighingResult.value = true;
    }
    catch (error) {
      error.message && uni.showToast({ title: error.message, icon: 'none' });
    }
    loading.value = false;
  };

  // 继续产出
  const continueWeighing = () => {
    showWeighingResult.value = false;
  };
  // 签名
  const toSign = () => {
    showWeighingResult.value = false;
    toResult();
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

  // 获取单位选项
  async function getUnitOptions() {
    try {
      const res = await getMesUnitListDownExtendBound({
        materialId: detailData.value.materialId,
      });
      unitOptions.value = res.data.map((item) => {
        return {
          label: item.extendUnitName,
          value: item.id,
          subLabel: item.expression,
        };
      });
      unitOptions.value.unshift({
        label: detailData.value.basicUnit,
        value: detailData.value.basicUnitId,
        subLabel: t('标准单位'),
      });
    }
    catch (error) {
      error.message && showNotify({
        message: error.message,
        type: 'danger',
      });
    }
  };

  // 返回
  const toBack = () => {
    uni.navigateBack();
  };
  // 跳转称量结果
  function toResult() {
    uni.navigateTo({
      url: `/pages/businessComponents/outputWeighing/result/index?componentId=${
        query.value.componentId
      }`,
    });
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

  const clearApi = throttle(() => {
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
  }, 300);

  const removePeelApi = throttle(() => {
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
  }, 300);

  // 下一步
  const submit = async () => {
    if (isAuto.value) {
      // 产出称量
      if (query.value.mode === '0') {
        switch (operationType.value) {
          case 0:
            clearApi();
            break;
          case 1:
            removePeelApi();
            break;
          case 2:
            await weighAndPrint();
            break;
          default:
            break;
        }
      }
      else if (query.value.mode === '3') {
      // 扫码去皮
        switch (operationType.value) {
          case 0:
            clearZeroMessage.value = false;
            setTimeout(() => {
              if (clearZeroMessage.value) {
                clearZero();
                operationType.value = 2;
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
            if (weighingMachineValue.tareWeight === '') {
              showNotify({
                message: t('请输入皮重'),
                type: 'danger',
              });
              return;
            }
            else {
              if (Number(weighingMachineValue.tareWeight) <= 0) {
                showNotify({
                  message: t('皮重必须大于0'),
                  type: 'danger',
                });
                return;
              }
            }
            await weighAndPrint();
            break;
          default:
            break;
        }
      }
    }
    else {
      const values = await formRef.value.validate();
      if (query.value.mode === '1') {
        weighingMachineValue.tareWeight = values.tareWeight;
        weighingMachineValue.grossWeight = values.grossWeight;
        weighingMachineValue.netWeight = values.netWeight;
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
    await queryWeighDetail();
    if (query.value.mode === '1') {
      formRef.value.setFormModel('unit', detailData.value.unit);
    }
    if (query.value.mode === '2') {
      formRef.value.setFormModel('size', 1);
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
      catch (error) {
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
    toSign,
    onPositionScanSelect,
    confirmStorage,
    onScanSuccess,
    onScanFail,
    onScanComplete,
    onScanConfirm,
    toBack,
    toResult,
    previousStep,
    submit,
  };
};
