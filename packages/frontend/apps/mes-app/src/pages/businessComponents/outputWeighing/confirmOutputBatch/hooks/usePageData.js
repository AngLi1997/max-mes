import {
  getMiddleMaterialListOutputApi,
  getOutputWeighProcessApi,
  getUnionOriginMaterialListOutputApi,
  makeSureBatchOutputApi,
  makeSureWeigherOutputApi,
  postSingerListWithPermissionCodeAndComponentApi,
  queryBatchInfoOutputApi,
} from '@/api';
import {
  getCurrentCopyRecordItem,
  goBackToTargetPath,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { useOutputWeighingStore } from '@/stores/businessComponents/outputWeighing/index.js';
import { buildUrlQuery } from '@/utils/url';
import { t } from '@/utils/useBmosI18n.js';
import { format } from 'date-fns';

import { storeToRefs } from 'pinia';
import { computed, onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const usePageData = () => {
  const { showNotify } = useNotify();
  const outputWeighingStore = useOutputWeighingStore();
  const { detailData, reCheckerList } = storeToRefs(outputWeighingStore);
  const {
    setDetailData,
    setReCheckerList,
    setWeighingPersonList,
    initOutputWeighingStore,
  } = outputWeighingStore;

  const query = ref({});
  const signOpen = ref(false);
  // 签名数据
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
  const materialList = ref([]);
  const relatedMaterialList = ref([]);
  const haveExpiredDate = ref(false);

  // 参数
  const params = computed(() => {
    return {
      componentId: query.value.componentId,
      copyVersion: getCurrentCopyRecordItem().version,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      productPlanId: urlQueryRef.value.productPlanId,
    };
  });

  const signatureData = computed(() => {
    return {
      componentId: query.value.componentId,
      copyVersion: getCurrentCopyRecordItem().version,
      procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
      productPlanId: urlQueryRef.value.productPlanId,
      reCheckerId: signValue.value.userId2,
      remark: signValue.value.remark,
      weigherId: signValue.value.userId1,
    };
  });

  const labelList = computed(() => {
    return [
      {
        label: t('产出人'),
        signatureAction: 56,
        disabled: true,
      },
      {
        label: t('复核人'),
        signatureAction: 57,
        options: reCheckerList,
      },
    ];
  });

  const formsRef = ref(null);
  // 产出批次表单配置
  const formProps = reactive({
    schemas: [
      {
        field: 'formTitle1',
        component: 'FormTitle',
        label: t('产出批次'),
        colProps: {
          span: 24,
        },
        componentProps: {
          color: '#fff',
        },
      },
      {
        field: 'materialId',
        component: 'BMFormSelect',
        label: t('物料名称'),
        required: true,
        colProps: {
          span: 12,
        },
        componentProps: {
          clearable: false,
          options: [],
          title: t('选择产出物料'),
          fieldNames: {
            label: 'name',
            value: 'id',
          },
          onConfirm: async (data) => {
            setMaterialInfo(data || {});
          },
        },
      },
      {
        field: 'mergeCode',
        component: 'Input',
        label: t('物料编码'),
        colProps: {
          span: 12,
        },
        componentProps: {
          disabled: true,
          placeholder: t('自动填入'),
        },
      },
      {
        field: 'specification',
        component: 'Input',
        label: t('物料规格'),
        colProps: {
          span: 12,
        },
        componentProps: {
          disabled: true,
          placeholder: t('自动填入'),
        },
      },
      {
        field: 'batchNo',
        component: 'Input',
        label: t('物料批号'),
        required: true,
        colProps: {
          span: 12,
        },
        componentProps: {
          onBlur: () => {
            getBatchInfo();
          },
        },
      },
      {
        field: 'expiredDate',
        component: 'BMFormDatePicker',
        label: t('有效期至'),
        required: true,
        colProps: {
          span: 12,
        },
        componentProps: () => {
          return {
            formatDate: 'yyyy-MM-dd',
            title: t('有效期至'),
            minDate: new Date(),
            disabled: haveExpiredDate.value,
          };
        },
      },
    ],
  });
  const relatedFormsRef = ref(null);
  // 产出批次表单配置
  const relatedFormProps = reactive({
    schemas: [
      {
        field: 'formTitle1',
        component: 'FormTitle',
        label: t('关联批次'),
        colProps: {
          span: 24,
        },
        componentProps: {
          color: '#fff',
        },
      },
      {
        field: 'materialId',
        component: 'BMFormSelect',
        label: t('物料名称'),
        colProps: {
          span: 12,
        },
        componentProps: ({ formModel, formInstance }) => {
          return {
            options: [],
            title: t('选择关联物料'),
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            onConfirm: async (data) => {
              formModel.mergeCode = data.mergeCode;
              formModel.specification = data.specification;
              formModel.batchNo = undefined;
              formInstance.updateSchema({
                field: 'batchNo',
                componentProps: {
                  options: (data.batchNoList || []).map((item) => {
                    return {
                      id: item,
                      name: item,
                    };
                  }),
                },
              });
            },
            onClear: () => {
              formModel.mergeCode = undefined;
              formModel.specification = undefined;
              formModel.batchNo = undefined;
              formInstance.updateSchema({
                field: 'batchNo',
                componentProps: {
                  options: [],
                },
              });
            },
          };
        },
      },
      {
        field: 'mergeCode',
        component: 'Input',
        label: t('物料编码'),
        colProps: {
          span: 12,
        },
        componentProps: {
          disabled: true,
          placeholder: t('自动填入'),
        },
      },
      {
        field: 'specification',
        component: 'Input',
        label: t('物料规格'),
        colProps: {
          span: 12,
        },
        componentProps: {
          disabled: true,
          placeholder: t('自动填入'),
        },
      },
      {
        field: 'batchNo',
        component: 'BMFormSelect',
        label: t('物料批号'),
        colProps: {
          span: 12,
        },
        componentProps: {
          options: [],
          title: t('选择关联批次物料批号'),
          fieldNames: {
            label: 'name',
            value: 'id',
          },
        },
      },
    ],
  });

  const signConfirm = async () => {
    try {
      await makeSureWeigherOutputApi(signatureData.value);
      getOutputWeighProcess();
    }
    catch (error) {
      error.message
      && showNotify({
        type: 'warning',
        message: error.message,
      });
    }
    signOpen.value = false;
  };

  // 获取复核人列表
  const getReCheckerList = async () => {
    try {
      const res = await postSingerListWithPermissionCodeAndComponentApi({
        permissionCode: '121010001002011',
        componentId: query.value.componentId,
        procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
        productPlanId: urlQueryRef.value.productPlanId,
      });
      const list = res.data || [];
      setReCheckerList(list);
    }
    catch (error) {
      error.message
      && showNotify({
        type: 'warning',
        message: error.message,
      });
    }
  };

  // 获取称量人列表
  const getWeighingPersonList = async () => {
    try {
      const res = await postSingerListWithPermissionCodeAndComponentApi({
        componentId: query.value.componentId,
        procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
        productPlanId: urlQueryRef.value.productPlanId,
      });
      const list = res.data || [];
      setWeighingPersonList(list);
    }
    catch (error) {
      error.message
      && showNotify({
        type: 'warning',
        message: error.message,
      });
    }
  };

  // 获取物料批次有效期
  async function getBatchInfo() {
    const values = formsRef.value.getFormValues();
    const { batchNo, materialId } = values;
    if (batchNo && materialId) {
      try {
        const res = await queryBatchInfoOutputApi({
          batchNo,
          materialId,
        });
        formsRef.value.setFormModels({
          expiredDate: res.data.expiredDate,
        });
        haveExpiredDate.value = !!res.data.expiredDate;
      }
      catch (error) {
        haveExpiredDate.value = false;
        error.message
        && showNotify({
          type: 'warning',
          message: error.message,
        });
      }
    }
    else {
      formsRef.value.setFormModels({
        expiredDate: undefined,
      });
      haveExpiredDate.value = false;
    }
  };

  // 设置产出批次表单数据
  function setMaterialInfo(data) {
    formsRef.value.setFormModels({
      materialId: data.id,
      mergeCode: data.mergeCode,
      specification: data.specification,
      expiredDate: undefined,
      batchNo: urlQueryRef.value.batchNo,
    });
    getBatchInfo();
  };

  // 数据回显方法
  const dataEcho = () => {
    // 详情有物料id时，说明已经确认过批次,回显相关信息
    if (detailData.value.materialId) {
      const {
        materialId,
        materialMergeCode,
        materialSpecification,
        expiredDate,
        storageMaterialBatchNo,
      } = detailData.value;
      formsRef.value.setFormModels({
        materialId,
        mergeCode: materialMergeCode,
        specification: materialSpecification,
        expiredDate,
        batchNo: storageMaterialBatchNo,
      });
      haveExpiredDate.value = !!expiredDate;
    }
    else {
      if (materialList.value.length === 1) {
        setMaterialInfo(materialList.value[0]);
      }
      else {
        formsRef.value.setFormModels({
          batchNo: urlQueryRef.value.batchNo,
        });
        getBatchInfo();
      }
    }
  };

  // 获取产出称量信息
  async function getOutputWeighProcess() {
    try {
      const res = await getOutputWeighProcessApi(params.value);
      if (res.data === null) {
        signOpen.value = true;
      }
      else {
        setDetailData(res.data);
        // 获取中间物料列表
        const res1 = await getMiddleMaterialListOutputApi({
          outputWeighProcessId: detailData.value.id,
        });
        materialList.value = res1.data || [];
        formsRef.value.updateSchema({
          field: 'materialId',
          componentProps: {
            options: materialList.value,
          },
        });
        // 获取关联批次中的原辅包物料列表
        const res2 = await getUnionOriginMaterialListOutputApi({
          outputWeighProcessId: detailData.value.id,
        });
        relatedMaterialList.value = res2.data || [];
        relatedFormsRef.value.updateSchema({
          field: 'materialId',
          componentProps: {
            options: relatedMaterialList.value,
          },
        });
        // 数据回显
        dataEcho();
      }
    }
    catch (error) {
      error.message
      && showNotify({
        type: 'warning',
        message: error.message,
      });
    }
  };

  const submit = async () => {
    const formValues = await formsRef.value.validate();
    const relatedFormValues = await relatedFormsRef.value.getFormValues();
    try {
      await makeSureBatchOutputApi({
        expiredDate: format(formValues.expiredDate, 'yyyy-MM-dd'),
        materialId: formValues.materialId,
        outputWeighProcessId: detailData.value.id,
        storageMaterialBatchNo: formValues.batchNo,
        relevanceMaterialId: relatedFormValues.materialId || undefined,
        relevanceMaterialBatchNo: relatedFormValues.batchNo || undefined,
      });
      const res = await getOutputWeighProcessApi(params.value);
      setDetailData(res.data);
      uni.navigateTo({
        url: `/pages/businessComponents/outputWeighing/modeDevice/index?${buildUrlQuery(
          query.value,
        )}`,
      });
    }
    catch (error) {
      error.message
      && showNotify({
        type: 'warning',
        message: error.message,
      });
    }
  };

  // 返回
  const toBack = () => {
    goBackToTargetPath();
  };

  const toResult = () => {
    uni.navigateTo({
      url: `/pages/businessComponents/outputWeighing/result/index?componentId=${query.value.componentId}`,
    });
  };

  onMounted(() => {
    initOutputWeighingStore();
    getReCheckerList();
    getWeighingPersonList();
    getOutputWeighProcess();
  });
  return {
    query,
    signValue,
    signOpen,
    signatureData,
    labelList,
    formProps,
    formsRef,
    relatedFormsRef,
    relatedFormProps,
    signConfirm,
    submit,
    toBack,
    toResult,
  };
};
