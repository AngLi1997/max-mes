import { computed, ref, reactive } from 'vue';
import { t } from '@/utils/useBmosI18n.js';
import { useNotify } from 'wot-design-uni';
import {
  queryLiquidOutputProduceApi,
  scanLiquidOutputPreparationProduceContainerApi,
  scanLiquidOutputPreparationCargoCodeApi,
  queryLiquidOutputTreeWithCargoPositionApi,
  handleLiquidOutputApi,
  reqPrintStorageMaterialTagApi // 打码
} from '@/api';
import { onShow } from '@dcloudio/uni-app';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { initFillData2 } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';

export const useOutput = ({ props }) => {
  const { showNotify } = useNotify();

  const bmosPrinterInstance = ref(null);
  const detail = ref({});

  // 是否自动称量
  const auto = computed(() => props.mode === 'true');

  // 称量结果
  const showWeighingResult = ref(false);
  // 称量结果表格
  const resultTableRef = ref();
  const resultTableProps = reactive({
    pagination: false,
    data: [],
    border: true,
    tableColProps: [
      {
        prop: 'quantity',
        label: t('物料量'),
        width: 240
      },
      {
        prop: 'unit',
        label: t('单位'),
        width: 100
      },
      {
        prop: 'container',
        label: t('容器'),
        width: 414
      },
      {
        prop: 'position',
        label: t('暂存货位'),
        width: 414
      }
    ]
  });

  const infoItems = [
    {
      label: t('物料名称'),
      field: 'materialName',
      type: 'text'
    },
    {
      label: t('物料编码'),
      field: 'materialMergeCode',
      type: 'text'
    },
    {
      label: t('物料规格'),
      field: 'materialSpecification',
      type: 'text'
    },
    {
      label: t('物料批号'),
      field: 'storageMaterialBatchNo',
      type: 'text'
    }
  ];

  const formRef = ref();
  const formProps = reactive({
    schemas: [
      {
        field: 'quantity',
        component: 'Input',
        label: t('物料量'),
        colProps: {
          span: 20
        },
        componentProps: ({ formModel, formInstance }) => {
          return {
            placeholder: t('请输入'),
            type: 'number'
          };
        }
      },
      {
        field: 'unit',
        component: 'Input',
        label: t('单位'),
        colProps: {
          span: 4
        },
        componentProps: {
          disabled: true
        }
      }
    ]
  });

  // 扫描容器
  const scanContainerValue = ref('');
  const containerCode = ref('');
  // 扫描货位
  const scanPositionValue = ref('');
  const positionCode = ref('');
  const showPositionModal = ref(false);
  const treePositionData = ref([]);
  // 货位扫描选择
  const onPositionScanSelect = async() => {
    try {
      if (treePositionData.value.length === 0) {
        const res = await queryLiquidOutputTreeWithCargoPositionApi();
        treePositionData.value = res.data;
      }
      showPositionModal.value = true;
    } catch (error) {
      showNotify({
        type: 'danger',
        message: error.message
      });
    }
  };
  // 货位确认
  const confirmPosition = (data) => {
    if (data) {
      positionCode.value = data.positionCode;
      scanPositionValue.value = data.name;
    } else {
      positionCode.value = '';
      scanPositionValue.value = '';
    }
  };
  // 扫描容器
  const handleContainerScan = async(code) => {
    try {
      const res = await scanLiquidOutputPreparationProduceContainerApi({
        code: code
      });
      scanContainerValue.value = `${res.data.deviceCode}-${
        res.data.deviceName
      }`;
      containerCode.value = code;
    } catch (error) {
      showNotify({
        type: 'danger',
        message: error.message
      });
    }
  };
  // 扫描货位
  const handlePositionScan = async(code) => {
    try {
      const res = await scanLiquidOutputPreparationCargoCodeApi({
        code: code
      });
      scanPositionValue.value = `${res.data.code}-${res.data.name}`;
      positionCode.value = code;
    } catch (error) {
      showNotify({
        type: 'danger',
        message: error.message
      });
    }
  };
  // 扫描
  const handleScan = async(code, type) => {
    switch (type) {
      // 容器
      case 'container':
        handleContainerScan(code);
        break;
      // 货位
      case 'position':
        handlePositionScan(code);
        break;
      default:
        break;
    }
  };
  const onScanSuccess = (code, type) => {
    handleScan(code, type);
  };
  const onScanFail = (result) => {
    showNotify({
      type: 'danger',
      message: t('扫描失败')
    });
  };
  const onScanComplete = (result) => {
    console.log('onScanComplete', result);
  };
  const onScanConfirm = (code, type) => {
    handleScan(code, type);
  };

  const onScanClear = (_code, type) => {
    if (type === 'container') {
      containerCode.value = '';
      scanContainerValue.value = '';
    }
    else if (type === 'position') {
      positionCode.value = '';
      scanPositionValue.value = '';
    }
  };
  const handlePreviousStep = () => {
    toBack();
  };

  const handleWeigh = async() => {
    try {
        const formValues = formRef.value.getFormValues();
        const params = {
          progressId: props.progressId,
          quantity: formValues.quantity,
          deviceCode: containerCode.value,
          cargoPositionCode: positionCode.value,
          producerId: detail.value.producerId
        };
        const res = await handleLiquidOutputApi(params);
        initFillData2();
        resultTableProps.data = [
          {
            quantity: formValues.quantity,
            unit: detail.value.unit,
            container: scanContainerValue.value,
            position: scanPositionValue
          }
        ];
        showWeighingResult.value = true;
        return res;
    } catch (error) {
      error.message && showNotify({
        type: 'danger',
        message: error.message
      });
        return false;
    }
  };

  const handleNextStep = async() => {
    // 校验当前登录人和称量人是否一致
    const currentUser = getStorageSync(USER_INFO) || {};
    const { userId } = currentUser;
    if (detail.value.producerId !== userId) {
      showNotify({
        message: t('登录账号与操作人不符'),
        type: 'danger'
      });
      return;
    }
    const formValues = formRef.value.getFormValues();
    try {
      if (isNaN(Number(formValues.quantity))) {
        showNotify({
          type: 'danger',
          message: t('请输入正确的物料量')
        });
        return;
      }
      if (Number(formValues.quantity) <= 0) {
        showNotify({
          type: 'warning',
          message: t('产出结果必须大于0')
        });
        return;
      }

      const device = bmosPrinterInstance.value.print();
      if (device) {
        const res = await handleWeigh();
        // 称量成功后打码
        if (res) {
          reqPrintStorageMaterialTagApi({
            deviceId: device.id,
            sceneId: 121002010,
            body: {
              no: res.data
            }
          });
        }
      }
    } catch (error) {
      showNotify({
        type: 'danger',
        message: error.message
      });
    }
  };

  const toBack = () => {
    uni.navigateBack();
  };

  const toResult = () => {
    showWeighingResult.value = false;
    uni.navigateTo({
      url: `/pages/businessComponents/liquidOutput/result/index?progressId=${
        props.progressId
      }&componentId=${props.componentId}`
    });
  };

  // 获取产出信息
  const getLiquidOutputProduce = async() => {
    try {
      const res = await queryLiquidOutputProduceApi({
        progressId: props.progressId
      });
      detail.value = res.data;
      formRef.value.setFormModel('unit', res.data.unit);
    } catch (error) {
      showNotify({
        type: 'danger',
        message: error.message
      });
    }
  };

  const resultConfirm = () => {
    formRef.value.setFormModel('quantity', '');
    scanContainerValue.value = '';
    containerCode.value = '';
    scanPositionValue.value = '';
    positionCode.value = '';
    showWeighingResult.value = false;
  };

  onShow(() => {
    getLiquidOutputProduce();
  });

  return {
    bmosPrinterInstance,
    auto,
    infoItems,
    detail,
    formRef,
    formProps,
    resultTableRef,
    resultTableProps,
    showWeighingResult,
    scanContainerValue,
    containerCode,
    scanPositionValue,
    positionCode,
    showPositionModal,
    treePositionData,
    toBack,
    toResult,
    handlePreviousStep,
    handleNextStep,
    onScanSuccess,
    onScanFail,
    onScanComplete,
    onScanConfirm,
    onScanClear,
    onPositionScanSelect,
    confirmPosition,
    resultConfirm,
    handleWeigh
  };
};
