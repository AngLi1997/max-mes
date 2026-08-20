import { getMaterialOutSpotCheckHistory } from '@/services';
import { useDict } from '@/stores/dictStore';
import { MaterialReceiveResultEnum, MaterialWarehouseAreaEnum, dictToMap, yesOrNoEnum } from '@/types';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { FormProps, ModalFormInstance } from '@bmos/components';
import { Button, Select, message } from 'ant-design-vue';

export const useForm = () => {
  const { getDict } = useDict();
  const { materialReceiveResultDict, materialTypeDict, materialWarehouseAreaDict, yesOrNoDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();
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

  const formProps = reactive<FormProps>({
    initialValues: { minInventory: 60 },
    labelWidth: 120,
    schemas: [
      {
        label: t('物料编号'),
        field: 'materialNo',
        required: true,
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('物料名称'),
        field: 'materialName',
        required: true,
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('物料类型'),
        field: 'materialType',
        required: true,
        component: 'Select',
        componentProps: {
          options: materialTypeDict,
          disabled: true,
        },
      },
      {
        label: t('仓库地址'),
        field: 'warehouseAddressId',
        required: true,
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('仓库地址');
          },
        },
      },
      {
        label: t('物料规格'),
        field: 'specificationId',
        required: true,
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('物料规格');
          },
        },
      },
      {
        label: t('物料批号'),
        field: 'batchNo',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 15,
          showCount: true,
        },
      },
      {
        label: t('入库数量'),
        field: 'quantity',
        required: true,
        component: 'InputNumber',
        componentProps: {
          min: 1,
          max: 999999,
          precision: 0,
          style: {
            width: '100%',
          },
        },
      },
      {
        label: t('生产日期'),
        field: 'productionDate',
        required: true,
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('有效日期'),
        field: 'expireDate',
        required: true,
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('质控品含量'),
        field: 'qualityControlNumerical',
        vIf: ({ formModel }: any) => formModel.keyMaterialCategory === 'QUALITY_CONTROL',
        required: true,
        component: 'Select',
        componentProps: {
          fieldNames: { label: 'label', value: 'label' },
          request: async () => {
            return await getDict('质控品含量');
          },
        },
      },
      {
        label: t('备注'),
        field: 'remark',
        component: 'InputTextArea',
        componentProps: {
          maxlength: 200,
          showCount: true,
        },
      },
      {
        label: t('接收结果'),
        field: 'receiveResult',
        required: true,
        component: 'Select',
        componentProps: {
          options: materialReceiveResultDict,
        },
      },
      {
        label: t('是否抽检'),
        field: 'needSpotCheck',
        required: true,
        vIf: ({ formModel }: any) => formModel.receiveResult === MaterialReceiveResultEnum.PASS,
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请选择是否抽检'),
            },
          ];
        },
        component: ({ formModel }: any) => {
          return (
            <>
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px', width: '100%' }}>
                <Select v-model:value={formModel.needSpotCheck} options={yesOrNoDict} allowClear></Select>
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
      {
        label: t('仓库区域'),
        field: 'warehouseArea',
        component: ({ formModel }: any) => {
          const warehouseArea: keyof typeof MaterialWarehouseAreaEnum = formModel.warehouseArea;
          return (
            <div
              style={{
                width: '100%',
                height: '34px',
                backgroundColor: '#f5f5f5',
                padding: '6px 12px',
                borderRadius: '3px',
                color: MaterialWarehouseAreaEnum[warehouseArea],
              }}>
              {dictToMap(materialWarehouseAreaDict)?.[warehouseArea]}
            </div>
          );
        },
      },
    ],
  });

  enum warehouseAreaEnum {
    PASS = 'PASS',
    NOPASS = 'NOPASS',
    WAITING = 'WAITING',
  }

  const warehouseArea = computed(() => {
    const formModel = modalFormRef.value?.formRef?.formModel ?? {};
    if (formModel.receiveResult === MaterialReceiveResultEnum.NO_PASS) {
      return warehouseAreaEnum.NOPASS;
    } else {
      switch (formModel.needSpotCheck) {
        case yesOrNoEnum.YES:
          return warehouseAreaEnum.WAITING;
        case yesOrNoEnum.NO:
          return warehouseAreaEnum.PASS;
        default:
          return '';
      }
    }
  });

  watch(
    () => warehouseArea.value,
    () => {
      setFormModels({ warehouseArea: warehouseArea.value });
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
    setFormModels,
    updateSchema,
  };
};
