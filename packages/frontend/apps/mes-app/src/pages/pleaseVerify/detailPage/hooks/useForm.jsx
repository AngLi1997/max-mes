import { getInspectConfigApi, getMesUnitListDownExtendBound, getProcessBindMaterialListApi, reqProductMaterialDetail } from '@/api';
import {
  BMInfoTable,
} from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';

export const useForm = () => {
  const infoFormRef = ref();

  const setFormModel = (formModel) => {
    infoFormRef.value?.setFormModels(formModel);
  };

  // 生产信息
  const productDetails = reactive([
    {
      title: t('产品名称'),
      dataIndex: 'productName',
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
    },
    {
      title: t('指令单编号'),
      dataIndex: 'planNo',
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
    },
    {
      title: t('批量'),
      dataIndex: 'batchQuantity',
    },
    {
      title: t('单位'),
      dataIndex: 'unitName',
    },
  ]);

  // 物料信息
  const materialDetails = reactive([
    {
      title: t('物料类型'),
      dataIndex: 'materialType',
    },
    {
      title: t('物料信息'),
      dataIndex: 'materialName',
    },
  ]);

  // 请验单信息
  const pleaseVerifyDetails = ref([
  ]);

  const getPleaseVerifyInfoSchema = async (dataList) => {
    try {
      const tempSchema = [];
      for (let i = 0; i < dataList.length; i++) {
        const item = dataList[i];
        if (item.code === 'sampleQuantityUnit') {
          const materialId = infoFormRef.value?.formModel.materialId;
          // await confirmMaterialTreeModal(obj);
          const { data } = await getMesUnitListDownExtendBound({
            materialId,
          });
          const { data: material } = await reqProductMaterialDetail(materialId);
          const unitList = data.map(util => ({
            label: util.extendUnitName,
            id: util.id,
            expression: util.expression,
          }));

          unitList.unshift({
            label: material.unitName,
            id: material.unitId,
            isUnit: true,
            expression: t('标准单位'),
          });
          const sampleQuantityUnit = unitList.find(unit => unit.id === item.value)?.label;
          setFormModel({ sampleQuantityUnit });
        }
        tempSchema.push({
          title: item.showName,
          dataIndex: item.code,
        });
      }
      pleaseVerifyDetails.value = tempSchema;
    }
    catch (error) {
      console.log(error);
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
            <BMInfoTable
              details={productDetails}
              data={formModel}
            />
          );
        },
        colProps: {
          span: 24,
        },
      },
      {
        label: t('物料信息'),
        field: 'materialInfoTitle',
        component: 'FormTitle',
        colProps: {
          span: 24,
        },
      },
      {
        field: 'materialInfo',
        component: ({ formModel }) => {
          return (
            <BMInfoTable
              details={materialDetails}
              data={formModel}
            />
          );
        },
        colProps: {
          span: 24,
        },
      },
      {
        label: t('请验单信息'),
        field: 'pleaseVerifyInfoTitle',
        component: 'FormTitle',
        colProps: {
          span: 24,
        },
      },
      {
        field: 'pleaseVerifyInfo',
        component: ({ formModel }) => {
          return (
            <BMInfoTable
              details={pleaseVerifyDetails.value}
              data={formModel}
            />
          );
        },
        colProps: {
          span: 24,
        },
      },
    ],
  });

  return {
    infoFormRef,
    infoFormProps,
    getPleaseVerifyInfoSchema,
    setFormModel,
  };
};
