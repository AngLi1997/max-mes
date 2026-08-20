import {
  getStorageConfigTreeApi,
  queryWeighCenterExecuteRequirementById,
  reqPrintStorageMaterialTagApi, // 打码
  reqScanMaterialApi, // 物料/容器
  weighCenterExecuteAddConsumeStorageMaterial,
  weighCenterExecuteChangeBatch,
  weighCenterExecuteFinish,
  weighCenterExecuteScanWeighContainerCodeApi, // 容器
  weighCenterExecuteScanWeighPositionCodeApi, // 货位
  weighCenterExecuteWeighingApi, // 称量
} from '@/api';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { goBackToTargetPath, throttle } from '@/utils/func.js';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { useBalanceSocket } from '@/utils/useBalanceSocket';
import { t } from '@/utils/useBmosI18n.js';
import { useMathJs } from '@/utils/useMathJs.js';
import { onHide, onShow } from '@dcloudio/uni-app';
import { storeToRefs } from 'pinia';
import { computed, onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdIcon from 'wot-design-uni/components/wd-icon/wd-icon.vue';

export const useMaterialWeighing = ({ props }) => {
  const weighingMachineStore = useWeighingMachineStore();
  const { selectedBalance } = storeToRefs(weighingMachineStore);
  // 打印实例
  const bmosPrinterInstance = ref(null);
  // 完成称量签名弹窗实例
  const completeWeighingSignModalRef = ref(null);
  // 更换物料批次签名弹窗实例
  const materialBatchSignModalRef = ref(null);
  // 称量打码loading
  const nextLoading = ref(false);
  const timer = ref(null);
  const { math } = useMathJs();
  const { showNotify } = useNotify();
  const showQuitModal = ref(false);
  // 0:清零 1:去皮 2:称量打码
  const actionNumber = ref(0);
  // 需求详情
  const requirementDetail = ref({});
  // 称量结果详情
  const weighingResultDetail = ref({});
  const resultTableRef = ref();
  const resultTableProps = reactive({
    pagination: false,
    data: [],
    border: true,
    tableColProps: [
      {
        prop: 'INDEX',
        label: t('序号'),
        width: 80,
      },
      {
        prop: 'storageMaterialBatchNo',
        label: t('物料批号'),
        width: 200,
      },
      {
        prop: 'storageMaterialNo',
        label: t('物料件号'),
        width: 200,
      },
      {
        prop: 'netWeight',
        label: t('净重'),
        width: 160,
      },
      {
        prop: 'tareWeight',
        label: t('皮重'),
        width: 160,
      },
      {
        prop: 'grossWeight',
        label: t('毛重'),
        width: 160,
      },

      {
        prop: 'unit',
        label: t('单位'),
        width: 100,
      },
      {
        prop: 'containerName',
        label: t('容器'),
        width: 254,
      },
      {
        prop: 'materialPositionName',
        label: t('暂存货位'),
        width: 254,
      },
    ],
  });
  const isFirst = ref(true);
  const resultConfirmText = computed(() => {
    if (weighingResultDetail.value.nextProcess?.value === 3 && requirementDetail.value.weighProcess?.value === 1) {
      return t('余料称量');
    }
    if (weighingResultDetail.value.nextProcess?.value === 2) {
      return t('更换物料需求');
    }
    return t('继续称量');
  });
  // 是否自动称量
  const auto = computed(() => props.mode === 'true');
  const socketParams = computed(() => {
    return {
      auto: auto.value,
      batchNo: requirementDetail.value.batchNo,
      productName: requirementDetail.value.productName,
    };
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
  const weighingMachineValue = reactive({
    tareWeight: '', // 皮重
    netWeight: '', // 净重
    grossWeight: '', // 毛重
  });

  const weighingMachineProps = computed(() => {
    const { unit, diff, unWeighedQuantity } = requirementDetail.value;
    return {
      auto: auto.value,
      actionNumber: actionNumber.value,
      weighingName:
        requirementDetail.value.weighProcess?.value === 1
          ? t('物料称量')
          : t('余料称量'),
      unit,
      weight: weightInfo.weight,
      targetAmount: unWeighedQuantity,
      remainingAmount: math.format(
        math.subtract(math.bignumber(unWeighedQuantity || 0), math.bignumber(weightInfo.weight || 0)),
      ),
      diff,
    };
  });
  const confirmButtonText = computed(() => {
    if (!auto.value) {
      return t('确定');
    }
    if (actionNumber.value === 0) {
      return t('清零');
    }
    else if (actionNumber.value === 1) {
      return t('去皮');
    }
    else if (actionNumber.value === 2) {
      return t('称量打码');
    }
    return '';
  });
  // 扫描参数
  // const scanMaterialParams = computed(() => {
  //   return {
  //     isAvailable: true,
  //     isOutbound: true,
  //     productPlanId: requirementDetail.value.productPlanId,
  //     formulaMaterialId: requirementDetail.value.formulaMaterialId,
  //     unitId: requirementDetail.value.unitId,
  //   };
  // });

  // 更换物料批次签名弹窗
  const showMaterialBatch = ref(false);
  const materialBatchSignValue = ref({
    userName1: '',
    loginName1: '',
    password1: '',
    userId1: '',
  });
  const materialBatchLabelList = [
    {
      label: t('账号'),
      // 签名动作
      signatureAction: 94,
      menuId: '121020001000007',
    },
  ];
  // 返回执行页面
  const backToExecution = () => {
    goBackToTargetPath('pages/weighingCenter/execution/index');
  };
  // 更换物料批次签名确认
  const materialBatchSignConfirm = async () => {
    try {
      await materialBatchSignModalRef.value?.checkSign();
      await weighCenterExecuteChangeBatch({
        requirementId: props.requirementId,
        changerId: materialBatchSignValue.value.userId1,
      });
      backToExecution();
    }
    catch (error) {
      if (error.data && error.data.failedIndex && error.data.failedIndex.length > 0) {
        return;
      }
      showNotify({
        message: error.message,
        type: 'danger',
      });
    }
  };

  // 直接完成称量签名弹窗
  const showCompleteWeighing = ref(false);
  const completeWeighingSignValue = ref({
    userName1: '',
    loginName1: '',
    password1: '',
    userId1: '',
  });
  const completeWeighingLabelList = computed(() => [
    {
      label: t('账号'),
      // 签名动作
      signatureAction:
        requirementDetail.value.weighProcess?.value === 1 ? 93 : 97,
      menuId:
        requirementDetail.value.weighProcess?.value === 1
          ? '121020001000004'
          : '121020001000005',
    },
  ]);

  // 余料称量签名弹窗
  const showResidualMaterialSign = ref(false);
  const residualMaterialSignValue = ref({
    userName1: '',
    loginName1: '',
    password1: '',
    userId1: '',
  });
  const residualMaterialLabelList = computed(() => [
    {
      label: t('签名人'),
      // 签名动作
      signatureAction: 96,
      menuId: '121020001000006',
    },
  ]);

  const completeWeighingSignConfirm = async () => {
    try {
      await completeWeighingSignModalRef.value?.checkSign();
      await weighCenterExecuteFinish({
        requirementId: props.requirementId,
        finisherId: completeWeighingSignValue.value.userId1,
      });
      backToExecution();
    }
    catch (error) {
      if (error.data && error.data.failedIndex && error.data.failedIndex.length > 0) {
        return;
      }
      showNotify({
        message: error.message,
        type: 'danger',
      });
    }
  };

  // 物料详情
  const showMaterialDetail = ref(false);

  // 添加物料
  const showMaterialAdd = ref(false);
  // 扫描物料件号
  const scanMaterialAddValue = ref('');
  const addMaterialTableRef = ref();
  const showDeleteModal = ref(false);
  const currentRow = ref(null);
  // 打开删除确认弹窗
  const openDeleteModal = row => () => {
    currentRow.value = row;
    showDeleteModal.value = true;
  };
  const addMaterialTableProps = reactive({
    pagination: false,
    data: [],
    border: true,
    tableColProps: [
      {
        prop: 'button',
        label: '',
        width: 72,
        customRender: ({ row }) => {
          return (
            <WdIcon
              name="shanchu"
              size="18.75rpx"
              class-prefix="bmos-app-icon"
              color="var(--bmos-color-error)"
              onClick={openDeleteModal(row)}
            />
          );
        },
      },
      {
        prop: 'materialBatchNo',
        label: t('物料批号'),
        width: 220,
      },
      {
        prop: 'materialNo',
        label: t('物料件号'),
        width: 220,
      },
      {
        prop: 'quantity',
        label: t('物料量'),
        width: 180,
      },
      {
        prop: 'unit',
        label: t('单位'),
        width: 90,
      },
      {
        prop: 'expiredDate',
        label: t('有效期至'),
        width: 150,
      },
      {
        prop: 'factoryBatchNo',
        label: t('原厂批号'),
        width: 220,
      },
      {
        prop: 'supplier',
        label: t('供应商'),
        width: 300,
      },
      {
        prop: 'producer',
        label: t('生产商'),
        width: 300,
      },
    ],
  });
  // 扫描容器
  const scanContainerValue = ref('');
  const containerId = ref('');
  // 扫描货位
  const scanPositionValue = ref('');
  const positionId = ref('');
  const showPositionModal = ref(false);
  const treePositionData = ref([]);

  // 删除表格行
  const deleteMaterialConfirm = () => {
    const index = addMaterialTableProps.data.findIndex(
      item => item.id === currentRow.value.id,
    );
    addMaterialTableProps.data.splice(index, 1);
    showDeleteModal.value = false;
    currentRow.value = null;
  };

  // 称量结果
  const showWeighingResult = ref(false);

  // 更换批次
  const changeBatch = () => {
    showMaterialBatch.value = true;
  };
  // 添加物料
  const addMaterial = () => {
    addMaterialTableProps.data = [];
    scanMaterialAddValue.value = '';
    showMaterialAdd.value = true;
  };
  // 添加物料件
  const handleAddMaterial = async () => {
    try {
      const params = {
        consumeStorateMaterialIdList: addMaterialTableProps.data.map(
          item => item.id,
        ),
        requirementId: requirementDetail.value.id,
        // 是否余料称量
        isResidual: requirementDetail.value.weighProcess?.value === 3,
      };
      if (params.consumeStorateMaterialIdList.length === 0) {
        return;
      }
      await weighCenterExecuteAddConsumeStorageMaterial(params);
      getWeighCenterExecuteRequirementById({ init: false });
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  // 添加物料确认
  const materialAddConfirm = async () => {
    handleAddMaterial();
    showMaterialAdd.value = false;
  };

  // 打开物料详情
  const toMaterialDetail = () => {
    showMaterialDetail.value = true;
  };

  const handleFinishWeighing = () => {
    showCompleteWeighing.value = true;
  };

  const infoItems = [
    {
      label: t('物料总量'),
      field: 'batchConsumeTotalQuantityUnit',
      type: 'text',
    },
    {
      label: t('物料剩余量'),
      field: 'remainingQuantityUnit',
      type: 'text',
    },
    { label: t('更换批次'), type: 'button', click: changeBatch },
    { label: t('添加物料'), type: 'button', click: addMaterial },
    { label: t('详情'), type: 'button', click: toMaterialDetail },
  ];

  const dataInfoItems = [
    {
      label: t('生产批号'),
      field: 'batchNo',
      type: 'text',
    },
    {
      label: t('目标量'),
      field: 'targetTotalQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
    {
      label: t('已称量'),
      field: 'weighedQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
    {
      label: t('未称量'),
      field: 'unWeighedQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
    { label: t('完成称量'), type: 'button', click: handleFinishWeighing },
  ];

  const resultDetailDataInfoItems = [
    {
      label: t('目标量'),
      field: 'targetQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
    {
      label: t('已称量'),
      field: 'weighedQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
    {
      label: t('未称量'),
      field: 'unWeighedQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
  ];
  // 获取需求详情
  const getWeighCenterExecuteRequirementById = async (data = { init: true }) => {
    if (data.init) {
      positionId.value = '';
      scanPositionValue.value = '';
      containerId.value = '';
      scanContainerValue.value = '';
      actionNumber.value = 0;
      weighingMachineValue.tareWeight = '';
      weighingMachineValue.netWeight = '';
      weighingMachineValue.grossWeight = '';
    }
    const res = await queryWeighCenterExecuteRequirementById({
      requirementId: props.requirementId,
    });
    requirementDetail.value = res.data;
    const {
      batchConsumeTotalQuantity,
      remainingQuantity,
      targetTotalQuantity,
      weighedQuantity,
      unWeighedQuantity,
      unit,
      materialName,
      materialMergeCode,
    } = res.data;

    // 物料信息
    requirementDetail.value.materialInfo = `${materialMergeCode}-${materialName}`;
    // 物料总量
    requirementDetail.value.batchConsumeTotalQuantityUnit = batchConsumeTotalQuantity
      ? `${batchConsumeTotalQuantity}${unit}`
      : '';
    // 物料剩余量
    requirementDetail.value.remainingQuantityUnit = remainingQuantity
      ? `${remainingQuantity}${unit}`
      : '';
    // 目标量
    requirementDetail.value.targetTotalQuantityUnit = targetTotalQuantity
      ? `${targetTotalQuantity}${unit}`
      : '';
    // 已称量
    requirementDetail.value.weighedQuantityUnit = weighedQuantity
      ? `${weighedQuantity}${unit}`
      : '';
    // 未称量
    requirementDetail.value.unWeighedQuantityUnit = unWeighedQuantity
      ? `${unWeighedQuantity}${unit}`
      : '';

    // 调用接口后，修改标识
    isFirst.value = false;
  };
  const weighingCenterExecuteParams = computed(() => {
    return {
      containerId: containerId.value,
      deviceId: selectedBalance.value.balanceId,
      grossWeight: weighingMachineValue.grossWeight,
      materialPositionId: positionId.value,
      netWeight: weighingMachineValue.netWeight,
      requirementId: props.requirementId,
      tareWeight: weighingMachineValue.tareWeight,
      unitId: requirementDetail.value.unitId,
      weighMode: auto.value ? 1 : 2,
      weighProcess: requirementDetail.value?.weighProcess?.value,
    };
  });
  const handleWeigh = async () => {
    nextLoading.value = true;
    try {
      const res = await weighCenterExecuteWeighingApi(weighingCenterExecuteParams.value);
      weighingResultDetail.value = res.data;
      const {
        targetQuantity,
        weighedQuantity,
        unWeighedQuantity,
        unit,
        resultItemList,
      } = res.data;
      // 目标量
      weighingResultDetail.value.targetQuantityUnit = targetQuantity
        ? `${targetQuantity}${unit}`
        : '';
      // 已称量
      weighingResultDetail.value.weighedQuantityUnit = weighedQuantity
        ? `${weighedQuantity}${unit}`
        : '';
      // 未称量
      weighingResultDetail.value.unWeighedQuantityUnit = unWeighedQuantity
        ? `${unWeighedQuantity}${unit}`
        : '';
      // 称量结果列表
      resultTableProps.data = resultItemList;
      showWeighingResult.value = true;
      return true;
    }
    catch (error) {
      showNotify({
        message: error.message,
        type: 'danger',
      });
      return false;
    }
    finally {
      nextLoading.value = false;
    }
  };
  // 称量打码
  const weighingPrint = async (skipVerification = false) => {
    if (!auto.value) {
      if (
        Number.isNaN(Number(weighingMachineValue.tareWeight))
        || !weighingMachineValue.tareWeight
      ) {
        showNotify({
          message: t('请输入正确的皮重'),
          type: 'danger',
        });
        return;
      }
      else {
        if (Number(weighingMachineValue.tareWeight) < 0) {
          showNotify({
            message: t('皮重不能小于0'),
            type: 'danger',
          });
          return;
        }
      }
      if (Number.isNaN(Number(weighingMachineValue.grossWeight))) {
        showNotify({
          message: t('请输入正确的毛重'),
          type: 'danger',
        });
        return;
      }
      else {
        if (Number(weighingMachineValue.grossWeight) < 0) {
          showNotify({
            message: t('毛重不能小于0'),
            type: 'danger',
          });
          return;
        }
      }
      // 毛重需大于皮重
      if (
        Number(weighingMachineValue.grossWeight)
        <= Number(weighingMachineValue.tareWeight)
      ) {
        showNotify({
          message: t('毛重需大于皮重'),
          type: 'danger',
        });
        return;
      }
    }
    if (!skipVerification) {
      // 物料称量且称量结果大于目标量范围
      const max
        = requirementDetail.value.diff.toleranceDiff[2]
        || requirementDetail.value.diff.toleranceDiff[1];
      if (
        requirementDetail.value.weighProcess?.value === 1
        && Number(weighingMachineValue.netWeight) > Number(max)
      ) {
        showNotify({
          message: t('超出批次目标量范围'),
          type: 'danger',
        });
        return;
      }
      // 余料称量且称量结果大于目标量范围,签名弹窗
      if (
        requirementDetail.value.weighProcess?.value === 3
        && Number(weighingMachineValue.netWeight) > Number(max)
      ) {
        showResidualMaterialSign.value = true;
        return;
      }
      if (Number(weighingMachineValue.netWeight) <= 0) {
        showNotify({
          message: t('称量结果必须大于0'),
          type: 'danger',
        });
        return;
      }

      // 校验当前登录人和称量人是否一致
      const currentUser = getStorageSync(USER_INFO) || {};
      const { userId } = currentUser;
      if (requirementDetail.value.weigherId !== userId) {
        showNotify({
          message: t('登录账号与称量人不符'),
          type: 'danger',
        });
        return;
      }
    }

    const device = bmosPrinterInstance.value.print();
    if (device) {
      try {
        const res = await handleWeigh();
        // 称量成功后打码
        if (res) {
          await reqPrintStorageMaterialTagApi({
            deviceId: device.id,
            sceneId:
              requirementDetail.value.categoryType.value === 0
                ? 121001008
                : 121002012,
            body: {
              no: weighingResultDetail.value.no,
            },
          });
        }
      }
      catch (error) {
        showNotify({
          type: 'danger',
          message: error.message,
        });
      }
    }
  };

  // 余料称量签名成功
  const residualMaterialSignConfirm = async () => {
    showResidualMaterialSign.value = false;
    weighingPrint(true);
  };

  // 上一步
  const handlePreviousStep = () => {
    if (auto.value) {
      if (actionNumber.value === 0) {
        uni.navigateBack();
        return;
      }
      actionNumber.value = 0;
      weighingMachineValue.tareWeight = '';
      weighingMachineValue.netWeight = '';
      weighingMachineValue.grossWeight = '';
    }
    else {
      uni.navigateBack();
    }
  };
  const clearApi = throttle(() => {
    clearZeroMessage.value = false;
    setTimeout(() => {
      if (clearZeroMessage.value) {
        clearZero();
        actionNumber.value = 1;
      }
      else {
        showNotify({
          type: 'danger',
          message: t('秤具连接异常'),
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
        weighingMachineValue.tareWeight = weightInfo.weight;
        removePeel();
        actionNumber.value = 2;
      }
      else {
        uni.showToast({
          title: t('秤具连接异常'),
          icon: 'none',
        });
      }
    }, 250);
  }, 300);

  // 下一步
  const handleNextStep = () => {
    if (requirementDetail.value.weighProcess?.value === 4) {
      showNotify({
        type: 'danger',
        message: t('已完成称量'),
      });
      return;
    }
    if (auto.value) {
      switch (actionNumber.value) {
        case 0:
          clearApi();
          break;
        case 1:
          removePeelApi();
          break;
        case 2:
          // 称量打码
          weighingPrint();
          break;
        default:
      }
    }
    else {
      weighingPrint();
    }
  };

  const toBack = () => {
    uni.navigateBack();
  };

  const toResult = () => {
    uni.navigateTo({
      url: `/pages/weighingCenter/result/index?id=${props.id}`,
    });
  };

  // 扫描物料件/设备号查询物料件信息
  const handleMaterialScan = async (code) => {
    try {
      const res = await reqScanMaterialApi({
        no: code,
        productPlanId: requirementDetail.value.productPlanId,
        storageMaterialBatchId: requirementDetail.value.storageMaterialBatchId,
      });
      // if (!res.data) {
      //   showNotify({
      //     type: 'danger',
      //     message: t('物料件号不存在'),
      //   });
      //   return;
      // }
      // if (res.data?.batchId) {
      //   showNotify({
      //     type: 'danger',
      //     message: t('已预定生产批次,请选择未预定的物料件'),
      //   });
      //   return;
      // }
      // if (res.data?.materialId !== requirementDetail.value.materialId) {
      //   showNotify({
      //     type: 'danger',
      //     message: t('无法添加不符合称量需求的物料'),
      //   });
      //   return;
      // }
      // if (
      //   requirementDetail.value.storageMaterialBatchId
      //   !== res.data.materialBatchId
      // ) {
      //   showNotify({
      //     type: 'danger',
      //     message: t('物料件不是同一批次'),
      //   });
      //   return;
      // }
      // if (new Date(res.data?.expiredDate) < new Date(new Date().toISOString().split('T')[0])) {
      //   showNotify({
      //     type: 'danger',
      //     message: t('物料件已超过有效期'),
      //   });
      //   return;
      // }
      // if (res.data?.materialPositionId) {
      //   showNotify({
      //     type: 'danger',
      //     message: t('物料件未出库'),
      //   });
      //   return;
      // }
      // if (res.data?.qualityStatus?.value !== 'QUALIFIED' && res.data?.qualityStatus?.value !== 'RESTRICTED_RELEASE') {
      //   showNotify({
      //     type: 'danger',
      //     message: `物料批次处于${res.data?.qualityStatus?.name}状态,无法使用`,
      //   });
      //   return;
      // }
      if (addMaterialTableProps.data.some(item => item.id === res.data.id)) {
        showNotify({
          type: 'danger',
          message: t('物料件已添加，不能重复添加'),
        });
        return;
      }
      addMaterialTableProps.data.push(res.data);
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  // 扫描容器
  const handleContainerScan = async (code) => {
    try {
      const res = await weighCenterExecuteScanWeighContainerCodeApi({
        code,
      });
      scanContainerValue.value = `${res.data.deviceCode} - ${
        res.data.deviceName
      }`;
      containerId.value = res.data.deviceId;
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  // 扫描货位
  const handlePositionScan = async (code) => {
    try {
      const res = await weighCenterExecuteScanWeighPositionCodeApi({
        code,
      });
      scanPositionValue.value = `${res.data.fullName}`;
      positionId.value = res.data.id;
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  // 扫描
  const handleScan = async (code, type) => {
    switch (type) {
      // 物料/容器
      case 'addMaterial':
        handleMaterialScan(code);
        break;
      // 容器
      case 'container':
        handleContainerScan(code);
        break;
      // 货位
      case 'position':
        handlePositionScan(code);
        break;
      default:
        break;
    }
  };
  const onScanSuccess = (code, type) => {
    handleScan(code, type);
  };
  const onScanFail = () => {
    showNotify({
      type: 'danger',
      message: t('扫描失败'),
    });
  };
  const onScanComplete = (result) => {
    console.log('onScanComplete', result);
  };
  const onScanConfirm = (code, type) => {
    handleScan(code, type);
  };

  // 货位扫描选择
  const onPositionScanSelect = async () => {
    try {
      const res = await getStorageConfigTreeApi();
      treePositionData.value = res.data;
      showPositionModal.value = true;
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  // 货位确认
  const confirmPosition = (data) => {
    if (data) {
      positionId.value = data.id;
      scanPositionValue.value = data.name;
    }
    else {
      positionId.value = '';
      scanPositionValue.value = '';
    }
  };
  const handleResultCancel = () => {
    toResult();
  };
  const handleResultConfirm = () => {
    showWeighingResult.value = false;
    if (weighingResultDetail.value.nextProcess?.value === 2) {
      backToExecution();
    }
    getWeighCenterExecuteRequirementById();
  };

  // 退出称量
  const exitWeighing = () => {
    goBackToTargetPath('pages/weighingCenter/list/index');
  };
  onMounted(() => {
    getWeighCenterExecuteRequirementById();
  });
  onShow(() => {
    if (!isFirst.value) {
      getWeighCenterExecuteRequirementById({ init: false });
    }
    timer.value = setInterval(() => {
      if (weightInfo.open && isBackMessage.value) {
        getReadings();
        if (actionNumber.value === 2) {
          weighingMachineValue.grossWeight = math.format(
            math.sum(math.bignumber(weightInfo.weight), math.bignumber(weighingMachineValue.tareWeight)),
          );
          weighingMachineValue.netWeight = weightInfo.weight;
        }
        else {
          weighingMachineValue.grossWeight = 0;
        }
      }
    }, 200);
  });
  onHide(() => {
    if (timer.value) {
      clearInterval(timer.value);
      timer.value = null;
    }
  });
  return {
    nextLoading,
    weighingCenterExecuteParams,
    completeWeighingSignModalRef,
    materialBatchSignModalRef,
    showQuitModal,
    weighingMachineValue,
    weighingMachineProps,
    actionNumber,
    confirmButtonText,
    infoItems,
    dataInfoItems,
    requirementDetail,
    showMaterialBatch,
    materialBatchSignValue,
    materialBatchLabelList,
    materialBatchSignConfirm,
    showCompleteWeighing,
    completeWeighingSignValue,
    completeWeighingLabelList,
    completeWeighingSignConfirm,
    showResidualMaterialSign,
    residualMaterialSignValue,
    residualMaterialLabelList,
    residualMaterialSignConfirm,
    showMaterialDetail,
    showMaterialAdd,
    scanMaterialAddValue,
    addMaterialTableRef,
    addMaterialTableProps,
    showWeighingResult,
    showDeleteModal,
    scanContainerValue,
    scanPositionValue,
    positionId,
    showPositionModal,
    treePositionData,
    bmosPrinterInstance,
    weighingResultDetail,
    resultDetailDataInfoItems,
    resultTableRef,
    resultTableProps,
    resultConfirmText,
    toBack,
    toResult,
    handlePreviousStep,
    handleNextStep,
    toMaterialDetail,
    handleFinishWeighing,
    deleteMaterialConfirm,
    onScanSuccess,
    onScanFail,
    onScanComplete,
    onScanConfirm,
    materialAddConfirm,
    onPositionScanSelect,
    confirmPosition,
    handleWeigh,
    handleResultCancel,
    handleResultConfirm,
    exitWeighing,
  };
};
