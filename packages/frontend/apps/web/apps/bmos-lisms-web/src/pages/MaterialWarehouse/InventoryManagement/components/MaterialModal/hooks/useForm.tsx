import { getMaterialOutSpotCheckHistory } from '@/services';
import { useDict } from '@/stores/dictStore';
import { MaterialModelTypeEnum } from '@/types';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { FormProps, ModalFormInstance } from '@bmos/components';
import { Button, Select, message } from 'ant-design-vue';

const { getDict } = useDict();

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();
  const { yesOrNoDict } = getDicts();
  // 是否查询抽检历史
  const isQueryHistory = ref(false);

  const loading = ref(false);

  const queryHistory = async (formModel: any) => {
    try {
      if (!formModel.specificationId || !formModel.batchNo) {
        message.error(t('请输入物料批号、物料规格'));
        return;
      }
      loading.value = true;
      const { data } = await getMaterialOutSpotCheckHistory({
        materialIdentify: formModel.materialIdentify,
        specificationId: formModel.specificationId,
        batchNo: formModel.batchNo,
      });
      isQueryHistory.value = true;
      setFormModels({ history: data });
    } catch (error: any) {
      console.error(error);
      error.message && message.error(error.message);
    } finally {
      loading.value = false;
    }
  };

  // 物料领用
  const receiveSchemas = [
    {
      label: t('物料编号'),
      field: 'materialNo',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('物料名称'),
      field: 'materialName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('供应商'),
      field: 'supplierName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('物料批号'),
      field: 'batchNo',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('物料规格'),
      field: 'specificationName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('有效日期'),
      field: 'expireDate',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('可用库存量'),
      field: 'availableStock',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('领用数量'),
      field: 'useCount',
      component: 'InputNumber',
      required: true,
      componentProps: {
        precision: 0,
        min: 1,
        style: {
          width: '100%',
        },
      },
    },
    {
      label: t('领用原因'),
      field: 'reasonId',
      component: 'Select',
      required: true,
      componentProps: {
        request: async () => {
          return await getDict('领用原因');
        },
      },
    },
    {
      label: t('领用库'),
      field: 'targetWarehouseId',
      component: 'Select',
      required: true,
      componentProps: {
        request: async () => {
          return await getDict('领用库');
        },
      },
    },
    {
      label: t('备注'),
      field: 'remark',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 100,
        showCount: true,
      },
    },
  ];

  // 物料报废
  const scrapSchemas = [
    {
      label: t('物料编号'),
      field: 'materialNo',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('物料名称'),
      field: 'materialName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('入库单号'),
      field: 'inWarehouseNo',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('供应商'),
      field: 'supplierName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('物料规格'),
      field: 'specificationName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('可用库存量'),
      field: 'availableStock',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('报废数量'),
      field: 'useCount',
      component: 'InputNumber',
      required: true,
      componentProps: {
        precision: 0,
        min: 1,
        style: {
          width: '100%',
        },
      },
    },
    {
      label: t('报废原因'),
      field: 'reasonId',
      component: 'Select',
      required: true,
      componentProps: {
        request: async () => {
          return await getDict('报废原因');
        },
      },
    },
    {
      label: t('备注'),
      field: 'remark',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 100,
        showCount: true,
      },
    },
  ];

  // 物料抽检
  const spotCheckSchemas = [
    {
      label: t('物料编号'),
      field: 'materialNo',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('物料名称'),
      field: 'materialName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('供应商'),
      field: 'supplierName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('物料规格'),
      field: 'specificationName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('入库单号'),
      field: 'inWarehouseNo',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('可用库存量'),
      field: 'availableStock',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('是否抽检'),
      field: 'needSpotCheck',
      component: ({ formModel }: any) => {
        return (
          <>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', width: '100%' }}>
              <Select v-model:value={formModel.needSpotCheck} options={yesOrNoDict} disabled></Select>
              <Button loading={loading.value} onClick={() => queryHistory(formModel)} type='primary'>
                {t('抽检历史')}
              </Button>
            </div>
            {isQueryHistory.value ? (
              <div
                class='info'
                style={{ display: 'flex', alignItems: 'center', gap: '4px', width: '100%', color: 'red' }}>
                <ExclamationCircleOutlined />
                <span>{`${t('该批次最近抽检放行时间')}:${formModel.history ?? t('无')}`}</span>
              </div>
            ) : (
              <></>
            )}
          </>
        );
      },
    },
  ];

  // 物料退货
  const returnSchemas = [
    {
      label: t('物料编号'),
      field: 'materialNo',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('物料名称'),
      field: 'materialName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('供应商'),
      field: 'supplierName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('物料规格'),
      field: 'specificationName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('入库单号'),
      field: 'inWarehouseNo',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('可用库存量'),
      field: 'availableStock',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('退货数量'),
      field: 'useCount',
      component: 'InputNumber',
      required: true,
      componentProps: {
        precision: 0,
        min: 1,
        style: {
          width: '100%',
        },
      },
    },
    {
      label: t('退货原因'),
      field: 'reasonId',
      component: 'Select',
      required: true,
      componentProps: {
        request: async () => {
          return await getDict('退货原因');
        },
      },
    },
    {
      label: t('备注'),
      field: 'remark',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 100,
        showCount: true,
      },
    },
  ];

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 120,
    schemas: receiveSchemas,
  });

  const modalType = ref<MaterialModelTypeEnum>(MaterialModelTypeEnum.RECEIVE);

  watch(
    () => modalType.value,
    () => {
      if (modalType.value === MaterialModelTypeEnum.RECEIVE) {
        formProps.schemas = receiveSchemas;
      } else if (modalType.value === MaterialModelTypeEnum.SCRAP) {
        formProps.schemas = scrapSchemas;
      } else if (modalType.value === MaterialModelTypeEnum.SPOT_CHECK) {
        formProps.schemas = spotCheckSchemas;
      } else if (modalType.value === MaterialModelTypeEnum.RETURN) {
        formProps.schemas = returnSchemas;
      }
    },
  );

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  const updateSchema = (obj: any) => {
    modalFormRef.value?.formRef?.updateSchema(obj);
  };

  return {
    modalFormRef,
    formProps,
    receiveSchemas,
    scrapSchemas,
    returnSchemas,
    setFormModels,
    updateSchema,
    modalType,
    isQueryHistory,
  };
};
