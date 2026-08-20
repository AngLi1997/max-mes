import {
  addMeasureConsumeStorageMaterialApi,
  completeMeasureApi,
  getStorageConfigTreeApi,
  measureAndPrintApi,
  queryMeasureBatchDetailApi,
  reqPrintStorageMaterialTagApi, // 打码
  scanStorageMaterialApi,
  scanWeighContainerCodeApi,
  scanWeighPositionCodeApi,
} from '@/api';
import { initFillData2 } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { goBackToTargetPath } from '@/utils/func.js';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { onShow } from '@dcloudio/uni-app';
import { computed, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdIcon from 'wot-design-uni/components/wd-icon/wd-icon.vue';

export const useLiquidMeasure = ({ props }) => {
  // 打印实例
  const bmosPrinterInstance = ref(null);
  const { showNotify } = useNotify();

  const confirmButtonText = computed(() => {
    return t('量取打码');
  });

  // 量取批次详细信息
  const measureBatchDetail = ref({});
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
        prop: 'storageMaterialNo',
        label: t('物料件号'),
        width: 160,
      },
      {
        prop: 'measureQuantity',
        label: t('物料量'),
        width: 160,
      },
      {
        prop: 'unitName',
        label: t('单位'),
        width: 80,
      },
      {
        prop: 'containerName',
        label: t('容器'),
        width: 334,
      },
      {
        prop: 'materialPositionName',
        label: t('暂存货位'),
        width: 334,
      },
    ],
  });
  const resultConfirmText = computed(() => {
    if (weighingResultDetail.value.nextMeasureStage?.value === 'COMPLETED') {
      return t('更换物料批次');
    }
    if (weighingResultDetail.value.nextMeasureStage?.value === 'MEASURING' && measureBatchDetail.value.measureStage?.value === 'UNMEASURED') {
      return t('余料称量');
    }
    return t('继续称量');
  });

  const weighingMachineValue = reactive({
    tareWeight: '',
    netWeight: '',
    grossWeight: '',
  });

  // 是否自动称量
  const auto = computed(() => props.mode === 'true');

  const weighingMachineProps = computed(() => {
    const {
      unitName,
      toleranceDiff,
      measureStage,
      toleranceType,
      toleranceLower,
      toleranceUpper,
    } = measureBatchDetail.value;
    return {
      auto: auto.value,
      weighingName: measureStage?.label,
      unit: unitName,
      diff: {
        maxTolerance: toleranceUpper,
        minTolerance: toleranceLower,
        toleranceDiff,
        toleranceTypeEnum: toleranceType,
      },
      isLiquidMeasure: true,
    };
  });

  // 称量结果
  const showWeighingResult = ref(false);
  // 扫描物料件号
  const scanMaterialAddValue = ref('');
  const addMaterialTableRef = ref();
  const addMaterialTableProps = reactive({
    pagination: false,
    data: [],
    border: true,
    tableColProps: [
      {
        prop: 'button',
        label: '',
        width: 50,
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
        prop: 'materialNo',
        label: t('物料件号'),
        width: 240,
      },
      {
        prop: 'ACTION',
        label: '物料详情',
        width: 100,
        actions: ({ row }) => {
          return [
            {
              label: '查看',
              onClick: () => {
                currentRow.value = row;
                openBatchDetail.value = true;
              },
            },
          ];
        },
      },
      {
        prop: 'materialQuantity',
        label: t('物料量'),
        width: 240,
      },
      {
        prop: 'unitName',
        label: t('单位'),
        width: 100,
      },
      {
        prop: 'expiredDate',
        label: t('有效期至'),
        width: 142,
      },
      {
        prop: 'originalBatchNo',
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

  // 添加物料
  const showMaterialAdd = ref(false);
  // 删除物料
  const showDeleteModal = ref(false);
  // 物料详情
  const openBatchDetail = ref(false);
  const currentRow = ref(null);
  // 打开删除确认弹窗
  const openDeleteModal = row => () => {
    currentRow.value = row;
    showDeleteModal.value = true;
  };
  // 删除表格行
  const deleteMaterialConfirm = () => {
    const index = addMaterialTableProps.data.findIndex(
      item => item.id === currentRow.value.id,
    );
    addMaterialTableProps.data.splice(index, 1);
    showDeleteModal.value = false;
    currentRow.value = null;
  };
  const addMaterial = () => {
    addMaterialTableProps.data = [];
    scanMaterialAddValue.value = '';
    showMaterialAdd.value = true;
  };

  // 扫描容器
  const scanContainerValue = ref('');
  const containerId = ref('');
  // 扫描货位
  const scanPositionValue = ref('');
  const positionId = ref('');
  const showPositionModal = ref(false);
  const treePositionData = ref([]);

  // 直接完成称量签名弹窗
  const showCompleteWeighing = ref(false);
  const completeWeighingSignValue = ref({
    userName1: '',
    loginName1: '',
    password1: '',
    userId1: '',
  });
    // 余液量量取签名弹窗
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
      signatureAction: 128,
      menuId: '121010001002019',
    },
  ]);
  const completeWeighingLabelList = computed(() => [
    {
      label: t('账号'),
      // 签名动作
      signatureAction:
        measureBatchDetail.value.measureStage?.value === 'UNMEASURED'
          ? 101
          : 102,
      menuId:
        measureBatchDetail.value.measureStage?.value === 'UNMEASURED'
          ? '121010001002005'
          : '121010001002006',
    },
  ]);

  // 余液量取签名成功
  const residualMaterialSignConfirm = async () => {
    showResidualMaterialSign.value = false;
    weighingPrint(true);
  };
  // 完成量取
  const handleFinishMeasure = () => {
    showCompleteWeighing.value = true;
  };

  // 完成量取签名对象
  const completeWeighingParams = computed(() => {
    return {
      measureBatchId: props.measureBatchId,
      completeUserId: completeWeighingSignValue.value.userId1,
    };
  });
  // 完成配液称量签名确认
  const completeWeighingSignConfirm = async () => {
    try {
      await completeMeasureApi(completeWeighingParams.value);
      showCompleteWeighing.value = false;
      initFillData2();
      await queryMeasureBatchDetail();
      if (measureBatchDetail.value.measureStage.value === 'COMPLETED') {
        goBackToTargetPath('pages/businessComponents/liquidMeasure/index');
      }
    }
    catch (error) {
      showNotify({
        message: error.message,
        type: 'danger',
      });
    }
  };

  const infoItems = [
    {
      label: t('物料信息'),
      field: ['materialMergeCode', 'materialName'],
      type: 'text',
    },
    {
      label: t('物料批号'),
      field: 'materialBatchNo',
      type: 'text',
    },

    { label: t('添加物料'), type: 'button', click: addMaterial },
  ];

  const dataInfoItems = [
    {
      label: t('物料总量'),
      field: 'totalQuantityUnit',
      type: 'text',
    },
    {
      label: t('目标量'),
      field: 'targetQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
    {
      label: t('已量取'),
      field: 'measuredQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
    {
      label: t('未量取'),
      field: 'unmeasuredQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
    { label: t('完成量取'), type: 'button', click: handleFinishMeasure },
  ];
  const resultDataInfoItems = [
    {
      label: t('物料总量'),
      field: 'quantityUnit',
      type: 'text',
    },
    {
      label: t('目标量'),
      field: 'targetQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
    {
      label: t('已量取'),
      field: 'measuredQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
    {
      label: t('未量取'),
      field: 'unmeasuredQuantityUnit',
      type: 'text',
      color: 'var(--bmos-color-warning)',
    },
  ];
  // 扫描参数
  const scanParams = computed(() => {
    return {
      id: measureBatchDetail.value.instanceId,
      materialBatchId: measureBatchDetail.value.materialBatchId,
    };
  });
  // 扫描物料件/设备号查询物料件信息
  const handleMaterialScan = async (code) => {
    // 有生产计划id，说明选择了物料需求
    try {
      const res = await scanStorageMaterialApi({
        ...scanParams.value,
        code,
      });
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
      const res = await scanWeighContainerCodeApi({
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
      const res = await scanWeighPositionCodeApi({
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
  const onScanClear = (_code, type) => {
    if (type === 'container') {
      containerId.value = '';
      scanContainerValue.value = '';
    }
    else if (type === 'position') {
      positionId.value = '';
      scanPositionValue.value = '';
    }
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

  const measureParams = computed(() => {
    return {
      containerId: containerId.value,
      materialPositionId: positionId.value,
      measureBatchId: props.measureBatchId,
      measureMode: auto.value ? 'EQUIPMENT_MEASURE' : 'MANUAL_MEASURE',
      measureQuantity: weighingMachineValue.netWeight,
      unitId: measureBatchDetail.value.unitId,
      measureStage: measureBatchDetail.value.measureStage?.value,
    };
  });
  const handleWeigh = async () => {
    try {
      const res = await measureAndPrintApi(measureParams.value);
      weighingResultDetail.value = res.data;
      const {
        quantity,
        targetQuantity,
        measuredQuantity,
        unmeasuredQuantity,
        unitName,
        resultList,
      } = res.data;
      // 物料总量
      weighingResultDetail.value.quantityUnit = quantity
        ? `${quantity}${unitName}`
        : '';
      // 目标量
      weighingResultDetail.value.targetQuantityUnit = targetQuantity
        ? `${targetQuantity}${unitName}`
        : '';
      // 已量取
      weighingResultDetail.value.measuredQuantityUnit = measuredQuantity
        ? `${measuredQuantity}${unitName}`
        : '';
      // 未量取
      weighingResultDetail.value.unmeasuredQuantityUnit = unmeasuredQuantity
        ? `${unmeasuredQuantity}${unitName}`
        : '';
      // 称量结果列表
      resultTableProps.data = resultList;
      showWeighingResult.value = true;
      initFillData2();
      return true;
    }
    catch (error) {
      showNotify({
        message: error.message,
        type: 'danger',
      });
      return false;
    }
  };
  // 称量打码
  const weighingPrint = async (skipVerification = false) => {
    if (
      Number.isNaN(Number(weighingMachineValue.netWeight))
      || !weighingMachineValue.netWeight
    ) {
      showNotify({
        message: t('请输入正确的物料量'),
        type: 'danger',
      });
      return;
    }
    else {
      if (Number(weighingMachineValue.netWeight) <= 0) {
        showNotify({
          message: t('量取结果必须大于0'),
          type: 'danger',
        });
        return;
      }
    }
    if (!skipVerification) {
    // 配液量取且量取结果大于目标量范围
      const max
        = measureBatchDetail.value.toleranceDiff[2]
        || measureBatchDetail.value.toleranceDiff[1];
      if (
        measureBatchDetail.value.measureStage.value === 'UNMEASURED'
        && Number(weighingMachineValue.netWeight) > Number(max)
      ) {
        showNotify({
          message: t('超出批次目标量范围'),
          type: 'danger',
        });
        return;
      }
      // 余液量取且称量结果大于目标量范围,签名弹窗
      if (
        measureBatchDetail.value.measureStage.value === 'MEASURING'
        && Number(weighingMachineValue.netWeight) > Number(max)
      ) {
        showResidualMaterialSign.value = true;
        return;
      }
    }
    // 校验当前登录人和称量人是否一致
    const currentUser = getStorageSync(USER_INFO) || {};
    const { userId } = currentUser;
    if (measureBatchDetail.value.measurerId !== userId) {
      showNotify({
        message: t('登录账号与操作人不符'),
        type: 'danger',
      });
      return;
    }
    const device = bmosPrinterInstance.value.print();
    if (device) {
      try {
        const res = await handleWeigh();
        // 称量成功后打码
        if (res) {
          reqPrintStorageMaterialTagApi({
            deviceId: device.id,
            sceneId:
              measureBatchDetail.value.categoryType.value === 0
                ? 121001006
                : 121002008,
            body: {
              no: weighingResultDetail.value.no,
            },
          });
        }
      }
      catch (error) {
        error.message && showNotify({
          message: error.message,
          type: 'danger',
        });
      }
    }
  };

  // 上一步
  const handlePreviousStep = () => {
    if (auto.value) {
      // if (actionNumber.value === 0) {
      //   uni.navigateBack();
      //   return;
      // }
      // actionNumber.value = 0;
      // weighingMachineValue.tareWeight = '';
    }
    else {
      uni.navigateBack();
    }
  };
  // 下一步
  const handleNextStep = () => {
    if (measureBatchDetail.value.measureStage?.value === 'COMPLETED') {
      showNotify({
        type: 'danger',
        message: t('已完成量取'),
      });
      return;
    }
    weighingPrint();
  };

  const toBack = () => {
    uni.navigateBack();
  };

  const toResult = () => {
    uni.navigateTo({
      url: `/pages/businessComponents/liquidMeasure/result/index?id=${
        props.id
      }&componentId=${props.componentId}`,
    });
  };

  // 配液量取批次详细信息
  async function queryMeasureBatchDetail() {
    const res = await queryMeasureBatchDetailApi({
      measureBatchId: props.measureBatchId,
    });
    const {
      totalQuantity,
      targetQuantity,
      measuredQuantity,
      unmeasuredQuantity,
      unitName,
    } = res.data;
    measureBatchDetail.value = {
      ...res.data,
      totalQuantityUnit: totalQuantity ? `${totalQuantity}${unitName}` : '',
      targetQuantityUnit: targetQuantity ? `${targetQuantity}${unitName}` : '',
      measuredQuantityUnit: measuredQuantity
        ? `${measuredQuantity}${unitName}`
        : '',
      unmeasuredQuantityUnit: unmeasuredQuantity
        ? `${unmeasuredQuantity}${unitName}`
        : '',
    };
    weighingMachineValue.tareWeight = '';
    weighingMachineValue.netWeight = '';
    weighingMachineValue.grossWeight = '';
  };
  // 添加物料件
  const handleAddMaterial = async () => {
    try {
      const params = {
        consumeStorateMaterialIdList: addMaterialTableProps.data.map(
          item => item.id,
        ),
        measureBatchId: props.measureBatchId,
      };
      if (params.consumeStorateMaterialIdList.length === 0) {
        return;
      }
      await addMeasureConsumeStorageMaterialApi(params);
      queryMeasureBatchDetail();
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

  const handleResultCancel = () => {
    queryMeasureBatchDetail();
    showWeighingResult.value = false;
    toResult();
  };
  const handleResultConfirm = () => {
    queryMeasureBatchDetail();
    showWeighingResult.value = false;
    if (weighingResultDetail.value.nextMeasureStage?.value === 'COMPLETED') {
      goBackToTargetPath('pages/businessComponents/liquidMeasure/index');
    }
  };

  onShow(() => {
    queryMeasureBatchDetail();
  });

  return {
    bmosPrinterInstance,
    confirmButtonText,
    measureBatchDetail,
    weighingMachineValue,
    weighingMachineProps,
    infoItems,
    dataInfoItems,
    resultDataInfoItems,
    showMaterialAdd,
    scanMaterialAddValue,
    addMaterialTableRef,
    addMaterialTableProps,
    openBatchDetail,
    currentRow,
    showDeleteModal,
    showCompleteWeighing,
    completeWeighingSignValue,
    completeWeighingLabelList,
    scanContainerValue,
    scanPositionValue,
    positionId,
    showPositionModal,
    treePositionData,
    showWeighingResult,
    weighingResultDetail,
    resultTableRef,
    resultTableProps,
    resultConfirmText,
    showResidualMaterialSign,
    residualMaterialSignValue,
    residualMaterialLabelList,
    measureParams,
    completeWeighingParams,
    onScanSuccess,
    onScanFail,
    onScanComplete,
    onScanConfirm,
    onScanClear,
    materialAddConfirm,
    deleteMaterialConfirm,
    toBack,
    toResult,
    handlePreviousStep,
    handleNextStep,
    handleFinishMeasure,
    queryMeasureBatchDetail,
    completeWeighingSignConfirm,
    onPositionScanSelect,
    confirmPosition,
    handleWeigh,
    handleResultCancel,
    handleResultConfirm,
    residualMaterialSignConfirm,
  };
};
