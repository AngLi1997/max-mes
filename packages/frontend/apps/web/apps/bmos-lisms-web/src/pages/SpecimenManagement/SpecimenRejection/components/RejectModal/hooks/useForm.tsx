import { useDict } from '@/stores/dictStore';
import { FormProps, ModalFormInstance } from '@bmos/components';

const { getDict } = useDict();

export const useForm = () => {
  const { yesOrNoDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('拒收原因'),
        field: 'refuseReason',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
        },
        componentProps: {
          request: async () => {
            return await getDict('拒收原因');
          },
        },
      },
      {
        label: t('是否补样'),
        field: 'needSupplement',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
        },
        componentProps: {
          options: yesOrNoDict,
        },
      },
      {
        label: t('备注'),
        field: 'applyRemark',
        component: 'InputTextArea',
        componentProps: {
          maxlength: 200,
          showCount: true,
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
  };
};
