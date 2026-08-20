import {
  changeLiquidOutputProducerApi,
  invalidLiquidOutputApi,
  postSingerListWithPermissionCodeAndComponentApi,
  queryLiquidOutputProduceApi,
  reqPrintStorageMaterialTagApi,
  signLiquidOutputApi,
} from '@/api';
import {
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { goBackToTargetPath } from '@/utils/func.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdTag from 'wot-design-uni/components/wd-tag/wd-tag.vue';

export const useResult = ({ props }) => {
  const { showNotify } = useNotify();
  // 打印实例
  const bmosPrinterInstance = ref(null);
  const resultDetail = ref({});
  const infoItems = [
    {
      label: t('操作人'),
      field: ['producerName', 'producerLoginName'],
      type: 'text',
    },
    {
      label: t('复核人'),
      field: ['reCheckerName', 'reCheckerLoginName'],
      type: 'text',
    },
  ];
  const showQuitModal = ref(false);
  const showSignModal = ref(false);
  const showInvalidModal = ref(false);
  const showReplaceModal = ref(false);

  const signValue = ref({
    userName1: '',
    loginName1: '',
    password1: '',
    userId1: '',
    userName2: '',
    loginName2: '',
    password2: '',
    userId2: '',
    remark: '',
  });

  const labelList = ref([
    {
      label: t('操作人'),
      // 签名动作
      signatureAction: 103,
      disabled: true,
      options: [],
    },
    {
      label: t('复核人'),
      // 签名动作
      signatureAction: 104,
      disabled: true,
      options: [],
    },
  ]);
  const labelList1 = ref([
    {
      label: t('操作人'),
      // 签名动作
      signatureAction: 108,
      disabled: true,
      options: [],
    },
    {
      label: t('复核人'),
      // 签名动作
      signatureAction: 108,
      disabled: true,
      options: [],
    },
  ]);

  // 作废签名
  const invalidSignValue = ref({
    userName1: '',
    loginName1: '',
    userId1: '',
    userName2: '',
    loginName2: '',
    userId2: '',
    remark: '',
  });

  // 作废签名人员表
  const invalidLabelList = ref([
    {
      label: t('操作人'),
      // 签名动作
      signatureAction: 106,
      disabled: true,
      options: [],
    },
    {
      label: t('复核人'),
      // 签名动作
      signatureAction: 107,
      disabled: true,
      options: [],
    },
  ]);
  // 更换称量人/复核人
  const replaceLabelList = ref([
    {
      label: t('操作人'),
      // 签名动作
      signatureAction: 108,
      options: [],
    },
    {
      label: t('复核人'),
      // 签名动作
      signatureAction: 108,
      options: [],
    },
  ]);

  const tableKey = ref(0);
  const tableRef = ref();
  const selectedTableRows = ref([]);
  const tagTypes = ['warning', 'success', 'danger'];
  const tableProps = reactive({
    type: '',
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
        prop: 'signStatus',
        label: t('状态'),
        width: 112,
        customRender: ({ row }) => {
          return (
            <WdTag
              plain
              type={tagTypes[row.signStatus.value]}
            >
              {row.signStatus.name}
            </WdTag>
          );
        },
      },
      {
        prop: 'materialCode',
        label: t('物料编码'),
        width: 160,
      },
      {
        prop: 'materialName',
        label: t('物料名称'),
        width: 180,
      },
      {
        prop: 'storageMaterialBatchNo',
        label: t('物料批号'),
        width: 220,
      },
      {
        prop: 'storageMaterialNo',
        label: t('物料件号'),
        width: 220,
      },
      {
        prop: 'quantity',
        label: t('物料量'),
        width: 145,
      },
      {
        prop: 'unit',
        label: t('单位'),
        width: 90,
      },
      {
        prop: 'producerFullName',
        label: t('产出人'),
        width: 220,
      },
      {
        prop: 'reCheckerFullName',
        label: t('复核人'),
        width: 220,
      },
      {
        prop: 'containerName',
        label: t('容器'),
        width: 220,
      },
      {
        prop: 'materialPositionName',
        label: t('货位'),
        width: 220,
      },
      {
        prop: 'produceTime',
        label: t('产出时间'),
        width: 223,
      },
      {
        prop: 'ACTION',
        label: '标签',
        width: 80,
        actions: ({ row, tableInstance }) => {
          return [
            {
              label: '打印',
              onClick: async () => {
                const device = bmosPrinterInstance.value.print();
                if (device) {
                  try {
                    await reqPrintStorageMaterialTagApi({
                      deviceId: device.id,
                      sceneId: 121002011,
                      body: {
                        no: row.storageMaterialNo,
                      },
                    });
                  }
                  catch (error) {
                    showNotify({
                      type: 'danger',
                      message: error.message,
                    });
                  }
                }
              },
            },
          ];
        },
      },
    ],
    selectionProps: (row) => {
      return {
        disabled: row.signStatus.value !== 0,
      };
    },
  });

  const replaceValue = ref({
    userName1: '',
    loginName1: '',
    password1: '',
    userId1: '',
    userName2: '',
    loginName2: '',
    password2: '',
    userId2: '',
  });

  const allSigned = computed(() => {
    const { produceRecordList } = resultDetail.value;
    return (produceRecordList || []).every(item => item.signStatus.value);
  });
  const leftClick = () => {
    uni.navigateBack();
  };
  const exitWeighing = () => {
    if (!allSigned.value) {
      showQuitModal.value = true;
    }
    else {
      goBackToTargetPath();
    }
  };

  const handleInvalid = () => {
    tableProps.type = 'selection';
    tableKey.value += 1;
    selectedTableRows.value = [];
  };
  // 表格选择
  const tableSelection = (selectedRows) => {
    selectedTableRows.value = selectedRows;
  };

  const confirmInvalid = () => {
    if (selectedTableRows.value.length === 0) {
      showNotify({
        type: 'warning',
        message: t('未勾选作废物料件'),
      });
      return;
    }
    showInvalidModal.value = true;
  };
  const cancelInvalid = () => {
    tableProps.type = '';
    tableKey.value += 1;
    selectedTableRows.value = [];
    showInvalidModal.value = false;
  };

  const invalidConfirm = async () => {
    try {
      await invalidLiquidOutputApi({
        progressId: props.progressId,
        scrapStorageMaterialIdList: selectedTableRows.value.map(
          item => item.id,
        ),
        remark: invalidSignValue.value.remark,
        producerId: invalidSignValue.value.userId1,
        reCheckerId: invalidSignValue.value.userId2,
      });
      cancelInvalid();
      getResultDetail();
      initFillData2();
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };

  const handleReplace = () => {
    if (allSigned.value) {
      replaceValue.value = {
        userName1: '',
        loginName1: '',
        password1: '',
        userId1: '',
        userName2: '',
        loginName2: '',
        password2: '',
        userId2: '',
      };
      showReplaceModal.value = true;
      return;
    }
    showNotify({
      type: 'warning',
      message: t('已产出物料件需签名后才能更换'),
    });
  };

  const handleSign = () => {
    if (allSigned.value) {
      showNotify({
        type: 'warning',
        message: t('暂无物料需要签名确认'),
      });
      return;
    }
    signValue.value = {
      userName1: '',
      loginName1: '',
      password1: '',
      userId1: '',
      userName2: '',
      loginName2: '',
      password2: '',
      userId2: '',
      remark: '',
    };
    showSignModal.value = true;
  };
  const signConfirm = async () => {
    try {
      await signLiquidOutputApi({
        progressId: props.progressId,
        producerId: signValue.value.userId1,
        reCheckerId: signValue.value.userId2,
        remark: signValue.value.remark,
      });
      showSignModal.value = false;
      getResultDetail();
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  const quitConfirm = () => {
    showQuitModal.value = false;
    goBackToTargetPath();
  };

  const replaceConfirm = async () => {
    try {
      await changeLiquidOutputProducerApi({
        progressId: props.progressId,
        producerId: replaceValue.value.userId1,
        reCheckerId: replaceValue.value.userId2,
      });
      showReplaceModal.value = false;
      getResultDetail();
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };

  // 获取产出结果详情
  const getResultDetail = async () => {
    const res = await queryLiquidOutputProduceApi({
      progressId: props.progressId,
    });
    resultDetail.value = res.data;
    tableProps.data = res.data.produceRecordList;
    const {
      producerName,
      producerLoginName,
      reCheckerName,
      reCheckerLoginName,
      producerId,
      reCheckerId,
    } = resultDetail.value;
    labelList.value[0].options = labelList1.value[0].options = invalidLabelList.value[0].options = [
      {
        label: producerName,
        value: producerLoginName,
        id: producerId,
      },
    ];
    labelList.value[1].options = labelList1.value[1].options = invalidLabelList.value[1].options = [
      {
        label: reCheckerName,
        value: reCheckerLoginName,
        id: reCheckerId,
      },
    ];
  };

  // 获取新操作量人/新复核人列表
  const getReplaceList = async () => {
    // 新操作人
    postSingerListWithPermissionCodeAndComponentApi({
      componentId: props.componentId,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      productPlanId: urlQueryRef.value.productPlanId,
    }).then((res) => {
      replaceLabelList.value[0].options = res.data.map((item) => {
        return {
          label: item.userName,
          value: item.loginName,
          id: item.userId,
        };
      });
    });
    // 新复核人
    const params = {
      permissionCode: '121010001002021',
      componentId: props.componentId,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      productPlanId: urlQueryRef.value.productPlanId,
    };
    const res = await postSingerListWithPermissionCodeAndComponentApi(params);
    replaceLabelList.value[1].options = res.data.map((item) => {
      return {
        label: item.userName,
        value: item.loginName,
        id: item.userId,
      };
    });
  };

  onMounted(async () => {
    await getResultDetail();
    getReplaceList();
  });

  return {
    bmosPrinterInstance,
    resultDetail,
    infoItems,
    tableKey,
    tableRef,
    tableProps,
    selectedTableRows,
    showQuitModal,
    showSignModal,
    showInvalidModal,
    signValue,
    labelList,
    labelList1,
    replaceLabelList,
    showReplaceModal,
    replaceValue,
    invalidSignValue,
    invalidLabelList,
    leftClick,
    exitWeighing,
    handleReplace,
    handleSign,
    signConfirm,
    quitConfirm,
    replaceConfirm,
    invalidConfirm,
    handleInvalid,
    confirmInvalid,
    cancelInvalid,
    tableSelection,
  };
};
