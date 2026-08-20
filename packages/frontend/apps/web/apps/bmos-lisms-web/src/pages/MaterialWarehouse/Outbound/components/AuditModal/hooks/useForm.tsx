import { reqPlatformUserListByState } from '@/services';
import { EnableStatusMap } from '@/types/enum';
import { FormProps, ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('接收人'),
        field: 'receiver',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: {
          request: async () => {
            const { data } = await reqPlatformUserListByState(EnableStatusMap.ON);
            return data.map((item: any) => ({
              label: `${item.userName}(${item.loginName})`,
              value: item.userId,
            }));
          },
        },
      },
      {
        label: t('备注'),
        field: 'outRemark',
        component: 'InputTextArea',
        colProps: {
          span: 24,
        },
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
