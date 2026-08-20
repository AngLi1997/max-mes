import { getSortingMaintainPlasmaBox, getSortingMaintainSampleBox } from '@/services';
import { FormProps, ModalFormInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const getApiMap = {
    1: getSortingMaintainPlasmaBox,
    2: getSortingMaintainSampleBox,
  };

  const formProps = reactive<FormProps>({
    initialValues: {
      num: 0,
      weight: 0,
    },
    labelWidth: 100,
    schemas: [
      {
        label: t('箱号'),
        field: 'boxNo',
        required: true,
        component: 'InputSearch',
        colProps: {
          span: 24,
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: ({ formModel }: any) => ({
          enterButton: t('查询'),
          onSearch: async (val: any) => {
            if (!formModel.itemType || !val) {
              return;
            }
            try {
              const { data } =
                formModel.itemType === 1
                  ? await getSortingMaintainPlasmaBox(val)
                  : await getSortingMaintainSampleBox(val);
              setFormModels(data);
              message.success(t('查询成功'));
            } catch (error: any) {
              error.message && message.error(error.message);
            }
          },
        }),
      },
      {
        label: t('份数'),
        field: 'num',
        component: 'Span',
      },
      {
        label: t('重量'),
        field: 'weight',
        vIf: true,
        component: 'Span',
      },
    ],
  });

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
