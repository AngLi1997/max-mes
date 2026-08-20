import {
  reqPrintStorageMaterialTagApi,
  weighCenterExecuteChangeWeigher,
  weighCenterExecuteGetSingerListWithPermissionCodeAndStationIds,
  weighCenterExecuteQueryRecordResultByTaskIdApi,
  weighCenterExecuteSign,
} from '@/api';
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
  const segmentedOptions = ref([
    {
      label: t('物料称量'),
      value: 0,
    },
    {
      label: t('余料称量'),
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
      label: t('称量人'),
      // 签名动作
      signatureAction: 90,
      disabled: true,
      options: [],
    },
    {
      label: t('复核人'),
      // 签名动作
      signatureAction: 91,
      disabled: true,
      options: [],
    },
  ]);
  const labelList1 = ref([
    {
      label: t('原称量人'),
      // 签名动作
      signatureAction: 95,
      disabled: true,
      options: [],
    },
    {
      label: t('原复核人'),
      // 签名动作
      signatureAction: 95,
      disabled: true,
      options: [],
    },
  ]);

  const replaceLabelList = ref([
    {
      label: t('新称量人'),
      // 签名动作
      signatureAction: 95,
      options: [],
    },
    {
      label: t('新复核人'),
      // 签名动作
      signatureAction: 95,
      options: [],
    },
  ]);

  const tableRef = ref();
  const tableColProps = [
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
            type={row.signStatus.value === 1 ? 'success' : 'warning'}
          >
            {row.signStatus.label}
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
      prop: 'netWeight',
      label: t('净重'),
      width: 145,
    },
    {
      prop: 'tareWeight',
      label: t('皮重'),
      width: 120,
    },
    {
      prop: 'grossWeight',
      label: t('毛重'),
      width: 145,
    },
    {
      prop: 'unit',
      label: t('单位'),
      width: 90,
    },
    {
      prop: 'productName',
      label: t('产品名称'),
      width: 220,
    },
    {
      prop: 'productMergeCode',
      label: t('产品编码'),
      width: 200,
    },
    {
      prop: 'processName',
      label: t('工艺名称'),
      width: 300,
    },
    {
      prop: 'batchNo',
      label: t('生产批号'),
      width: 220,
    },
    {
      prop: 'weigherName',
      label: t('称量人'),
      width: 220,
      customRender: ({ row }) => {
        return (
          <span>
            {row.weigherName}
            -
            {row.weigherLoginName}
          </span>
        );
      },
    },
    {
      prop: 'reCheckerName',
      label: t('复核人'),
      width: 220,
      customRender: ({ row }) => {
        return (
          <span>
            {row.reCheckerName}
            -
            {row.reCheckerLoginName}
          </span>
        );
      },
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
      prop: 'weighTime',
      label: t('称量时间'),
      width: 223,
    },
    {
      prop: 'ACTION',
      label: '标签',
      width: 100,
      fixed: 'right',
      actions: ({ row }) => {
        return [
          {
            label: t('打印'),
            onClick: async () => {
              const device = bmosPrinterInstance.value.print();
              if (device) {
                try {
                  await reqPrintStorageMaterialTagApi({
                    deviceId: device.id,
                    sceneId:
                        row.categoryType.value === 0
                          ? 121001009
                          : 121002013,
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
  ];
  const tableProps = reactive({
    pagination: false,
    data: [],
    border: true,
    tableColProps,
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
    const { oddList, mainList } = resultDetail.value;
    const list = [...oddList, ...mainList];
    return list.every(item => item.signStatus.value === 1);
  });
  const leftClick = () => {
    uni.navigateBack();
  };
  const exitWeighing = () => {
    if (!allSigned.value) {
      showQuitModal.value = true;
    }
    else {
      goBackToTargetPath('pages/weighingCenter/list/index');
    }
  };

  const handleReplace = () => {
    if (resultDetail.value.weigherId && resultDetail.value.reCheckerId) {
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
    }
    else {
      showNotify({
        type: 'warning',
        message: t('未确定称量人员'),
      });
    }
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
  // 切换物料称量/余料称量
  const segmentedChange = () => {
    const { oddList, mainList } = resultDetail.value;
    tableProps.data = currentSegmented.value ? oddList : mainList;
    tableProps.tableColProps = currentSegmented.value === 0
      ? tableColProps
      : tableColProps.filter(item => !['productName', 'productMergeCode', 'processName', 'batchNo'].includes(item.prop));
  };
  // 获取称量结果详情
  const getResultDetail = async () => {
    const res = await weighCenterExecuteQueryRecordResultByTaskIdApi({
      taskId: props.id,
    });
    resultDetail.value = res.data;
    const {
      weigherName,
      weigherLoginName,
      reCheckerName,
      reCheckerLoginName,
      weigherId,
      reCheckerId,
    } = resultDetail.value;
    labelList.value[0].options = labelList1.value[0].options = [
      {
        label: weigherName,
        value: weigherLoginName,
        id: weigherId,
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
  const signConfirm = async () => {
    try {
      await weighCenterExecuteSign({
        taskId: props.id,
        weigherId: signValue.value.userId1,
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
    goBackToTargetPath('pages/weighingCenter/list/index');
  };

  const replaceConfirm = async () => {
    try {
      await weighCenterExecuteChangeWeigher({
        taskId: props.id,
        weigherId: replaceValue.value.userId1,
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

  // 获取新称量人/新复核人列表
  const getReplaceList = async () => {
    weighCenterExecuteGetSingerListWithPermissionCodeAndStationIds({
      stationIds: resultDetail.value.station,
    }).then((res) => {
      replaceLabelList.value[0].options = res.data.map((item) => {
        return {
          label: item.userName,
          value: item.loginName,
          id: item.userId,
        };
      });
    });
    weighCenterExecuteGetSingerListWithPermissionCodeAndStationIds({
      stationIds: resultDetail.value.station,
      permissionCode: '121020001000003',
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
