import {
  changeWeigherOutputApi,
  getOutputWeighProcessApi,
  reqPrintStorageMaterialTagApi,
  scrapOutputApi,
  signOutputApi,
} from '@/api';
import {
  getCurrentCopyRecordItem,
  goBackToTargetPath,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { useOutputWeighingStore } from '@/stores/businessComponents/outputWeighing/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { storeToRefs } from 'pinia';
import { computed, onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdTag from 'wot-design-uni/components/wd-tag/wd-tag.vue';

export const useTable = (props) => {
  const outputWeighingStore = useOutputWeighingStore();
  const { reCheckerList, weighingPersonList }
    = storeToRefs(outputWeighingStore);
  const { setDetailData } = outputWeighingStore;
  const { showNotify } = useNotify();
  // 展示退出弹窗
  const showQuitModal = ref(false);

  // 表格key
  const tableKey = ref(0);

  const infoItems = [
    {
      label: t('称量人'),
      field: ['weigherName', 'weigherLoginName'],
      type: 'text',
    },
    {
      label: t('复核人'),
      field: ['reCheckerName', 'reCheckerLoginName'],
      type: 'text',
    },
  ];
  // 状态map 已签名、未签名、已作废
  const statusArr = ['warning', 'success', 'danger'];
  const detailData = ref({});

  const params = reactive({
    componentId: props.componentId,
    copyVersion: getCurrentCopyRecordItem().version,
    procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
    productPlanId: urlQueryRef.value.productPlanId,
  });
  const bmosPrinterInstance = ref();
  const tableRef = ref();
  const tableProps = reactive({
    pagination: false,
    data: [],
    border: true,
    type: '',
    selectionProps: (row) => {
      return {
        disabled: row.signStatus.value !== 0,
      };
    },
    tableColProps: [
      {
        prop: 'INDEX',
        label: t('序号'),
        width: 80,
      },
      {
        prop: 'signStatus',
        label: t('状态'),
        width: 108,
        customRender: ({ row }) => {
          return (
            <WdTag
              plain
              type={statusArr[row.signStatus.value]}
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
        prop: 'netWeight',
        label: t('净重'),
        width: 120,
      },
      {
        prop: 'tareWeight',
        label: t('皮重'),
        width: 145,
      },
      {
        prop: 'grossWeight',
        label: t('毛重'),
        width: 145,
      },

      {
        prop: 'weigherFullName',
        label: t('产出人'),
        width: 220,
      },
      {
        prop: 'reCheckerFullName',
        label: t('复核人'),
        width: 200,
      },
      {
        prop: 'containerName',
        label: t('容器'),
        width: 300,
      },
      {
        prop: 'materialPositionName',
        label: t('货位'),
        width: 220,
      },
      {
        prop: 'weighTime',
        label: t('产出时间'),
        width: 223,
      },
      {
        prop: 'ACTION',
        label: '标签',
        width: 100,
        actions: ({ row }) => {
          return [
            {
              label: '打印',
              onClick: () => {
                print(row.storageMaterialNo);
              },
            },
          ];
        },
      },
    ],
  });

  const selectData = ref([]);

  const signOpen1 = ref(false);
  const signOpen3 = ref(false);
  const signOpen4 = ref(false);
  const signValue1 = ref({
    userName1: '',
    userName2: '',
    loginName1: '',
    loginName2: '',
    password1: '',
    password2: '',
    userId1: '',
    userId2: '',
  });
  const signValue2 = ref({
    userName1: '',
    userName2: '',
    loginName1: '',
    loginName2: '',
    password1: '',
    password2: '',
    userId1: '',
    userId2: '',
  });
  const signValue3 = ref({
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
  const signValue4 = ref({
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
  const signatureData1 = computed(() => {
    return {
      outputWeighProcessId: detailData.value.id,
    };
  });
  const signatureData3 = computed(() => {
    return {
      outputWeighProcessId: detailData.value.id,
      weigherId: signValue3.value.userId1,
      reCheckerId: signValue3.value.userId2,
      remark: signValue3.value.remark,
    };
  });
  const signatureData4 = ref({});

  // 作废按钮点击
  const handleVoid = () => {
    selectData.value = [];
    tableProps.type = 'selection';
    tableKey.value += 1;
  };
  // 取消作废
  const handleCancelSelect = () => {
    selectData.value = [];
    tableProps.type = '';
    tableKey.value += 1;
  };

  const tableSelectChange = (selection) => {
    selectData.value = selection;
  };
  // 作废
  const handleConfirmVoid = () => {
    if (selectData.value.length === 0) {
      showNotify({ message: t('未勾选作废物料'), type: 'danger' });
      return;
    }
    const ids = selectData.value.map(item => item.id);
    signatureData4.value = {
      outputWeighProcessId: detailData.value.id,
      scrapStorageMaterialIdList: ids,
    };
    // 作废签名弹窗
    signOpen4.value = true;
  };
  // 确认作废
  const handleScrap = async () => {
    try {
      await scrapOutputApi({
        weigherId: signValue4.value.userId1,
        reCheckerId: signValue4.value.userId2,
        remark: signValue4.value.remark,
        ...signatureData4.value,
      });
      showNotify({ message: t('作废成功'), type: 'success' });
      getResultList();
      initFillData2();
    }
    catch (error) {
      error.message && showNotify({ message: error.message, type: 'warning' });
    }
    signOpen4.value = false;
  };
  // 作废签名确认
  const signConfirm4 = async () => {
    signOpen4.value = false;
    try {
      await handleScrap();
      handleCancelSelect();
    }
    catch (error) {
      error.message
      && showNotify({
        message: error.message,
        type: 'warning',
      });
    }
  };

  const labelList1 = computed(() => {
    return [
      {
        label: t('产出人'),
        signatureAction: 75,
        disabled: true,
        options: [
          {
            userName: `${detailData.value.weigherName}`,
            loginName: detailData.value.weigherLoginName,
            userId: detailData.value.weigherId,
          },
        ],
      },
      {
        label: t('复核人'),
        signatureAction: 75,
        disabled: true,
        options: [
          {
            userName: `${detailData.value.reCheckerName}`,
            loginName: detailData.value.reCheckerLoginName,
            userId: detailData.value.reCheckerId,
          },
        ],
      },
    ];
  });
  const labelList2 = ref([
    {
      label: t('新产出人'),
      signatureAction: 75,
      options: weighingPersonList,
    },
    {
      label: t('新复核人'),
      signatureAction: 75,
      options: reCheckerList,
    },
  ]);
  const labelList3 = computed(() => {
    return [
      {
        label: t('产出人'),
        signatureAction: 56,
        disabled: true,
        options: [
          {
            label: `${detailData.value.weigherName}`,
            value: detailData.value.weigherLoginName,
            id: detailData.value.weigherId,
          },
        ],
      },
      {
        label: t('复核人'),
        signatureAction: 57,
        disabled: true,
        options: [
          {
            label: `${detailData.value.reCheckerName}`,
            value: detailData.value.reCheckerLoginName,
            id: detailData.value.reCheckerId,
          },
        ],
      },
    ];
  });
  const labelList4 = computed(() => {
    return [
      {
        label: t('产出人'),
        signatureAction: 76,
        disabled: true,
        options: [
          {
            label: `${detailData.value.weigherName}`,
            value: detailData.value.weigherLoginName,
            id: detailData.value.weigherId,
          },
        ],
      },
      {
        label: t('复核人'),
        signatureAction: 77,
        disabled: true,
        options: [
          {
            label: `${detailData.value.reCheckerName}`,
            value: detailData.value.reCheckerLoginName,
            id: detailData.value.reCheckerId,
          },
        ],
      },
    ];
  });
  // 获取结果详情
  async function getResultList() {
    try {
      const res = await getOutputWeighProcessApi(params);
      detailData.value = res.data || {};
      setDetailData(detailData.value);
      tableProps.data = res.data ? res.data.weightRecordList || [] : [];
    }
    catch (error) {
      error.message && showNotify({ message: error.message, type: 'warning' });
    }
  }
  // 打印
  function print(no) {
    const device = bmosPrinterInstance.value.print();
    if (device) {
      reqPrintStorageMaterialTagApi({
        deviceId: device.id,
        sceneId: 121002007,
        body: {
          no,
        },
      });
    }
  }

  // 签名
  const sign = () => {
    if (tableProps.data.every(item => item.signStatus.value !== 0)) {
      showNotify({ message: t('暂无物料需要签名确认'), type: 'danger' });
      return;
    }
    signOpen3.value = true;
  };
  // 签名确认
  const signConfirm3 = async () => {
    signOpen3.value = false;
    try {
      await signOutputApi(signatureData3.value);
      getResultList();
    }
    catch (error) {
      error.message && showNotify({ message: error.message, type: 'warning' });
    }
  };

  // 更换操作人
  const handleChangeSign = () => {
    if (tableProps.data.some(item => item.signStatus.value === 0)) {
      showNotify({
        message: t('已产出物料件需签名后才能更换'),
        type: 'warning',
      });
      return;
    }
    signValue1.value = {
      userId1: detailData.value.weigherId,
      userId2: detailData.value.reCheckerId,
      password1: '',
      password2: '',
    };
    signValue2.value = {
      userId1: '',
      userName1: '',
      loginName1: '',
      userId2: '',
      userName2: '',
      loginName2: '',
      password1: '',
      password2: '',
    };
    signOpen1.value = true;
  };
  // 更换操作人签名确认
  const signConfirm1 = async () => {
    try {
      await changeWeigherOutputApi({
        outputWeighProcessId: detailData.value.id,
        weigherId: signValue2.value.userId1,
        reCheckerId: signValue2.value.userId2,
      });
      getResultList();
      signOpen1.value = false;
    }
    catch (error) {
      error.message && showNotify({ message: error.message, type: 'warning' });
    }
  };

  // 关闭产出组件
  const close = () => {
    if (tableProps.data.every(item => item.signStatus.value !== 0)) {
      goBackToTargetPath();
    }
    else {
      showQuitModal.value = true;
    }
  };

  // 返回
  const toBack = () => {
    uni.navigateBack();
  };

  onMounted(() => {
    getResultList();
  });
  return {
    bmosPrinterInstance,
    showQuitModal,
    infoItems,
    tableRef,
    tableProps,
    tableKey,
    detailData,
    selectData,
    goBackToTargetPath,
    signOpen1,
    signOpen3,
    signOpen4,
    signValue1,
    signValue2,
    signValue3,
    signValue4,
    labelList1,
    labelList2,
    labelList3,
    labelList4,
    signatureData1,
    signatureData3,
    signatureData4,
    toBack,
    close,
    sign,
    handleChangeSign,
    handleVoid,
    handleCancelSelect,
    handleConfirmVoid,
    handleScrap,
    signConfirm1,
    signConfirm3,
    signConfirm4,
    tableSelectChange,
  };
};
