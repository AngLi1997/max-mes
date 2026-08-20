import {
  getStorageConfigTreeApi,
  queryWeighCenterExecuteRequirementDetail,
  queryWeighCenterExecuteTicketDetail, // 查询工单详情
  queryWeighCenterExecuteTicketOddmentInfo, // 余料称量详情
  reqPrintStorageMaterialTagApi, // 打码
  weighCenterExecuteRequirementBindStorageMaterial,
  weighCenterExecuteRequirementFinish,
  weighCenterExecuteRequirementOddment, // 余料称量
  weighCenterExecuteRequirementRecord, // 称量
  weighCenterExecuteScanLhStorageMaterial,
  weighCenterExecuteScanWeighContainerCodeApi, // 容器
  weighCenterExecuteScanWeighPositionCodeApi, // 货位
} from '@/api';
import { useBMBalanceMqtt } from '@/BMUtils/useBMBalanceMqtt.js';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { goBackToTargetPath, throttle } from '@/utils/func.js';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
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
  // 称量打码loading
  const nextLoading = ref(false);
  const timer = ref(null);
  const { math } = useMathJs();
  const { showNotify } = useNotify();
  // 0:清零 1:去皮 2:称量打码
  const actionNumber = ref(0);
  // 需求详情
  const requirementDetail = ref({});
  // 工单详情
  const ticketDetail = ref({});
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
        prop: 'unitName',
        label: t('单位'),
        width: 100,
      },
      {
        prop: 'deviceName',
        label: t('容器'),
        width: 254,
        customRender: ({ row }) => {
          return row.deviceName ? `${row.deviceCode}-${row.deviceName}` : '-';
        },
      },
      {
        prop: 'materialPositionName',
        label: t('暂存货位'),
        width: 254,
        customRender: ({ row }) => {
          return row.storageName ? `${row.storageCode}-${row.storageName}` : '-';
        },
      },
    ],
  });
  const isFirst = ref(true);
  // 是否余料称量
  const oddmentEnough = ref(false);
  // 完成称量参数
  const completeWeighingParams = computed(() => {
    return {
      requirementId: props.requirementId,
      ticketId: props.id,
      finishSignUser: completeWeighingSignValue.value.userId1,
      weighType: oddmentEnough.value ? '1' : '2',
    };
  });
  const resultConfirmText = computed(() => {
    if (!oddmentEnough.value) {
      if (weighingResultDetail.value.oddmentEnough) {
        return t('余料称量');
      }
      if (weighingResultDetail.value.requirementEnough) {
        return t('更换物料需求');
      }
    }
    return t('继续称量');
  });
  // 是否自动称量
  const auto = computed(() => props.mode === 'true');
  const socketParams = computed(() => {
    return {
      auto: auto.value,
      batchNo: requirementDetail.value.batchNo,
      productName: requirementDetail.value.productMaterialName,
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
  } = useBMBalanceMqtt(socketParams);
  const weighingMachineValue = reactive({
    tareWeight: '', // 皮重
    netWeight: '', // 净重
    grossWeight: '', // 毛重
  });

  const weighingMachineProps = computed(() => {
    const { notWeighToleranceUpper, notWeighToleranceLower, unitName, chargeMixtureToleranceUpper, chargeMixtureToleranceType, chargeMixtureToleranceLower, notWeighQuality } = requirementDetail.value;
    return {
      auto: auto.value,
      actionNumber: actionNumber.value,
      weighingName:
        oddmentEnough.value
          ? t('余料称量')
          : t('增重称量'),
      unit: unitName,
      weight: weightInfo.weight,
      targetAmount: notWeighQuality,
      remainingAmount: math.format(
        math.subtract(math.bignumber(notWeighQuality || 0), math.bignumber(weightInfo.weight || 0)),
      ),
      diff: {
        maxTolerance: chargeMixtureToleranceUpper,
        minTolerance: chargeMixtureToleranceLower,
        toleranceTypeEnum: chargeMixtureToleranceType,
        toleranceDiff: [
          notWeighToleranceLower,
          notWeighQuality,
          notWeighToleranceUpper,
        ],
      },
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
  // 返回执行页面
  const backToExecution = () => {
    goBackToTargetPath('pages/weighingWorkOrder/execution/index');
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
        oddmentEnough.value ? 139 : 137,
      menuId:
        oddmentEnough.value
          ? '121020007000005'
          : '121020007000004',
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
      signatureAction: 138,
      menuId: '121020007000006',
    },
  ]);

  const completeWeighingSignConfirm = async () => {
    try {
      await completeWeighingSignModalRef.value?.checkSign();
      await weighCenterExecuteRequirementFinish(completeWeighingParams.value);
      backToExecution();
    }
    catch (error) {
      if (error.data && error.data.failedIndex && error.data.failedIndex.length > 0) {
        return;
      }
      error.message !== 'error' && showNotify({
        message: error.message,
        type: 'danger',
      });
    }
  };

  // 需求详情
  const showRequirementDetail = ref(false);

  // 添加物料弹窗
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
        width: 240,
      },
      {
        prop: 'materialNo',
        label: t('物料件号'),
        width: 240,
      },
      {
        prop: 'availableQuantity',
        label: t('物料量'),
        width: 200,
      },
      {
        prop: 'unit',
        label: t('单位'),
        width: 100,
      },
      {
        prop: 'expiredDate',
        label: t('有效期至'),
        width: 182,
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
  const containerId = ref(''); // 容器id
  const containerName = ref(''); // 容器名称
  const containerCode = ref(''); // 容器编码
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
        storageMaterialIds: addMaterialTableProps.data.map(
          item => item.id,
        ),
        requirementId: requirementDetail.value.id,
      };
      if (params.storageMaterialIds.length === 0) {
        return;
      }
      await weighCenterExecuteRequirementBindStorageMaterial(params);
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
  const toRequirementDetail = () => {
    showRequirementDetail.value = true;
  };

  const handleFinishWeighing = () => {
    showCompleteWeighing.value = true;
  };

  const infoItems = computed(() => {
    const arr = [
      {
        label: t('物料信息'),
        field: ['materialMergeCode', 'materialName'],
        type: 'text',
      },
      {
        label: t('物料批号'),
        field: 'storageMaterialBatchNo',
        type: 'text',
      },
      { label: t('添加物料'), type: 'button', click: addMaterial },
      { label: t('详情'), type: 'button', click: toRequirementDetail },
    ];
    // 余料称量时，不展示添加物料和详情按钮
    if (oddmentEnough.value) {
      arr.splice(2, 2);
    }
    return arr;
  });

  const dataInfoItems = [
    {
      label: t('添加物料总量'),
      field: 'ticketQualityUnit',
      type: 'text',
    },
    {
      label: t('物料剩余量'),
      field: 'remainingQuantityUnit',
      type: 'text',
    },
    {
      label: oddmentEnough.value ? t('余料目标量') : t('需求目标量'),
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
  ];
  const initData = () => {
    positionId.value = '';
    // scanPositionValue.value = '';
    containerId.value = '';
    containerName.value = '';
    containerCode.value = '';
    scanContainerValue.value = '';
    actionNumber.value = 0;
    weighingMachineValue.tareWeight = '';
    weighingMachineValue.netWeight = '';
    weighingMachineValue.grossWeight = '';
  };
  // 获取需求详情
  const getWeighCenterExecuteRequirementById = async (data = { init: true }) => {
    if (data.init) {
      initData();
    }

    let res;
    if (oddmentEnough.value) {
      res = await queryWeighCenterExecuteTicketOddmentInfo(props.id);
    }
    else {
      res = await queryWeighCenterExecuteRequirementDetail(props.requirementId);
    }
    requirementDetail.value = res.data;
    const {
      ticketQuality,
      remainingQuality,
      requirementQuantity,
      notWeighQuality,
      weighedQuantity,
      unitName,
    } = res.data;
    // 添加物料总量
    requirementDetail.value.ticketQualityUnit = ticketQuality
      ? `${ticketQuality}${unitName}`
      : '';
    // 物料剩余量
    requirementDetail.value.remainingQuantityUnit = remainingQuality
      ? `${remainingQuality}${unitName}`
      : '';
    // 需求目标量
    requirementDetail.value.targetTotalQuantityUnit = requirementQuantity
      ? `${requirementQuantity}${unitName}`
      : '';
    // 已称量
    requirementDetail.value.weighedQuantityUnit = weighedQuantity
      ? `${weighedQuantity}${unitName}`
      : '';
    // 未称量
    requirementDetail.value.unWeighedQuantityUnit = notWeighQuality
      ? `${notWeighQuality}${unitName}`
      : '';
    // 调用接口后，修改标识
    isFirst.value = false;
  };
  const weighingCenterExecuteParams = computed(() => {
    return {
      deviceId: containerId.value || undefined,
      deviceName: containerName.value || undefined,
      deviceCode: containerCode.value || undefined,
      storageId: positionId.value || undefined,
      equipmentId: auto.value ? selectedBalance.value.balanceId : undefined,
      grossWeight: weighingMachineValue.grossWeight,
      netWeight: weighingMachineValue.netWeight,
      weighTicketRequirementId: props.requirementId,
      ticketId: props.id,
      tareWeight: weighingMachineValue.tareWeight,
      unitId: requirementDetail.value.unitId,
      weighFunc: auto.value ? 2 : 1,
      weighType: oddmentEnough.value ? 1 : 2,
    };
  });
  const handleWeigh = async () => {
    nextLoading.value = true;
    try {
      let res;
      if (oddmentEnough.value) {
        res = await weighCenterExecuteRequirementOddment({
          ...weighingCenterExecuteParams.value,
          finishSignUser: residualMaterialSignValue.value.userId1,
        });
      }
      else {
        res = await weighCenterExecuteRequirementRecord(weighingCenterExecuteParams.value);
      }
      weighingResultDetail.value = res.data;
      const {
        weighRequirementRecordVOList,
      } = res.data;
      // 称量结果列表
      resultTableProps.data = weighRequirementRecordVOList;
      showWeighingResult.value = true;
      getWeighCenterExecuteRequirementById({
        init: true,
      });
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
      // 未超出批次目标量范围，清除签名数据
      residualMaterialSignValue.value.userId1 = '';
      residualMaterialSignValue.value.userName1 = '';
      residualMaterialSignValue.value.loginName1 = '';
      residualMaterialSignValue.value.password1 = '';

      // 物料称量且称量结果+已称量大于目标量范围
      const max
        = requirementDetail.value.chargeUpperQuality;
      if (
        !oddmentEnough.value
        && Number(weighingMachineValue.netWeight) + Number(requirementDetail.value.weighedQuantity) > Number(max)
      ) {
        showNotify({
          message: t('超出批次目标量范围'),
          type: 'danger',
        });
        return;
      }
      // 余料称量且称量结果+已称量大于目标量范围,签名弹窗
      if (
        oddmentEnough.value
        && Number(weighingMachineValue.netWeight) + Number(requirementDetail.value.weighedQuantity) > Number(max)
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
      if (requirementDetail.value.weighUserId !== userId) {
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
          let sceneId = weighingResultDetail.value?.categoryInfoType?.value === 0 ? 121001014 : 121002018;
          if (oddmentEnough.value) {
            sceneId = weighingResultDetail.value?.categoryInfoType?.value === 0 ? 121001017 : 121002021;
          }
          await reqPrintStorageMaterialTagApi({
            deviceId: device.id,
            sceneId,
            body: {
              no: weighingResultDetail.value.storageMaterialNo,
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
        showNotify({
          type: 'danger',
          message: t('秤具连接异常'),
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
      url: `/pages/weighingWorkOrder/result/index?id=${props.id}`,
    });
  };

  // 扫描物料件/设备号查询物料件信息
  const handleMaterialScan = async (code) => {
    try {
      const res = await weighCenterExecuteScanLhStorageMaterial({
        no: code,
        storageMaterialBatchId: requirementDetail.value.storageMaterialBatchId,
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
  const scanMaterialSuccess = (res) => {
    // 判断res的前两位是否为01 02 04,如果是则去掉前两位
    if (res.startsWith('01') || res.startsWith('02') || res.startsWith('04')) {
      const code = res.substring(2);
      code && handleMaterialScan(code);
    }
    else {
      showNotify({
        type: 'danger',
        message: t('请扫描的物料件/容器标签'),
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
      containerName.value = res.data.deviceName;
      containerCode.value = res.data.deviceCode;
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };

  // 清空容器
  const handleClearContainer = () => {
    if (containerId.value) {
      scanContainerValue.value = '';
      containerId.value = '';
      containerName.value = '';
      containerCode.value = '';
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
  // 扫描成功
  const scanSuccess = (res) => {
    // 判断res的前两位是否为03 货位 04 设备
    const code = res.substring(2);
    if (res.startsWith('03') && code) {
      handlePositionScan(code);
    }
    else if (res.startsWith('04')) {
      handleContainerScan(code);
    }
    else {
      showNotify({
        type: 'danger',
        message: t('请扫描容器/货位标签'),
      });
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
  const onClearPosition = () => {
    positionId.value = '';
    scanPositionValue.value = '';
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
    if (!oddmentEnough.value && weighingResultDetail.value.requirementEnough) {
      backToExecution();
    }
    else {
      initData();
    }
    updateOddmentEnough();
  };

  // 更新余料称量状态
  const updateOddmentEnough = async () => {
    const res1 = await queryWeighCenterExecuteTicketDetail(props.id);
    oddmentEnough.value = res1.data.oddmentEnough;
    ticketDetail.value = res1.data;
  };

  // 退出称量
  const exitWeighing = () => {
    goBackToTargetPath('pages/weighingWorkOrder/list/index');
  };
  onMounted(async () => {
    await updateOddmentEnough();
    getWeighCenterExecuteRequirementById();
  });
  onShow(async () => {
    if (!isFirst.value) {
      await updateOddmentEnough();
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
          weighingMachineValue.grossWeight = '';
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
    weighingMachineValue,
    weighingMachineProps,
    actionNumber,
    confirmButtonText,
    infoItems,
    dataInfoItems,
    requirementDetail,
    showCompleteWeighing,
    completeWeighingSignValue,
    completeWeighingLabelList,
    completeWeighingParams,
    completeWeighingSignConfirm,
    showResidualMaterialSign,
    residualMaterialSignValue,
    residualMaterialLabelList,
    residualMaterialSignConfirm,
    showRequirementDetail,
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
    toRequirementDetail,
    handleFinishWeighing,
    deleteMaterialConfirm,
    handleContainerScan,
    handleClearContainer,
    handlePositionScan,
    materialAddConfirm,
    handleMaterialScan,
    scanMaterialSuccess,
    scanSuccess,
    onPositionScanSelect,
    onClearPosition,
    confirmPosition,
    handleWeigh,
    handleResultCancel,
    handleResultConfirm,
    exitWeighing,
  };
};
