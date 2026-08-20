import {
  confirmLiquidOutputApi,
  getLiquidOutputMaterialApi,
  getLiquidOutputPlanListApi,
  getLiquidOutputProgressApi,
  postSingerListWithPermissionCodeAndComponentApi,
  queryLiquidOutputMaterialBatchApi,
} from '@/api';
import {
  getCurrentCopyRecordItem,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { t } from '@/utils/useBmosI18n.js';
import { format } from 'date-fns';
import { computed, onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useExecution = ({ props }) => {
  const { showNotify } = useNotify();
  const instance = ref({ planVO: {}, batchVO: {} });
  const planVO = ref(null);
  const batchVO = ref(null);
  const formRef = ref();

  // 是否disabled确认按钮
  const disabledConfirm = ref(false);

  const formProps = reactive({
    schemas: [
      {
        field: 'formulaMaterialId',
        vIf: false,
      },
      {
        field: 'formTitle1',
        component: 'FormTitle',
        label: t('产出批次'),
        colProps: {
          span: 24,
        },
        // 自定义组件背景色
        componentProps: {
          color: '#fff',
        },
      },
      {
        field: 'materialName',
        component: 'Input',
        label: t('物料名称'),
        colProps: {
          span: 12,
        },
        required: true,
        componentProps: {
          placeholder: t('自动填入'),
          disabled: true,
        },
      },
      {
        field: 'materialMergeCode',
        component: 'Input',
        label: t('物料编码'),
        componentProps: {
          placeholder: t('自动填入'),
          disabled: true,
        },
      },
      {
        field: 'materialSpecification',
        component: 'Input',
        label: t('物料规格'),
        componentProps: {
          placeholder: t('自动填入'),
          disabled: true,
        },
      },
      {
        field: 'materialBatchNo',
        component: 'Input',
        label: t('物料批号'),
        required: true,
        componentProps: ({ formModel, formInstance }) => {
          return {
            onBlur: async ({ value }) => {
              if (!value) {
                return;
              }
              const materialBatchRes = await queryLiquidOutputMaterialBatchApi({
                materialBatchNo: formModel.materialBatchNo,
                formulaMaterialId: formModel.formulaMaterialId,
              });
              disabledConfirm.value = false;
              const { expireDate, materialBatchNo } = materialBatchRes.data;
              formInstance.setFieldsValue({
                expireDate: expireDate ? new Date(expireDate).getTime() : null,
              });
              formInstance.updateSchema({
                field: 'expireDate',
                componentProps: {
                  disabled: !!materialBatchNo,
                },
              });
            },
            onFocus: () => {
              disabledConfirm.value = true;
            },
          };
        },
      },
      {
        field: 'expireDate',
        component: 'BMFormDatePicker',
        label: t('有效期至'),
        componentProps: {
          formatDate: 'yyyy-MM-dd',
          title: t('有效期至'),
          minDate: new Date(),
        },
        required: true,
      },
    ],
  });

  const handleSwitchIngredient = () => {
    if (instance.value.produceStorageMaterialFlg) {
      showNotify({
        message: t('已确认配液单，无法切换'),
        type: 'danger',
      });
      return;
    }
    showSwitchingLiquidPreparationList.value = true;
  };

  const infoItems = [
    {
      label: t('配液单'),
      field: 'name',
      type: 'text',
    },
    { label: t('切换配液单'), type: 'button', click: handleSwitchIngredient },
  ];

  const showSign = ref(false);
  const signValue = ref({
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
      signatureAction: 103,
      disabled: true,
    },
    {
      label: t('复核人'),
      // 签名动作
      signatureAction: 104,
      disabled: false,
    },
  ]);

  const showSwitchingLiquidPreparationList = ref(false);
  const liquidPreparationListValue = ref('');
  const liquidPreparationListOptions = ref([]);

  // 获取配液单列表
  const getLiquidOutputPlanList = async () => {
    const { productPlanId } = urlQueryRef.value;
    const res = await getLiquidOutputPlanListApi({ productPlanId });
    liquidPreparationListOptions.value = res.data;
  };

  // 选择配液单确定
  const handleLiquidPreparationListConfirm = async (data) => {
    planVO.value = data;
    const res = await getLiquidOutputMaterialApi({
      preparationPlanId: liquidPreparationListValue.value,
    });
    formRef.value.setFieldsValue(res.data);
    batchVO.value = res.data;
  };
  // 取消选择配液单
  const handleLiquidPreparationListCancel = () => {
    if (instance.value.progressId || planVO.value?.id) {
      showSwitchingLiquidPreparationList.value = false;
    }
    else {
      handleCancel();
    }
  };

  // 获取配液进度
  const getLiquidOutputProgress = async () => {
    const { version } = getCurrentCopyRecordItem();
    const { productPlanId } = urlQueryRef.value;
    const { procedureStepModelId } = pageBasicDataRef.value;
    const res = await getLiquidOutputProgressApi({
      componentId: props.componentId,
      copyVersion: version,
      procedureStepModelId,
      productPlanId,
      reuse: pageBasicDataRef.value.reusable,
    });
    if (res.data) {
      instance.value = res.data;
      formRef.value.setFieldsValue({
        ...res.data.batchVO,
        expireDate: new Date(res.data.batchVO.expireDate).getTime(),
      });
      liquidPreparationListValue.value = res.data.planVO.id;
      const materialBatchRes = await queryLiquidOutputMaterialBatchApi({
        materialBatchNo: res.data.batchVO?.materialBatchNo,
        formulaMaterialId: res.data.batchVO?.formulaMaterialId,
      });
      const { materialBatchNo } = materialBatchRes.data;
      formRef.value.updateSchema({
        field: 'expireDate',
        componentProps: {
          disabled: !!materialBatchNo,
        },
      });
    }
    else {
      showSwitchingLiquidPreparationList.value = true;
    }
    getLiquidOutputPlanList();
  };

  // 获取配液产出复核人
  const getLiquidOutputCheckUserList = async () => {
    const params = {
      permissionCode: '121010001002021',
      componentId: props.componentId,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      productPlanId: urlQueryRef.value.productPlanId,
    };

    const res = await postSingerListWithPermissionCodeAndComponentApi(params);
    labelList.value[1].options = res.data;
  };

  const leftClick = () => {
    uni.navigateBack();
  };
  const toResult = () => {
    if (instance.value.progressId) {
      uni.navigateTo({
        url: `/pages/businessComponents/liquidOutput/result/index?progressId=${
          instance.value.progressId
        }&componentId=${props.componentId}`,
      });
      return;
    }
    showNotify({
      message: t('请先确认配液单'),
      type: 'danger',
    });
  };

  const values = ref({});
  const signConfirmParams = computed(() => {
    return {
      recordItemId: pageBasicDataRef.value?.recordItemId,
      recordVersionId: pageBasicDataRef.value?.recordVersionId,
      preparationPlanId: liquidPreparationListValue.value,
      materialBatchNo: values.value.materialBatchNo,
      formulaMaterialId:
      instance.value.batchVO.formulaMaterialId
      || batchVO.value?.formulaMaterialId,
      materialMergeCode: values.value.materialMergeCode,
      expireDate: values.value.expireDate && format(values.value.expireDate, 'yyyy-MM-dd'),
      confirmUserId: instance.value.producerId || signValue.value.userId1,
      reCheckUserId: instance.value.reCheckerId || signValue.value.userId2,
      productPlanId: urlQueryRef.value?.productPlanId,
      procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
      componentId: props.componentId,
      copyVersion: getCurrentCopyRecordItem()?.version,
      remark: signValue.value.remark,
      reuse: pageBasicDataRef.value.reusable,
    };
  });

  const signConfirm = async () => {
    try {
      const { data } = await confirmLiquidOutputApi(signConfirmParams.value);
      getLiquidOutputProgress();
      showSign.value = false;
      uni.navigateTo({
        url: `/pages/businessComponents/liquidOutput/modeDevice/index?progressId=${
          data
        }&componentId=${props.componentId}`,
      });
    }
    catch (error) {
      showNotify({
        message: error.message,
        type: 'danger',
      });
    }
  };

  const handleNextStep = async () => {
    await formRef.value.validate();
    values.value = formRef.value.getFormValues();
    if (instance.value.progressId) {
      signConfirm();
    }
    else {
      showSign.value = true;
    }
  };

  // 取消
  const handleCancel = () => {
    uni.navigateBack();
  };

  onMounted(() => {
    getLiquidOutputProgress();
    getLiquidOutputCheckUserList();
  });

  return {
    instance,
    planVO,
    infoItems,
    formRef,
    formProps,
    showSign,
    signValue,
    labelList,
    showSwitchingLiquidPreparationList,
    liquidPreparationListValue,
    liquidPreparationListOptions,
    signConfirmParams,
    disabledConfirm,
    leftClick,
    toResult,
    signConfirm,
    handleNextStep,
    handleCancel,
    handleLiquidPreparationListConfirm,
    handleLiquidPreparationListCancel,
  };
};
