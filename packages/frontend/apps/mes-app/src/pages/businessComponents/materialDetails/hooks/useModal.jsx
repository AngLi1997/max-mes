import {
  // queryLiquidOutputTreeWithCargoPositionApi,
  getStorageConfigTreeApi,
  postRequisitionReceiveRepositoryMaterial,
  scanWeighPositionCodeApi,
} from '@/api';
import {
  getCurrentCopyRecordItem,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import {
  t,
} from '@/utils/useBmosI18n';
import { useScan } from '@/utils/useScan.js';
import {
  reactive,
  ref,
} from 'vue';
import { useNotify } from 'wot-design-uni';

import Icon from 'wot-design-uni/components/wd-icon/wd-icon.vue';

export const useModal = ({
  UseSubTab,
  UseTable,
}) => {
  const { showNotify } = useNotify();
  const { bmosScanCode, init } = useScan();
  init();
  const cargoSpaceNameObj = ref({});
  const signRef = ref();

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
  const {
    seg,
    paramsData,
    signatureData,
  } = UseSubTab;
  const {
    tableData,
    selectedList,
    apiDetailsList,
  } = UseTable;
  // 弹出框ref
  const bmosPrinterInstance = ref(null);
  const deviceIdValue = ref();
  const showSign = ref(false);
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
  const labelList = ref([{
    label: t('接收人'),
    signatureAction: 50,
  }, {
    label: t('递交人'),
    signatureAction: 51,
    menuId: 121010001002002,
  }]);
  // 接收物料
  const receiveMaterial = () => {
    const boolArray = tableData.value.map(item => item.isDisabled || false);
    const allTrue = boolArray.every(item => item === true);
    if (allTrue) {
      return uni.showToast({
        title: t('物料件已接收完毕'),
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
    if (selectedList.value.length === 0) {
      return uni.showToast({
        title: t('请选择物料件'),
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
    const res = bmosPrinterInstance.value.print();
    if (res) {
      deviceIdValue.value = res.id;
      signatureData.value = {
        batchNo: urlQueryRef.value?.batchNo,
        componentId: paramsData.value?.componentId,
        copyVersion: getCurrentCopyRecordItem()?.version,
        procedureStepId: pageBasicDataRef.value?.procedureStepId,
        procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
        processId: urlQueryRef.value?.processId,
        processVersion: urlQueryRef.value?.processVersion,
        productPlanId: urlQueryRef.value?.productPlanId,
        recordItemId: pageBasicDataRef.value?.recordItemId,
        recordVersionId: pageBasicDataRef.value?.recordVersionId,
        requisitionId: paramsData.value?.requisitionId,
        cargoPositionId: seg.targetMaterialPositionId,
        idList: selectedList.value.map(item => item.id),
        reuse: pageBasicDataRef.value.reusable,
        materialBatchId: paramsData.value?.id,
        deviceId: res.id,
      };
      showSign.value = true;
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
    }
  };
    // 跳过
  const skipPrinter = () => {
    signatureData.value = {
      batchNo: urlQueryRef.value?.batchNo,
      componentId: paramsData.value?.componentId,
      copyVersion: getCurrentCopyRecordItem()?.version,
      procedureStepId: pageBasicDataRef.value?.procedureStepId,
      procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
      processId: urlQueryRef.value?.processId,
      processVersion: urlQueryRef.value?.processVersion,
      productPlanId: urlQueryRef.value?.productPlanId,
      recordItemId: pageBasicDataRef.value?.recordItemId,
      recordVersionId: pageBasicDataRef.value?.recordVersionId,
      requisitionId: paramsData.value?.requisitionId,
      cargoPositionId: seg.targetMaterialPositionId,
      idList: selectedList.value.map(item => item.id),
      reuse: pageBasicDataRef.value.reusable,
      materialBatchId: paramsData.value?.id,
    };
    showSign.value = true;
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
  };
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
        senderId: signValue.value?.userId1,
        receiverId: signValue.value?.userId2,
        cargoPositionId: values.materialPositionId || seg.targetMaterialPositionId,
      };
      await postRequisitionReceiveRepositoryMaterial(apiParams);
      showSign.value = false;
      uni.hideLoading();
      await apiDetailsList();
    }
    catch (error) {
      // TODO handle the exception
      uni.hideLoading();
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
      });
    }
  };

  const showPositionModal = ref(false);
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
  // 货位
  const selectProduct = (treeDataMap, props) => {
    seg.targetMaterialPositionId = props.id;
    seg.targetMaterialPositionName = props.name;
  };
  // 获取货位信息
  const scanWeighPositionCode = async (materialNo) => {
    try {
      const res = await scanWeighPositionCodeApi({ code: materialNo });
      if (res.data) {
        seg.targetMaterialPositionId = res?.data.id || '';
        seg.targetMaterialPositionName = res?.data.fullName || '';
        formRef.value?.setFormModels({
          materialPositionId: res.data.id,
        });
      }
    }
    catch (error) {
      // TODO handle the exception
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };
  const onScanSuccess = (code) => {
    handlePositionScan(code);
  };
  const onScanComplete = (result) => {
    console.log('onScanComplete', result);
  };
  const onScanConfirm = (code) => {
    handlePositionScan(code);
  };

  return {
    formRef,
    formProps,
    signRef,
    bmosPrinterInstance,
    labelList,
    showSign,
    signValue,
    showPositionModal,
    onScanSuccess,
    onScanComplete,
    onScanConfirm,
    receiveMaterial,
    selectProduct,
    signSubmit,
    skipPrinter,
  };
};
