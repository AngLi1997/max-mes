import {
  getProductTreeApi,
  queryBatchInfoOutputApi,
  reqProductMaterialDetail,
  reqUnitGetUnitById,
} from '@/api';
import { useMaterialWeighingStore } from '@/stores/workbench/materialWeighing/index.js';
import { buildUrlQuery } from '@/utils/url';
import { t } from '@/utils/useBmosI18n.js';
import { format } from 'date-fns';
import { isEmpty } from 'lodash-es';
import { storeToRefs } from 'pinia';
import { computed, nextTick, onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const usePageData = () => {
  const { showNotify } = useNotify();
  const materialWeighingStore = useMaterialWeighingStore();
  const {
    initMaterialWeighingStore,
    setDetailData,
    setStoreSignValue,
  } = materialWeighingStore;
  const { detailData, storeSignValue } = storeToRefs(materialWeighingStore);
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
  const haveExpiredDate = ref(false);

  const signatureData = computed(() => {
    return {
      reCheckerId: signValue.value?.userId2,
      remark: signValue.value?.remark,
      weigherId: signValue.value?.userId1,
    };
  });

  const labelList = computed(() => {
    return [
      {
        label: t('称量人'),
        signatureAction: 130,
        disabled: true,
        currentUser: true,
      },
      {
        label: t('复核人'),
        signatureAction: 132,
        menuId: '121020005000003',
      },
    ];
  });

  const formsRef = ref(null);
  const getMaterialTreeModalData = async (type) => {
    try {
      if (isEmpty(type)) {
        formsRef.value?.updateSchema({
          field: 'materialId',
          componentProps: {
            'tree-data': [],
          },
        });
        return;
      }
      const { data } = await getProductTreeApi({
        categoryType: type,
      });
      formsRef.value?.updateSchema({
        field: 'materialId',
        componentProps: {
          'tree-data': data,
        },
      });
    }
    catch (_error) {
      formsRef.value?.updateSchema({
        field: 'materialId',
        componentProps: {
          'tree-data': [],
        },
      });
    }
  };
  // 产出批次表单配置
  const formProps = reactive({
    schemas: [
      {
        field: 'formTitle1',
        component: 'FormTitle',
        label: t('物料批次'),
        colProps: {
          span: 24,
        },
        componentProps: {
          color: '#fff',
        },
      },
      {
        field: 'type',
        component: 'BMFormSelect',
        label: t('物料类型'),
        required: true,
        defaultValue: '0',
        componentProps: ({ formModel }) => {
          return {
            fieldNames: {
              label: 'label',
              value: 'value',
            },
            options: [
              {
                label: t('原辅包'),
                value: '0',
              },
              {
                label: t('中间品'),
                value: '1',
              },
            ],
            title: t('选择物料类型'),
            onChange: (val) => {
              getMaterialTreeModalData(val);
              formModel.materialId = '';
              formModel.specification = '';
              formModel.materialName = '';
              formModel.materialMergeCode = '';
              formModel.expiredDate = '';
              formModel.batchNo = '';
              haveExpiredDate.value = false;
            },
          };
        },
      },
      {
        field: 'materialId',
        component: 'BMFormSelect',
        label: t('物料信息'),
        required: true,
        componentProps: ({ formModel }) => {
          return {
            title: t('选择物料信息'),
            type: 'tree',
            fieldNames: {
              name: 'showName',
              key: 'id',
              checkKey: 'categoryFlag',
              checkKeyValue: false,
              parentId: 'parentId',
              children: 'children',
            },
            treeData: [],
            onConfirm: (data) => {
              formModel.specification = data.specification;
              formModel.materialName = data.name;
              formModel.materialMergeCode = data.mergeCode;
              formModel.expiredDate = '';
              formModel.batchNo = '';
              haveExpiredDate.value = false;
            },
            onClear: () => {
              formModel.materialId = '';
              formModel.specification = '';
              formModel.materialName = '';
              formModel.materialMergeCode = '';
              formModel.expiredDate = '';
              formModel.batchNo = '';
              haveExpiredDate.value = false;
            },
          };
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
        label: t('生产批次'),
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
        label: t('产品信息'),
        componentProps: () => {
          return {
            title: t('选择产品信息'),
            type: 'tree',
            fieldNames: {
              name: 'showName',
              key: 'id',
              checkKey: 'categoryFlag',
              checkKeyValue: false,
              parentId: 'parentId',
              children: 'children',
            },
            request: async () => {
              try {
                const { data } = await getProductTreeApi({
                  categoryType: 2,
                });
                return data;
              }
              catch (_error) {

              }
            },
          };
        },
      },
      {
        field: 'productionBatchNo',
        component: 'Input',
        label: t('生产批号'),
      },
    ],
  });

  const signConfirm = async () => {
    try {
      signOpen.value = false;
      setStoreSignValue(signValue.value);
    }
    catch (error) {
      error.message && showNotify({
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
        error.message && showNotify({
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

  const submit = async () => {
    const formValues = await formsRef.value.validate();
    const relatedFormValues = await relatedFormsRef.value.getFormValues();
    try {
      const params = {
        expiredDate: format(formValues.expiredDate, 'yyyy-MM-dd'),
        materialId: formValues.materialId,
        storageMaterialBatchNo: formValues.batchNo,
        relevanceMaterialId: relatedFormValues.materialId || undefined,
        relevanceMaterialBatchNo: relatedFormValues.productionBatchNo || undefined,
        specification: formValues.specification,
      };
      const { data: materialDetail } = await reqProductMaterialDetail(formValues.materialId);
      const { data: unitDetail } = await reqUnitGetUnitById(materialDetail.unitId);
      setDetailData({
        ...params,
        materialName: formValues.materialName,
        materialMergeCode: formValues.materialMergeCode,
        unitId: materialDetail.unitId,
        unitExtendId: materialDetail.unitExtendId,
        precision: unitDetail.precision,
        categoryType: formValues.type,
      });
      const query = {
        materialId: formValues.materialId,
      };
      uni.navigateTo({
        url: `/pages/materialWeighing/modeDevice/index?${buildUrlQuery(
          query,
        )}`,
      });
    }
    catch (error) {
      error.message && showNotify({
        type: 'warning',
        message: error.message,
      });
    }
  };

  // 返回
  const toBack = () => {
    uni.navigateTo({
      url: `/pages/home/index`,
    });
  };

  onMounted(async () => {
    if (!query.value.continue) {
      signOpen.value = true;
      initMaterialWeighingStore();
      getMaterialTreeModalData('0');
    }
    else {
      await nextTick();
      formsRef.value.setFormModels({
        type: detailData.value?.categoryType,
        materialId: detailData.value?.materialId,
        batchNo: detailData.value?.storageMaterialBatchNo,
        expiredDate: detailData.value?.expiredDate,
        specification: detailData.value?.specification,
        materialName: detailData.value?.materialName,
        materialMergeCode: detailData.value?.materialMergeCode,
      });
      getMaterialTreeModalData(detailData.value?.categoryType);
      relatedFormsRef.value?.setFormModels({
        materialId: detailData.value?.relevanceMaterialId,
        productionBatchNo: detailData.value?.relevanceMaterialBatchNo,
      });
      signValue.value = storeSignValue.value;
    }
  });
  return {
    signValue,
    signOpen,
    signatureData,
    labelList,
    formProps,
    formsRef,
    relatedFormsRef,
    relatedFormProps,
    query,
    signConfirm,
    submit,
    toBack,
  };
};
