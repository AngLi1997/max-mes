import {
  getInspectConfigApi,
  getMesUnitListDownExtendBound,
  getProcessBindMaterialListApi,
  reqProductMaterialDetail,
} from '@/api';
import {
  BMInfoTable,
} from '@/BMComponents';
import { isNullOrUnDef } from '@/utils/is.js';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { isEmpty } from 'lodash-es';
import { reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useForm = (details) => {
  const infoFormRef = ref();

  const { showNotify } = useNotify();

  const setFormModel = (formModel) => {
    infoFormRef.value?.setFormModels(formModel);
  };

  const unitList = ref([]);

  const setUnitOptions = async (materialId) => {
    try {
      if (isEmpty(materialId)) {
        unitList.value = [];
        return;
      }
      const { data } = await getMesUnitListDownExtendBound({
        materialId,
      });
      const { data: material } = await reqProductMaterialDetail(materialId);
      unitList.value = data.map(item => ({
        label: item.extendUnitName,
        id: item.id,
        expression: item.expression,
      }));

      unitList.value.unshift({
        label: material.unitName,
        id: material.unitId,
        isUnit: true,
        expression: t('标准单位'),
      });
    }
    catch (error) {
      console.log(error);
      unitList.value = [];
    }
  };

  const confirmMaterialTreeModal = async (obj) => {
    if (isEmpty(obj)) {
      await setUnitOptions();
      return;
    }
    await setUnitOptions(obj.materialId);
    setFormModel({ sampleQuantityUnit: obj.unitId });
  };

  const materialDetail = ref([]);

  const getMaterialTreeModalData = async (type) => {
    try {
      if (isNullOrUnDef(type)) {
        materialDetail.value = [];
        infoFormRef.value?.updateSchema({
          field: 'formulaMaterialId',
          componentProps: {
            // 'tree-data': [],
            options: [],
          },
        });
        return;
      }
      console.log('console', type);
      const { data } = await getProcessBindMaterialListApi({
        categoryType: type,
        procedureModelId: infoFormRef.value?.formModel.procedureModelId,
      });
      materialDetail.value = data.map((item) => {
        return {
          ...item,
          label: `${item.materialMergeCode}-${item.materialName}`,
        };
      });
      infoFormRef.value?.updateSchema({
        field: 'formulaMaterialId',
        componentProps: {
          // 'tree-data': data,
          options: materialDetail.value,
        },
      });
    }
    catch (error) {
      console.log(error);
      infoFormRef.value?.updateSchema({
        field: 'formulaMaterialId',
        componentProps: {
          // 'tree-data': [],
          options: [],
        },
      });
    }
  };

  const disableDefaultMap = {
    instructionNo: 'planNo',
    productBatchNo: 'batchNo',
    productLineName: 'productionLineName',
    productLineCode: 'productionLineCode',
    productBatchQuantity: 'batchQuantity',
    productBatchQuantityUnit: 'unitName',
  };

  const pleasVerifyInfoSchema = ref([]);

  const getPleaseVerifyInfoSchema = async (id) => {
    try {
      const { data } = await getInspectConfigApi(id);
      setFormModel({ inspectConfigId: data.id });
      data.dataList.sort((a, b) => a.sort - b.sort);
      const tempSchema = [];
      const obj = materialDetail.value.find(material => material.id === id) || {};
      for (let i = 0; i < data.dataList.length; i++) {
        const item = data.dataList[i];
        let component = 'Input';
        let componentProps = {
          disabled: ['pleaseCheckNo', 'inspector'].includes(item.code),
        };
        if (item.code === 'sampleQuantityUnit') {
          component = 'BMFormSelect';
          await confirmMaterialTreeModal(obj);
          componentProps = {
            disabled: false,
            fieldNames: {
              label: 'label',
              value: 'id',
            },
            options: unitList.value,
            subLabel: 'expression',
            title: t('单位选择'),
          };
        }
        else if (item.code === 'materialName') {
          setFormModel({ materialName: obj.materialName });
          componentProps.disabled = true;
        }
        else if (item.code === 'materialCode') {
          setFormModel({ materialCode: obj.materialMergeCode });
          componentProps.disabled = true;
        }
        else if (disableDefaultMap[item.code]) {
          componentProps.disabled = true;
        }
        tempSchema.push({
          label: item.showName,
          field: item.code,
          component,
          required: item.required,
          dataName: item.dataName,
          inspectConfigDataId: item.id,
          sort: item.sort,
          valueDefault: item.defaultValue,
          componentProps,
        });
      }
      pleasVerifyInfoSchema.value = [{
        label: t('请验单信息'),
        field: 'pleaseVerifyInfo',
        component: 'FormTitle',
        colProps: {
          span: 24,
        },
      }, ...tempSchema];
    }
    catch (error) {
      pleasVerifyInfoSchema.value = [];
      console.log(error);
      error.message && showNotify({
        type: 'warning',
        message: error.message,
      });
    }
  };

  const infoFormProps = reactive({
    schemas: [
      {
        field: 'productionInfoTitle',
        component: 'FormTitle',
        label: t('生产信息'),
        colProps: {
          span: 24,
        },
      },
      {
        field: 'productionInfo',
        component: ({ formModel }) => {
          return (
            <BMInfoTable details={details} data={formModel} />);
        },
        colProps: {
          span: 24,
        },
      },
      {
        field: 'materialInfoTitle',
        component: 'FormTitle',
        label: t('物料信息'),
        colProps: {
          span: 24,
        },
      },
      {
        label: t('物料类型'),
        field: 'materialType',
        required: true,
        component: 'BMFormSelect',
        defaultValue: '1',
        componentProps: () => {
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
              {
                label: t('成品'),
                value: '2',
              },
            ],
            title: t('物料类型'),
            onChange: (val) => {
              getMaterialTreeModalData(val);
              infoFormRef.value?.removeSchemaByFiled(pleasVerifyInfoSchema.value.map(item => item.field));
              unitList.value = [];
            },
          };
        },
      },
      {
        label: t('物料信息'),
        field: 'formulaMaterialId',
        required: true,
        component: 'BMFormSelect',
        componentProps: ({ formModel }) => {
          return {
            title: t('物料信息'),
            // type: 'tree',
            fieldNames: {
              label: 'label',
              value: 'id',
            },
            options: [],
            // treeData: [],
            onChange: async (val) => {
              infoFormRef.value?.removeSchemaByFiled(pleasVerifyInfoSchema.value.map(item => item.field));
              await confirmMaterialTreeModal();
              if (val) {
                await getPleaseVerifyInfoSchema(val);
                infoFormRef.value?.appendSchemasByField(pleasVerifyInfoSchema.value, 'materialInfo');
                for (let i = 1; i < pleasVerifyInfoSchema.value.length; i++) {
                  const tem = pleasVerifyInfoSchema.value[i];
                  if (tem.field === 'inspector') { // 请验人
                    const currentUser = getStorageSync(USER_INFO) || {};
                    const { loginName, userName } = currentUser;
                    formModel.inspector = `${userName}-${loginName}`;
                  }
                  else if (tem.field === 'materialBatchNo') { // 物料批号
                    formModel.materialBatchNo = formModel.batchNo;
                  }
                  else if (disableDefaultMap[tem.field]) {
                    formModel[tem.field] = formModel[disableDefaultMap[tem.field]];
                  }
                  else if (!(tem.field === 'sampleQuantityUnit' || ['materialName', 'materialCode'].includes(tem.field))) {
                    formModel[tem.field] = tem.valueDefault;
                  }
                }
              }
            },
          };
        },
      },
    ],
  });

  return {
    infoFormRef,
    infoFormProps,
    pleasVerifyInfoSchema,
    getMaterialTreeModalData,
    getPleaseVerifyInfoSchema,
    setFormModel,
  };
};
