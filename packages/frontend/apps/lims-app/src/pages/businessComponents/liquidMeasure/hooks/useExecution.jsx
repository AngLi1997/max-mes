import {
  addMeasureConsumeStorageMaterialApi,
  confirmMeasureApi,
  getLiquidMeasureInstanceApi,
  getLiquidMeasurePlanDetailApi,
  getLiquidMeasurePlanListApi,
  postSingerListWithPermissionCodeAndComponentApi,
  scanStorageMaterialApi,
} from '@/api';
import {
  getCurrentCopyRecordItem,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { useLiquidMeasureStore } from '@/stores/businessComponents/liquidMeasure/index.js';
import { goBackToTargetPath } from '@/utils/func.js';
import { t } from '@/utils/useBmosI18n.js';
import { useMathJs } from '@/utils/useMathJs.js';
import { storeToRefs } from 'pinia';
import { computed, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdIcon from 'wot-design-uni/components/wd-icon/wd-icon.vue';

export const useExecution = ({ props }) => {
  const liquidMeasureStore = useLiquidMeasureStore();
  const { selectedLiquidMeasureSheet, selectedMaterialBatch } = storeToRefs(
    liquidMeasureStore,
  );
  const {
    setSelectedLiquidMeasureSheet,
    setSelectedMaterialBatch,
  } = liquidMeasureStore;
  const { showNotify } = useNotify();
  const { math } = useMathJs();
  const instance = ref({});
  const scanValue = ref('');
  const showDeleteModal = ref(false);
  // 配液单选择
  const showLiquidMeasureSheet = ref(false);
  const liquidMeasureSheetValue = ref('');
  const liquidMeasureSheetOptions = ref([]);
  // 选择物料批次
  const showMaterialBatch = ref(false);
  const materialBatchValue = ref('');
  const materialBatchOptions = ref([]);
  const materialBatchSubLabels = ref([
    {
      label: t('批号'),
      key: 'measuringBatchNo',
    },
  ]);

  const showSign = ref(false);
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
  const labelList = ref([
    {
      label: t('称量人'),
      // 签名动作
      signatureAction: 99,
      disabled: true,
    },
    {
      label: t('复核人'),
      // 签名动作
      signatureAction: 100,
      disabled: false,
      options: [],
    },
  ]);
  const openBatchDetail = ref(false);
  const currentRow = ref({});

  // 扫描参数
  const scanParams = computed(() => {
    return {
      id: instance.value.id,
      materialBatchId:
        instance.value.materialBatchId
        || selectedMaterialBatch.value?.materialBatchId,
    };
  });

  // 物料件详情
  const materialDetailsBasicItems = computed(() => {
    const arr = [
      {
        label: t('物料信息'),
        field: 'materialInfo',
      },
      {
        label: t('物料批号'),
        field: 'materialBatchNo',
      },
      {
        label: t('物料件号'),
        field: 'materialNo',
      },
      {
        label: t('生产日期'),
        field: 'produceDate',
      },
      {
        label: t('有效期至'),
        field: 'expiredDate',
      },
    ];
    const fieldList = currentRow.value.fieldList || [];
    fieldList.forEach((item, index) => {
      currentRow.value[`fieldValue${index}`] = item.fieldValue;
      arr.push({
        label: item.fieldName,
        field: `fieldValue${index}`,
      });
    });
    return arr;
  });

  // 跳转到配液单详情
  const toLiquidMeasureSheetDetail = () => {
    uni.navigateTo({
      url: `/pages/businessComponents/liquidMeasure/liquidSheetDetail/index?id=${
        instance.value.id
      }&liquidPreparationId=${
        selectedLiquidMeasureSheet.value.liquidPreparationPlanId
      }&switch=${!instance.value.liquidPreparationPlanId}
      `,
    });
  };
  // 选择物料批次
  const selectMaterialBatch = async () => {
    // 选择物料批次时，实时获取数据
    const { version } = getCurrentCopyRecordItem();
    const { productPlanId } = urlQueryRef.value;
    const { procedureStepModelId } = pageBasicDataRef.value;
    const res = await getLiquidMeasureInstanceApi({
      componentId: props.componentId,
      copyVersion: version,
      procedureStepModelId,
      productPlanId,
    });
    if (res.data.measuringBatchId) {
      showNotify({
        type: 'danger',
        message: t('配液批次处于量取中，无法切换'),
      });
      return;
    }
    // 获取物料批次
    await getLiquidMeasurePlanDetail();
    if (materialBatchOptions.value.length === 0) {
      showNotify({
        type: 'danger',
        message: t('无配液批次未量取'),
      });
      return;
    }
    materialBatchValue.value = selectedMaterialBatch.value?.id || '';
    showMaterialBatch.value = true;
  };

  // 获取配液单详情
  const getLiquidMeasurePlanDetail = async () => {
    const res = await getLiquidMeasurePlanDetailApi({
      id: instance.value.id,
      liquidPreparationId:
        instance.value.liquidPreparationPlanId
        || selectedLiquidMeasureSheet.value.liquidPreparationPlanId,
    });
    materialBatchOptions.value = (res.data.batchList || [])
      .filter(item => item.measureStatus.value === 'UNMEASURED')
      .map(item => ({
        ...item,
        materialFullName: `${item.materialMergeCode}-${item.materialName}`,
        measuringBatchNo: item.materialBatchNo,
      }));
  };
  // 取消配液单选择
  const cancelLiquidMeasureSheet = () => {
    uni.navigateBack();
  };
  // 配液单确认
  const confirmLiquidMeasureSheet = (data) => {
    if (data) {
      setSelectedLiquidMeasureSheet({
        liquidPreparationPlanName: data.name,
        liquidPreparationPlanId: data.id,
      });
    }
    else {
      showNotify({
        type: 'danger',
        message: '请选择配液单',
      });
    }
  };

  // 选择物料批次确定
  const confirmMaterialBatch = (data) => {
    if (data) {
      setSelectedMaterialBatch({
        ...data,
        materialFullName: data.materialFullName,
      });
    }
    else {
      showNotify({
        type: 'danger',
        message: '请选择物料批次',
      });
    }
  };
  const leftClick = () => {
    goBackToTargetPath();
  };
  const toResult = () => {
    uni.navigateTo({
      url: `/pages/businessComponents/liquidMeasure/result/index?id=${
        instance.value.id
      }&componentId=${props.componentId}`,
    });
  };
  const infoItems = [
    {
      label: t('配液单'),
      field: 'liquidPreparationPlanName',
      type: 'text',
    },
    { label: t('详情'), type: 'button', click: toLiquidMeasureSheetDetail },
  ];

  const dataInfoItems = [
    {
      label: t('物料信息'),
      field: 'materialFullName',
      type: 'text',
    },
    {
      label: t('物料批号'),
      field: 'measuringBatchNo',
      type: 'text',
    },
    { label: t('选择'), type: 'button', click: selectMaterialBatch },
  ];

  const statisticalInfoItems = computed(() => [
    {
      label: t('总件数'),
      field: 'total',
    },
    {
      label: t('总量'),
      field: 'amount',
      unit: instance.value?.unit,
    },
  ]);

  // 打开删除确认弹窗
  const openDeleteModal = row => () => {
    currentRow.value = row;
    showDeleteModal.value = true;
  };
  // 删除表格行
  const deleteMaterialConfirm = () => {
    const index = tableProps.data.findIndex(
      item => item.id === currentRow.value.id,
    );
    tableProps.data.splice(index, 1);
    showDeleteModal.value = false;
    currentRow.value = null;
  };

  const tableRef = ref();

  const tableProps = reactive({
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
        width: 140,
      },
      {
        prop: 'ACTION',
        label: '物料详情',
        width: 100,
        actions: ({ row, tableInstance }) => {
          return [
            {
              label: '查看',
              onClick: () => {
                currentRow.value = row;
                currentRow.value.materialInfo = `${row.materialMergeCode}-${row.materialName}`;
                openBatchDetail.value = true;
              },
            },
          ];
        },
      },
      {
        prop: 'materialQuantity',
        label: t('物料量'),
        width: 160,
      },
      {
        prop: 'unitName',
        label: t('单位'),
        width: 100,
      },
      {
        prop: 'expiredDate',
        label: t('有效期至'),
        width: 200,
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

  // 统计数据
  const statisticalInfoData = computed(() => {
    return {
      total: tableProps.data.length,
      amount:
        tableProps.data.reduce((prev, cur) => {
          try {
            return math.add(prev, cur.materialQuantity);
          }
          catch (error) {
            return 0;
          }
        }, 0) + (tableProps.data[0]?.unitName || ''),
    };
  });
  // 跳转到设备详情页
  const toDeviceDetail = (measuringBatchId) => {
    getLiquidMeasureInstance();
    showSign.value = false;
    uni.navigateTo({
      url: `/pages/businessComponents/liquidMeasure/modeDevice/index?id=${
        instance.value.id
      }&measureBatchId=${measuringBatchId
      || instance.value.measuringBatchId}&componentId=${props.componentId}`,
    });
  };
  const signParams = computed(() => {
    const { userId1, userId2, remark } = signValue.value;
    return {
      consumeStorateMaterialIdList: tableProps.data.map(item => item.id),
      id: instance.value.id,
      measurerId: userId1,
      reCheckerId: userId2,
      remark,
      planBatchId:
        instance.value.planBatchId || selectedMaterialBatch.value?.id,
    };
  });
  const signConfirm = async () => {
    try {
      const res = await confirmMeasureApi({
        ...signParams.value,
      });
      toDeviceDetail(res.data);
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  // 添加物料件
  const handleAddMaterial = async () => {
    try {
      await addMeasureConsumeStorageMaterialApi({
        consumeStorateMaterialIdList: tableProps.data.map(item => item.id),
        measureBatchId: instance.value.measuringBatchId,
      });
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  const handleNextStep = async () => {
    if (instance.value.materialBatchId) {
      if (tableProps.data.length === 0) {
        toDeviceDetail();
      }
      else {
        await handleAddMaterial();
        toDeviceDetail();
      }
    }
    else if (tableProps.data.length === 0) {
      showNotify({
        type: 'danger',
        message: t('请添加物料件'),
      });
    }
    else {
      showSign.value = true;
      signValue.value = {
        loginName1: signValue.value.loginName1,
        userId1: signValue.value.userId1,
        userName1: signValue.value.userName1,
        password1: '',
        loginName2: '',
        password2: '',
        userId2: '',
        userName2: '',
        remark: '',
      };
    }
  };
  const handleCancel = () => {
    leftClick();
  };

  // 获取未量取的配液单列表
  const getLiquidMeasurePlanList = async () => {
    const res = await getLiquidMeasurePlanListApi({
      productPlanId: urlQueryRef.value.productPlanId,
    });
    liquidMeasureSheetOptions.value = res.data || [];
  };
  // 获取配液称量组件实例
  async function getLiquidMeasureInstance() {
    const { version } = getCurrentCopyRecordItem();
    const { productPlanId } = urlQueryRef.value;
    const { procedureStepModelId } = pageBasicDataRef.value;
    const res = await getLiquidMeasureInstanceApi({
      componentId: props.componentId,
      copyVersion: version,
      procedureStepModelId,
      productPlanId,
    });
    instance.value = res.data;
    // 重置数据
    setSelectedMaterialBatch(null);
    setSelectedLiquidMeasureSheet({
      liquidPreparationPlanName: instance.value.liquidPreparationPlanName,
      liquidPreparationPlanId: instance.value.liquidPreparationPlanId,
    });
    tableProps.data = [];
    if (!instance.value.liquidPreparationPlanId) {
      showLiquidMeasureSheet.value = true;
    }
  };

  // 扫描物料件/设备号查询物料件信息
  const handleScan = async (code) => {
    // 有物料批次id时扫描物料件
    if (scanParams.value.materialBatchId) {
      try {
        const res = await scanStorageMaterialApi({
          ...scanParams.value,
          code,
        });
        if (tableProps.data.some(item => item.id === res.data.id)) {
          showNotify({
            type: 'danger',
            message: t('物料件已添加，不能重复添加'),
          });
          return;
        }
        tableProps.data.push(res.data);
      }
      catch (error) {
        showNotify({
          type: 'danger',
          message: error.message,
        });
      }
    }
    else {
      showNotify({
        type: 'danger',
        message: t('请先选择物料批次'),
      });
    }
  };
  const onScanSuccess = (code) => {
    handleScan(code);
  };
  const onScanFail = (result) => {
    showNotify({
      type: 'danger',
      message: t('扫描失败'),
    });
  };
  const onScanComplete = (result) => {
    console.log('onScanComplete', result);
  };
  const onScanConfirm = (code) => {
    handleScan(code);
  };
  const getReCheckerList = async () => {
    try {
      const res = await postSingerListWithPermissionCodeAndComponentApi({
        permissionCode: '121010001002016',
        componentId: props.componentId,
        procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
        productPlanId: urlQueryRef.value.productPlanId,
      });
      const list = (res.data || []).map((item) => {
        return {
          label: `${item.userName}`,
          value: item.loginName,
          id: item.userId,
        };
      });
      labelList.value[1].options = list;
    }
    catch (error) {
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
      });
    }
  };
  return {
    infoItems,
    dataInfoItems,
    statisticalInfoItems,
    statisticalInfoData,
    instance,
    scanValue,
    tableRef,
    tableProps,
    openBatchDetail,
    materialDetailsBasicItems,
    currentRow,
    showSign,
    signValue,
    labelList,
    signParams,
    showLiquidMeasureSheet,
    liquidMeasureSheetValue,
    liquidMeasureSheetOptions,
    selectedLiquidMeasureSheet,
    showMaterialBatch,
    materialBatchValue,
    materialBatchOptions,
    materialBatchSubLabels,
    selectedMaterialBatch,
    showDeleteModal,
    leftClick,
    toResult,
    onScanSuccess,
    onScanFail,
    onScanComplete,
    onScanConfirm,
    signConfirm,
    handleNextStep,
    handleCancel,
    getLiquidMeasureInstance,
    getLiquidMeasurePlanList,
    cancelLiquidMeasureSheet,
    confirmLiquidMeasureSheet,
    confirmMaterialBatch,
    deleteMaterialConfirm,
    getReCheckerList,
  };
};
