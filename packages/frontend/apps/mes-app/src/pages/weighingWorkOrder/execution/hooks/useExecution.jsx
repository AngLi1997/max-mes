import {
  postMesUnitCalcSumAdapt,
  queryWeighCenterExecuteTicketDetail,
  weighCenterExecuteGetSingerListWithPermissionCodeAndStationIds,
  weighCenterExecuteRequirementBindStorageMaterial,
  weighCenterExecuteRequirementExecute,
  weighCenterExecuteScanLhStorageMaterial,
  weighCenterExecuteTicketBindOperator,
} from '@/api';
import { goBackToTargetPath } from '@/utils/func.js';
import { t } from '@/utils/useBmosI18n.js';
import { onShow } from '@dcloudio/uni-app';
import { computed, reactive, ref, watch } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdIcon from 'wot-design-uni/components/wd-icon/wd-icon.vue';

export const useExecution = ({ props }) => {
  const { showNotify } = useNotify();
  const taskDetail = ref({});
  const scanValue = ref('');
  const showDeleteModal = ref(false);
  // 选中的物料需求
  const selectedMaterialRequire = ref({});
  const showMaterialRequire = ref(false);
  const materialRequireValue = ref('');
  const materialRequireOptions = computed(() => {
    if (taskDetail.value.requirements) {
      return taskDetail.value.requirements.filter(item => item.requirementStatus.value === 1).map((item) => {
        return {
          ...item,
          requirementQuantityUnit: `${item.requirementQuantity}${item.unitName}`,
          product: `${item.productMaterialMergeCode}-${item.productMaterialName}`,
        };
      });
    }
    else {
      return [];
    }
  });
  const materialRequireSubLabels = ref([
    {
      label: t('物料批号'),
      key: 'storageMaterialBatchNo',
    },
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
      signatureAction: 135,
      disabled: true,
    },
    {
      label: t('复核人'),
      // 签名动作
      signatureAction: 136,
      disabled: false,
    },
  ]);

  // 是否是第一次
  const isFirst = computed(() => {
    return !taskDetail.value.weighUserId;
  });

  // 跳转到称量任务详情页
  const toWeighingTaskDetail = () => {
    uni.navigateTo({
      url: `/pages/weighingWorkOrder/taskDetail/index?id=${props.id}`,
    });
  };
  // 选择物料需求
  const selectMaterialRequirements = () => {
    if (selectedMaterialRequire.value?.requirementStatus?.value !== 1) {
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
    materialRequireValue.value = selectedMaterialRequire.value.id;
    showMaterialRequire.value = true;
  };
  // 物料需求确认
  const confirmMaterialRequirement = (data) => {
    selectedMaterialRequire.value = data;
  };
  const leftClick = () => {
    goBackToTargetPath('pages/weighingWorkOrder/list/index');
  };
  const toResult = () => {
    uni.navigateTo({
      url: `/pages/weighingWorkOrder/result/index?id=${props.id}`,
    });
  };
  const infoItems = [
    {
      label: t('工单编号'),
      field: 'ticketNo',
      type: 'text',
    },
    {
      label: t('称量中心'),
      field: ['centreCode', 'centreName'],
      type: 'text',
    },
    { label: t('详情'), type: 'button', click: toWeighingTaskDetail },
  ];

  const dataInfoItems = [
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
    {
      label: t('需求量'),
      field: ['requirementQuantity', 'unitName'],
      type: 'text',
      hyphen: ' ',
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
      unit: taskDetail.value?.unitName,
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
      url: `/pages/weighingWorkOrder/modeDevice/index?id=${props.id}`,
    });
  };

  // 执行称量需求
  const executeRequirement = async () => {
    try {
      const storageMaterialIds = tableProps.data.map(item => item.id);
      await weighCenterExecuteRequirementExecute({
        requirementId: selectedMaterialRequire.value.id,
        storageMaterialIds,
        ticketId: props.id,
      });
      const params = {
        storageMaterialIds,
        requirementId: selectedMaterialRequire.value.id,
      };
      if (params.storageMaterialIds.length !== 0) {
        await weighCenterExecuteRequirementBindStorageMaterial(params);
      }
      toDeviceDetail();
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  const signParams = computed(() => {
    const { userId1, userId2, remark } = signValue.value;
    return {
      ticketId: props.id,
      userId: userId1,
      signUser: userId2,
      remark,
    };
  });
  const signConfirm = async () => {
    try {
      await weighCenterExecuteTicketBindOperator({
        ...signParams.value,
      });
      showSign.value = false;
      executeRequirement();
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  const handleNextStep = async () => {
    if (taskDetail.value.enoughCompleteCondition) {
      showNotify({
        type: 'danger',
        message: t('余料称量已完成'),
      });
      return;
    }
    if (!selectedMaterialRequire.value.id && !taskDetail.value.oddmentEnough) {
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
        stationIds: taskDetail.value.stationIdList,
        permissionCode: '121020007000003',
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
      executeRequirement();
    }
  };
  const handleCancel = () => {
    leftClick();
  };

  // 获取起称量任务详情
  async function getWeighCenterExecuteTaskById() {
    const res = await queryWeighCenterExecuteTicketDetail(props.id);
    taskDetail.value = res.data;
    if (isFirst.value) {
      // 默认选中第一个需求
      selectedMaterialRequire.value = taskDetail.value.requirements[0];
    }
    else if (!taskDetail.value.oddmentEnough) {
      selectedMaterialRequire.value = materialRequireOptions.value[0];
      taskDetail.value.requirements.forEach((item) => {
        if (item.requirementStatus.value === 2) {
          selectedMaterialRequire.value = item;
        }
      });
    }
    else {
      taskDetail.value.requirements.forEach((item) => {
        if (item.lastFlg) {
          selectedMaterialRequire.value = item;
        }
      });
    }
  };

  // 扫描物料件/设备号查询物料件信息
  const handleScan = async (code) => {
    try {
      const res = await weighCenterExecuteScanLhStorageMaterial({
        no: code,
        storageMaterialBatchId: taskDetail.value.storageMaterialBatchId,
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
  };
  const onScanConfirm = () => {
    if (scanValue.value) {
      handleScan(scanValue.value);
    }
  };
  const scanSuccess = (res) => {
    // 判断res的前两位是否为01 02 04,如果是则去掉前两位
    if (res.startsWith('01') || res.startsWith('02') || res.startsWith('04')) {
      const code = res.substring(2);
      code && handleScan(code);
    }
    else {
      showNotify({
        type: 'danger',
        message: t('请扫描的物料件/容器标签'),
      });
    }
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
  onShow(() => {
    getWeighCenterExecuteTaskById();
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
    onScanConfirm,
    scanSuccess,
    signConfirm,
    handleNextStep,
    handleCancel,
    getWeighCenterExecuteTaskById,
    confirmMaterialRequirement,
    deleteMaterialConfirm,
  };
};
