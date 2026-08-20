import { getAllUnitApi, saveWeighDataApi } from '@/api';
import { initFillData2, urlQueryRef } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { numberValidator } from '@/utils/numberValidator.js';
import { useBalanceSocket } from '@/utils/useBalanceSocket';
import { t } from '@/utils/useBmosI18n.js';
import { onHide, onShow } from '@dcloudio/uni-app';
import { values } from 'lodash-es';
import { storeToRefs } from 'pinia';
import { computed, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useMaterialWeighing = () => {
  const weighingMachineStore = useWeighingMachineStore();
  const { selectedBalance } = storeToRefs(weighingMachineStore);
  const query = ref({});
  const formRef = ref();
  const confirmLoading = ref(false);
  // 表单配置
  const formProps = reactive({
    schemas: [
      {
        field: 'weight',
        component: 'Input',
        label: t('秤具示数'),
        componentProps: {
          type: 'number',
        },
        colProps: {
          span: 18,
        },
        dynamicRules: () => {
          return [
            {
              validator: (val) => {
                if (!val)
                  return Promise.reject(t('请输入物料量'));
                return numberValidator(val, true);
              },
            },
          ];
        },
      },
      {
        field: 'unitId',
        label: t('单位'),
        component: 'BMFormSelect',
        colProps: {
          span: 6,
        },
        componentProps: ({ formModel }) => {
          return {
            title: t('选择单位'),
            type: 'tree',
            mode: 'single',
            fieldNames: {
              name: 'unitName',
              key: 'unitId',
              checkKey: 'extend',
              checkKeyValue: true,
              children: 'children',
            },
            request: async () => {
              try {
                // 获取所有单位树
                const { data } = await getAllUnitApi();
                return buildUnitTree(data);
              }
              catch (error) {
                console.log('error', error);
                return [];
              }
            },
            onConfirm: (val) => {
              formModel.unit = val.unitName;
            },
          };
        },
        dynamicRules: () => {
          return [
            {
              validator: (val) => {
                if (!val)
                  return Promise.reject(t('请选择单位'));
                return true;
              },
            },
          ];
        },
      },
    ],
  });

  const timer = ref(null);
  const { showNotify } = useNotify();

  const resultTableRef = ref();
  const resultTableProps = reactive({
    pagination: false,
    data: [],
    border: true,
    tableColProps: [
      {
        prop: 'weight',
        label: t('秤具示数'),
        width: 240,
      },
      {
        prop: 'unit',
        label: t('单位'),
        width: 240,
      },
    ],
  });

  // 是否自动称量
  const auto = computed(() => query.value.mode === '0');
  const socketParams = computed(() => {
    return {
      auto: auto.value,
      batchNo: urlQueryRef.value.batchNo,
      productName: urlQueryRef.value.productName,
    };
  });
  const {
    weightInfo,
    isBackMessage,
    getReadings,
  } = useBalanceSocket(socketParams);

  // 称量结果
  const showWeighingResult = ref(false);

  const handleWeigh = async () => {
    let params = {};
    if (auto.value) {
      params = {
        weight: weightInfo.weight,
        unit: selectedBalance.value.unit,
        unitId: selectedBalance.value.unitId,
      };
    }
    else {
      params = await formRef.value.validate();
    }

    confirmLoading.value = true;
    try {
      await saveWeighDataApi({
        weight: params.weight,
        unitId: params.unitId,
        componentInstanceId: query.value.id,
        mode: query.value.mode,
      });
      resultTableProps.data = [params];
      showWeighingResult.value = true;
      initFillData2();
    }
    catch (error) {
      error.message && showNotify({
        message: error.message,
        type: 'danger',
      });
    }
    confirmLoading.value = false;
  };

  // 上一步
  const handlePreviousStep = () => {
    uni.navigateBack();
  };
  // 下一步
  const handleNextStep = async () => {
    if (auto.value) {
      if (weightInfo.weight <= 0) {
        showNotify({
          message: t('称量结果必须大于0'),
          type: 'danger',
        });
        return;
      }
      handleWeigh();
    }
    else {
      await formRef.value.validate();
      handleWeigh();
    }
  };

  // 构建单位树
  function buildUnitTree(data) {
    const unit = values(data.unit);
    const existUnit = values(data.existUnit);
    const unitTree = unit;
    const unitMap = new Map();
    unit.forEach((unit, index) => {
      unit.children = [{ ...unit, unitName: `${unit.unitName}(${t('标准单位')})`, extend: true }];
      unitMap.set(unit.unitId, unit);
      unit.unitId = index;
    });
    existUnit.forEach((unit) => {
      const parent = unitMap.get(unit.parentUnitId);
      if (parent) {
        parent.children.push(unit);
      }
    });
    return unitTree;
  }

  const toBack = () => {
    uni.navigateBack();
  };

  const toResult = () => {
    uni.navigateTo({
      url: `/pages/businessComponents/weighingData/result/index?id=${query.value.id}`,
    });
  };
  // 继续称量
  const handleResultConfirm = () => {
    showWeighingResult.value = false;
    if (!auto.value) {
      formRef.value.resetForm();
    }
  };
  // 完成称量
  const handleResultFinish = () => {
    handleResultConfirm();
    toResult();
  };

  onShow(() => {
    timer.value = setInterval(() => {
      if (weightInfo.open && isBackMessage.value) {
        getReadings();
      }
    }, 200);
  });
  onHide(() => {
    if (timer.value) {
      clearInterval(timer.value);
      timer.value = null;
    }
  });
  return {
    auto,
    query,
    selectedBalance,
    formRef,
    formProps,
    weightInfo,
    showWeighingResult,
    resultTableRef,
    resultTableProps,
    confirmLoading,
    toBack,
    toResult,
    handlePreviousStep,
    handleNextStep,
    handleWeigh,
    handleResultFinish,
    handleResultConfirm,
  };
};
