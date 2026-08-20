import {
  getReceiveBoundRequisition,
  getRequisitionList,
  // queryLiquidOutputTreeWithCargoPositionApi,
  getStorageConfigTreeApi,
  postRequisitionReceiveRepositoryBatch,
  scanWeighPositionCodeApi,
} from '@/api';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { t } from '@/utils/useBmosI18n';
import { useScan } from '@/utils/useScan.js';
import { nextTick, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import Icon from 'wot-design-uni/components/wd-icon/wd-icon.vue';

export const useModal = ({ UseParams, UseList }) => {
  const { showNotify } = useNotify();
  const { seg, paramsData, signatureData } = UseParams;
  const { formData, receiveListApi, checkboxValue } = UseList;
  const { bmosScanCode, init } = useScan();
  init();
  const getShowName = (arr, parentName = '') => {
    arr.map((item) => {
      let name = '';
      if (parentName) {
        name = `${parentName}/`;
      }
      name += item.name;
      cargoSpaceNameObj.value[item.id] = name;
      if (item.children.length) {
        getShowName(item.children, name);
      }
    });
  };
  const cargoSpaceNameObj = ref({});
  const signRef = ref();

  const isSpot = ref(false);
  // ref 打印设备
  const bmosPrinterInstance = ref(null);
  const deviceIdValue = ref(null);
  // ref领料单弹出框
  const materialModal = ref(false);
  // 领料单id
  const materialValueId = ref('');
  // 弹出框内容
  const formColumns = ref();
  const formRef = ref();
  const formProps = reactive({
    schemas: [
      {
        field: 'materialPositionId',
        component: 'BMFormSelect',
        label: t('货位'),
        colProps: {
          span: 24,
        },
        required: true,
        componentProps: ({ formModel }) => {
          return {
            request: async () => {
              const res = await getStorageConfigTreeApi(); // 原pzw接口queryLiquidOutputTreeWithCargoPositionApi
              const data = res.data || [];
              getShowName(data);
              return data;
            },
            type: 'tree',
            title: t('选择货位'),
            fieldNames: {
              name: 'name',
              key: 'id',
              checkKey: 'level.value',
              checkKeyValue: 4,
            },
            onConfirm: (data) => {
              // 弹框确定按钮
              seg.targetMaterialPositionId = data?.id;
              seg.targetMaterialPositionName = data?.name;
              formModel.materialPositionId = data?.id;
            },
          };
        },
        componentSlots: () => {
          // #ifdef H5
          return null;
          // #endif
          // #ifdef APP-PLUS
          // eslint-disable-next-line no-unreachable
          return {
            right: ({ formModel }) => {
              return (
                <Icon
                  name="saomiao"
                  size="14.06rpx"
                  color="#2871FF"
                  class-prefix="bmos-app-icon"
                  onClick={(e) => {
                    e.stopPropagation();
                    bmosScanCode({
                      success: async (res) => {
                        const { result } = res;
                        if (!result) {
                          return;
                        }
                        const type = result.slice(0, 2);
                        const code = result.slice(2);
                        if (type !== '03' || !code) {
                          return;
                        }
                        onScanSuccess(code);
                      },
                      fail: (err) => {
                        showNotify({
                          type: 'danger',
                          message: t('扫描失败'),
                        });
                      },
                    });
                  }}
                />
              );
            },
          };
          // #endif
        },
      },
    ],
  });
  const showSign = ref(false);
  const signValue = ref({
    loginName1: '',
    password1: '',
    userId1: '',
    loginName2: '',
    password2: '',
    userId2: '',
    remark: '',
  });
  const labelList = ref([
    {
      label: t('接收人'),
      signatureAction: 50,
    },
    {
      label: t('递交人'),
      signatureAction: 51,
      menuId: 121010001002002,
    },
  ]);
  // 打开material弹窗 领料单详情
  const materialOpen = async (soot) => {
    isSpot.value = soot;
    try {
      const apiParams = {
        batchId: paramsData.value?.productPlanId,
      };
      const res = await getRequisitionList(apiParams);
      formColumns.value = res.data;
      await materialApi();
    }
    catch (error) {
      // TODO handle the exception
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
      });
    }
  };
  // 领料单已选ID
  const materialApi = async () => {
    try {
      const apiParams = {
        productPlanId: paramsData.value?.productPlanId,
        componentId: paramsData.value?.id,
        copyVersion: getCurrentCopyRecordItem()?.version,
        procedureStepModelId: paramsData.value?.procedureStepModelId,
      };
      const res = await getReceiveBoundRequisition(apiParams);
      if (res.data) {
        seg.ordeId = res.data.id;
        seg.ordeName = res.data.name;
        await receiveListApi();
        isSpot.value
        && uni.showToast({
          title: t('领料单已接收物料，无法切换'),
          icon: 'none',
          duration: 2000,
        });
      }
      else {
        nextTick(() => {
          try {
            if (!isSpot.value && formColumns.value.length === 1) {
              confirm(formColumns.value[0]);
            }
            else {
              materialModal.value = true;
            }
          }
          catch (error) {
            materialModal.value = true;
          }
        });
      }
    }
    catch (error) {
      // TODO handle the exception
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
      });
    }
  };
  // 获取选择数据(领料单选择弹框确定)
  const confirm = async (data) => {
    if (!data)
      return;
    seg.ordeId = data.id;
    seg.ordeName = data.name;
    seg.selectedOrde = data.id;
    materialValueId.value = data.id;
    await receiveListApi();
  };
  // 取消
  const cancel = () => {
    if (seg.selectedOrde) {
      materialModal.value = false;
    }
    else {
      uni.navigateBack();
    }
    initFillData2();
  };

  // 扫描货位
  const handlePositionScan = async (code) => {
    try {
      scanWeighPositionCode(code);
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  // 获取货位信息
  const scanWeighPositionCode = async (materialNo) => {
    try {
      const res = await scanWeighPositionCodeApi({ code: materialNo });
      if (res.data) {
        seg.targetMaterialPositionId = res.data.id || '';
        seg.targetMaterialPositionName = res.data.fullName || '';
        formRef.value?.setFormModels({
          materialPositionId: res.data.id,
        });
      }
    }
    catch (error) {
      // TODO handle the exception
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
      });
    }
  };
  const onScanSuccess = (code) => {
    handlePositionScan(code);
  };
  const onScanFail = (result) => {
    showNotify({
      type: 'danger',
      message: t('扫描失败'),
    });
  };
  const onScanConfirm = (code) => {
    handlePositionScan(code);
  };
  const signatureFromSignature = ref({
    batchNo: urlQueryRef.value?.batchNo,
    componentId: paramsData.value?.id,
    copyVersion: getCurrentCopyRecordItem()?.version,
    procedureStepId: pageBasicDataRef.value?.procedureStepId,
    procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
    processId: urlQueryRef.value?.processId,
    processVersion: urlQueryRef.value?.processVersion,
    productPlanId: urlQueryRef.value?.productPlanId,
    recordItemId: pageBasicDataRef.value?.recordItemId,
    recordVersionId: pageBasicDataRef.value?.recordVersionId,
    requisitionId: seg.ordeId,
    cargoPositionId: seg.targetMaterialPositionId,
    reuse: pageBasicDataRef.value.reusable,
  });
  // 接收物料
  const submit = async () => {
    const boolArray = formData.value.map(
      item => item.receiveCompleted || false,
    );
    const allTrue = boolArray.every(item => item === true); // 都符合才返回true
    if (allTrue) {
      return uni.showToast({
        title: t('物料件已接收完毕'),
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
    if (checkboxValue.value.length === 0) {
      return uni.showToast({
        title: t('请选择物料批次'),
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
    // ToDo:更改为真实数据
    const res = await bmosPrinterInstance.value.print();
    if (res) {
      deviceIdValue.value = res.id;
      signatureData.value = {
        ...signatureFromSignature.value,
        idList: checkboxValue.value.map(item => item.id),
        deviceId: res.id,
        componentId: paramsData.value?.id,
        requisitionId: seg.ordeId,
        cargoPositionId: seg.targetMaterialPositionId,
      };
      showSign.value = true;
      signValue.value = {
        loginName1: '',
        password1: '',
        userId1: '',
        loginName2: '',
        password2: '',
        userId2: '',
        remark: '',
      };
    }
  };
  // 跳过
  const skipPrinter = () => {
    signatureData.value = {
      ...signatureFromSignature.value,
      idList: checkboxValue.value.map(item => item.id),
      componentId: paramsData.value?.id,
      requisitionId: seg.ordeId,
      cargoPositionId: seg.targetMaterialPositionId,
    };
    showSign.value = true;
    signValue.value = {
      loginName1: '',
      password1: '',
      userId1: '',
      loginName2: '',
      password2: '',
      userId2: '',
      remark: '',
    };
  };
  // 签名名单
  const signSubmit = async () => {
    const values = await formRef.value.validateFields();
    await signRef.value.checkSign();
    uni.showLoading({
      title: t('保存中...'),
      mask: true,
    });
    try {
      const apiParams = {
        ...signatureData.value,
        cargoPositionId: values.materialPositionId || seg.targetMaterialPositionId,
        senderId: signValue.value?.userId1,
        receiverId: signValue.value?.userId2,
      };
      await postRequisitionReceiveRepositoryBatch(apiParams);
      await receiveListApi();
      showSign.value = false;
      uni.hideLoading();
    }
    catch (error) {
      // TODO handle the exception
      uni.hideLoading();
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
      });
    }
  };
  return {
    formColumns,
    bmosPrinterInstance,
    materialValueId,
    materialModal,
    materialOpen,
    labelList,
    formRef,
    formProps,
    signRef,
    showSign,
    signValue,
    onScanSuccess,
    onScanFail,
    onScanConfirm,
    confirm,
    cancel,
    signSubmit,
    submit,
    skipPrinter,
  };
};
