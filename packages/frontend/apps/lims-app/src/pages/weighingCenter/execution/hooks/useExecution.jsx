import {
  postMesUnitCalcSumAdapt,
  queryWeighCenterExecutePendingRequirementListByTaskId,
  queryWeighCenterExecuteTaskById,
  reqScanMaterialApi,
  weighCenterExecuteAddConsumeStorageMaterial,
  weighCenterExecuteGetSingerListWithPermissionCodeAndStationIds,
  weighCenterExecuteMakeSureWeigh,
} from '@/api';
import { goBackToTargetPath } from '@/utils/func.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, reactive, ref, watch } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdIcon from 'wot-design-uni/components/wd-icon/wd-icon.vue';

export const useExecution = ({ props }) => {
  const { showNotify } = useNotify();
  const taskDetail = ref({});
  const scanValue = ref('');
  const showDeleteModal = ref(false);
  // 选中的物料需求
  const selectedMaterialRequire = ref();
  const showMaterialRequire = ref(false);
  const materialRequireValue = ref('');
  const materialRequireOptions = ref([]);
  const materialRequireSubLabels = ref([
    {
      label: t('产品信息'),
      key: 'product',
    },
    {
      label: t('生产批号'),
      key: 'batchNo',
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
      signatureAction: 90,
      disabled: true,
    },
    {
      label: t('复核人'),
      // 签名动作
      signatureAction: 91,
      disabled: false,
    },
  ]);

  // 是否是第一次
  const isFirst = computed(() => {
    return !taskDetail.value.storageMaterialBatchId && !taskDetail.value.weighExecuteRequirement;
  });

  // 扫描参数
  const scanParams = computed(() => {
    let productPlanId = '';
    let formulaMaterialId;
    let storageMaterialBatchId = '';
    storageMaterialBatchId = taskDetail.value.storageMaterialBatchId;
    if (selectedMaterialRequire.value) {
      productPlanId = selectedMaterialRequire.value.productPlanId;
      formulaMaterialId = selectedMaterialRequire.value.formulaMaterialId;
    }
    if (taskDetail.value.weighExecuteRequirement) {
      productPlanId = taskDetail.value.weighExecuteRequirement.productPlanId;
      formulaMaterialId
        = taskDetail.value.weighExecuteRequirement.formulaMaterialId;
    }
    if (tableProps.data.length > 0) {
      storageMaterialBatchId = tableProps.data[0].materialBatchId;
    }
    return {
      isAvailable: true,
      isOutbound: true,
      productPlanId,
      formulaMaterialId,
      unitId: taskDetail.value.unitId,
      storageMaterialBatchId,
    };
  });

  // 跳转到称量任务详情页
  const toWeighingTaskDetail = () => {
    uni.navigateTo({
      url: `/pages/weighingCenter/taskDetail/index?id=${props.id}`,
    });
  };
  // 选择物料需求
  const selectMaterialRequirements = () => {
    if (taskDetail.value.weighExecuteRequirement) {
      showNotify({
        type: 'danger',
        message: t('物料需求处于称量中，无法切换'),
      });
      return;
    }
    if (materialRequireOptions.value.length === 0) {
      showNotify({
        type: 'danger',
        message: t('无物料需求未称量'),
      });
      return;
    }
    showMaterialRequire.value = true;
  };
  // 物料需求确认
  const confirmMaterialRequirement = (data) => {
    selectedMaterialRequire.value = data;
  };
  const leftClick = () => {
    goBackToTargetPath('pages/weighingCenter/list/index');
  };
  const toResult = () => {
    uni.navigateTo({
      url: `/pages/weighingCenter/result/index?id=${props.id}`,
    });
  };
  const infoItems = [
    {
      label: t('称量物料'),
      field: ['materialMergeCode', 'materialName'],
      type: 'text',
    },
    {
      label: t('称量中心'),
      field: ['weighCentreCode', 'weighCentreName'],
      type: 'text',
    },
    { label: t('详情'), type: 'button', click: toWeighingTaskDetail },
  ];

  const dataInfoItems = [
    {
      label: t('产品信息'),
      field: 'product',
      type: 'text',
    },
    {
      label: t('生产批号'),
      field: 'batchNo',
      type: 'text',
    },
    {
      label: t('需求量'),
      field: 'requirementQuantityUnit',
      type: 'text',
    },
    { label: t('选择'), type: 'button', click: selectMaterialRequirements },
  ];

  const statisticalInfoItems = computed(() => [
    {
      label: t('总件数'),
      field: 'total',
    },
    {
      label: t('总量'),
      field: 'amount',
      unit: taskDetail.value?.unit,
    },
  ]);

  const currentRow = ref(null);
  // 打开删除确认弹窗
  const openDeleteModal = row => () => {
    currentRow.value = row;
    showDeleteModal.value = true;
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
        prop: 'quantity',
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
  // 删除表格行
  const deleteMaterialConfirm = () => {
    const index = tableProps.data.findIndex(
      item => item.id === currentRow.value.id,
    );
    tableProps.data.splice(index, 1);
    showDeleteModal.value = false;
    currentRow.value = null;
  };
  // 统计总量
  const amount = ref(0);
  // 统计数据
  const statisticalInfoData = computed(() => {
    return {
      total: tableProps.data.length,
      amount: amount.value,
    };
  });
  // 跳转到设备详情页
  const toDeviceDetail = () => {
    getWeighCenterExecuteTaskById();
    scanValue.value = '';
    showSign.value = false;
    tableProps.data = [];
    uni.navigateTo({
      url: `/pages/weighingCenter/modeDevice/index?id=${props.id}`,
    });
  };
  const signParams = computed(() => {
    const { userId1, userId2, remark } = signValue.value;
    return {
      consumeStorateMaterialIdList: tableProps.data.map(item => item.id),
      reCheckerId: userId2,
      remark,
      requirementId: selectedMaterialRequire.value?.id || '',
      weigherId: userId1,
    };
  });
  const signConfirm = async () => {
    try {
      await weighCenterExecuteMakeSureWeigh({
        ...signParams.value,
      });
      toDeviceDetail();
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
      await weighCenterExecuteAddConsumeStorageMaterial({
        consumeStorateMaterialIdList: tableProps.data.map(item => item.id),
        requirementId: taskDetail.value.weighExecuteRequirement?.id || selectedMaterialRequire.value.id,
        // 是否余料称量
        isResidual: taskDetail.value.weighExecuteRequirement && taskDetail.value.weighExecuteRequirement.weighProcess.value === 3,
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
    if (!taskDetail.value.weighExecuteRequirement && materialRequireOptions.value.length === 0) {
      showNotify({
        type: 'danger',
        message: t('余料称量已完成'),
      });
      return;
    }
    if (!taskDetail.value.weighExecuteRequirement && !selectedMaterialRequire.value) {
      showNotify({
        type: 'danger',
        message: t('请先选择物料需求'),
      });
      return;
    }
    if (isFirst.value) {
      // 第一次，必须添加物料件
      if (tableProps.data.length === 0) {
        showNotify({
          type: 'danger',
          message: t('请添加物料件'),
        });
        return;
      }
      weighCenterExecuteGetSingerListWithPermissionCodeAndStationIds({
        stationIds: taskDetail.value.station,
        permissionCode: '121020001000003',
      }).then((res) => {
        labelList.value[1].options = res.data.map((item) => {
          return {
            label: item.userName,
            value: item.loginName,
            id: item.userId,
          };
        });
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
      });
    }
    else {
      if (taskDetail.value.weighExecuteRequirement && [2, 6].includes(taskDetail.value.weighExecuteRequirement?.weighProcess.value) && tableProps.data.length === 0) {
        showNotify({
          type: 'danger',
          message: t('请添加物料件'),
        });
        return;
      }
      if (tableProps.data.length > 0) {
        await handleAddMaterial();
      }
      try {
        if (!taskDetail.value.weighExecuteRequirement) {
          await weighCenterExecuteMakeSureWeigh({
            requirementId: selectedMaterialRequire.value?.id,
          });
        }
        toDeviceDetail();
      }
      catch (error) {
        showNotify({
          type: 'danger',
          message: error.message,
        });
      }
    }
  };
  const handleCancel = () => {
    leftClick();
  };

  // 获取未称量物料需求列表
  const getWeighCenterExecutePendingRequirementListByTaskId = async () => {
    const res = await queryWeighCenterExecutePendingRequirementListByTaskId({
      taskId: props.id,
    });
    materialRequireOptions.value = (res.data || []).map(item => ({
      ...item,
      product: `${item.productMergeCode}-${item.productName}`,
      requirementQuantityUnit: `${item.requirementQuantity}${item.unit}`,
    }));
  };
  // 获取起称量任务详情
  async function getWeighCenterExecuteTaskById() {
    selectedMaterialRequire.value = null;
    const res = await queryWeighCenterExecuteTaskById({ taskId: props.id });
    taskDetail.value = res.data;
    const { weighExecuteRequirement: item } = res.data;
    taskDetail.value.weighExecuteRequirement = item && {
      ...item,
      product: `${item.productMergeCode}-${item.productName}`,
      requirementQuantityUnit: `${item.requirementQuantity}${item.unit}`,
    };
  };

  // 扫描物料件/设备号查询物料件信息
  const handleScan = async (code) => {
    // 有生产计划id，说明选择了物料需求
    if (scanParams.value.productPlanId) {
      try {
        const res = await reqScanMaterialApi({
          no: code,
          productPlanId: scanParams.value.productPlanId,
          storageMaterialBatchId: scanParams.value.storageMaterialBatchId,
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
        // if (res.data?.materialId !== taskDetail.value.materialId) {
        //   showNotify({
        //     type: 'danger',
        //     message: t('无法添加不符合称量需求的物料'),
        //   });
        //   return;
        // }
        // // 第一次判断表格 否则判断taskDetail.value.storageMaterialBatchId
        // if (isFirst.value) {
        //   if (tableProps.data.length > 0) {
        //     if (tableProps.data[0]?.materialBatchId !== res.data?.materialBatchId) {
        //       showNotify({
        //         type: 'danger',
        //         message: t('物料件不是同一批次'),
        //       });
        //       return;
        //     }
        //   }
        // }
        // else {
        //   if (taskDetail.value.weighExecuteRequirement && [2, 6].includes(taskDetail.value.weighExecuteRequirement?.weighProcess.value)) {
        //     if (tableProps.data.length > 0) {
        //       if (tableProps.data[0]?.materialBatchId !== res.data?.materialBatchId) {
        //         showNotify({
        //           type: 'danger',
        //           message: t('物料件不是同一批次'),
        //         });
        //         return;
        //       }
        //     }
        //   }
        //   else {
        //     if (
        //       taskDetail.value.storageMaterialBatchId
        //       !== res.data?.materialBatchId
        //     ) {
        //       showNotify({
        //         type: 'danger',
        //         message: t('物料件不是同一批次'),
        //       });
        //       return;
        //     }
        //   }
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
        message: t('请先选择物料需求'),
      });
    }
  };
  const onScanSuccess = (code) => {
    handleScan(code);
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
  const onScanConfirm = (code) => {
    handleScan(code);
  };
  watch(() => tableProps.data.length, async () => {
    const targetUnitId = taskDetail.value.unitId;
    const list = tableProps.data.map((item) => {
      return {
        unitId: item.finalUnitId,
        value: item.quantity,
      };
    });
    if (list.length === 0) {
      amount.value = 0;
      return;
    }
    try {
      const res = await postMesUnitCalcSumAdapt({
        targetUnitId,
        list,
      });
      amount.value = res.data.value;
    }
    catch (_error) {
      amount.value = 0;
    }
  });
  return {
    infoItems,
    dataInfoItems,
    statisticalInfoItems,
    taskDetail,
    scanValue,
    tableRef,
    tableProps,
    showSign,
    signValue,
    labelList,
    signParams,
    showMaterialRequire,
    materialRequireValue,
    materialRequireOptions,
    materialRequireSubLabels,
    selectedMaterialRequire,
    statisticalInfoData,
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
    getWeighCenterExecuteTaskById,
    getWeighCenterExecutePendingRequirementListByTaskId,
    confirmMaterialRequirement,
    deleteMaterialConfirm,
  };
};
