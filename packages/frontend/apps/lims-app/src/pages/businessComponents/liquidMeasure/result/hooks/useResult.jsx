import {
  changeMeasurerApi,
  getLiquidMeasureResultApi,
  measureSignApi,
  postSingerListWithPermissionCodeAndComponentApi,
  reqPrintStorageMaterialTagApi,
} from '@/api';
import {
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
      label: t('量取人'),
      field: ['measurerName', 'measurerLoginName'],
      type: 'text',
    },
    {
      label: t('复核人'),
      field: ['reCheckerName', 'reCheckerLoginName'],
      type: 'text',
    },
  ];
  const segmentedOptions = ref([
    {
      label: t('配液量取'),
      value: 0,
    },
    {
      label: t('余液量取'),
      value: 1,
    },
  ]);
  const currentSegmented = ref(0);
  const showQuitModal = ref(false);
  const showSignModal = ref(false);

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
      label: t('量取人'),
      // 签名动作
      signatureAction: 99,
      disabled: true,
      options: [],
    },
    {
      label: t('复核人'),
      // 签名动作
      signatureAction: 100,
      disabled: true,
      options: [],
    },
  ]);
  const labelList1 = ref([
    {
      label: t('原量取人'),
      // 签名动作
      signatureAction: 131,
      disabled: true,
      options: [],
    },
    {
      label: t('原复核人'),
      // 签名动作
      signatureAction: 131,
      disabled: true,
      options: [],
    },
  ]);

  const replaceLabelList = ref([
    {
      label: t('新量取人'),
      // 签名动作
      signatureAction: 131,
      options: [],
    },
    {
      label: t('新复核人'),
      // 签名动作
      signatureAction: 131,
      options: [],
    },
  ]);

  const tableRef = ref();

  const tableProps = reactive({
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
              type={row.signStatus.value ? 'success' : 'warning'}
            >
              {row.signStatus.name}
            </WdTag>
          );
        },
      },
      {
        prop: 'materialMergeCode',
        label: t('物料编码'),
        width: 160,
      },
      {
        prop: 'materialName',
        label: t('物料名称'),
        width: 180,
      },
      {
        prop: 'materialBatchNo',
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
        prop: 'unitName',
        label: t('单位'),
        width: 90,
      },
      {
        prop: 'measurerName',
        label: t('量取人'),
        width: 220,
      },
      {
        prop: 'reCheckerName',
        label: t('复核人'),
        width: 220,
      },
      {
        prop: 'containerName',
        label: t('容器'),
        width: 220,
      },
      {
        prop: 'positionName',
        label: t('货位'),
        width: 220,
      },
      {
        prop: 'measureTime',
        label: t('量取时间'),
        width: 223,
      },
      {
        prop: 'ACTION',
        label: '标签',
        width: 80,
        actions: ({ row }) => {
          return [
            {
              label: '打印',
              onClick: async () => {
                const device = bmosPrinterInstance.value.print();
                if (device) {
                  try {
                    await reqPrintStorageMaterialTagApi({
                      deviceId: device.id,
                      sceneId:
                          row.categoryType.value === 0
                            ? 121001006
                            : 121002009,
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
  });

  const showReplaceModal = ref(false);
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
    const { measureList, oddMeasureList } = resultDetail.value;
    const list = [...measureList, ...oddMeasureList];
    return list.every(item => item.signStatus.value);
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
      message: t('已称量物料件需签名后才能更换'),
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
      await measureSignApi({
        measureInstanceId: props.id,
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
      await changeMeasurerApi({
        measureInstanceId: props.id,
        measurerId: replaceValue.value.userId1,
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

  // 获取称量结果详情
  const getResultDetail = async () => {
    const res = await getLiquidMeasureResultApi({
      id: props.id,
    });
    resultDetail.value = res.data;
    const {
      measurerName,
      measurerLoginName,
      reCheckerName,
      reCheckerLoginName,
      measurerId,
      reCheckerId,
    } = resultDetail.value;
    labelList.value[0].options = labelList1.value[0].options = [
      {
        label: measurerName,
        value: measurerLoginName,
        id: measurerId,
      },
    ];
    labelList.value[1].options = labelList1.value[1].options = [
      {
        label: reCheckerName,
        value: reCheckerLoginName,
        id: reCheckerId,
      },
    ];
    segmentedChange();
  };

  // 切换物料称量/余料称量
  const segmentedChange = () => {
    const { measureList, oddMeasureList } = resultDetail.value;
    tableProps.data = currentSegmented.value ? oddMeasureList : measureList;
  };

  // 获取新称量人/新复核人列表
  const getReplaceList = async () => {
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
    postSingerListWithPermissionCodeAndComponentApi({
      permissionCode: '121010001002016',
      componentId: props.componentId,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      productPlanId: urlQueryRef.value.productPlanId,
    }).then((res) => {
      replaceLabelList.value[1].options = res.data.map((item) => {
        return {
          label: item.userName,
          value: item.loginName,
          id: item.userId,
        };
      });
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
    segmentedOptions,
    currentSegmented,
    tableRef,
    tableProps,
    showQuitModal,
    showSignModal,
    signValue,
    labelList,
    labelList1,
    replaceLabelList,
    showReplaceModal,
    replaceValue,
    leftClick,
    exitWeighing,
    handleReplace,
    handleSign,
    signConfirm,
    quitConfirm,
    replaceConfirm,
    segmentedChange,
  };
};
