import { reqPlatformUserListByMenuId } from '@/services';
import { FormProps, ModalFormInstance } from '@bmos/components';
import { sso } from '@bmos/messager';

export const useForm = () => {
  const { auditResultDict } = getDicts();
  const { getUserInfo } = sso;
  const userInfo = getUserInfo();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {
      reviewerList: [userInfo?.userId],
    },
    schemas: [
      {
        label: t('审核人'),
        field: 'reviewerList',
        required: true,
        component: 'Select',
        colProps: {
          span: 20,
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: ({ formModel }: any) => {
          return {
            mode: 'multiple',
            onSelect: () => {
              if (formModel.reviewerList.length > 5) {
                formModel.reviewerList = formModel.reviewerList.slice(0, 5);
              }
            },
            request: async () => {
              const { data } = await reqPlatformUserListByMenuId('210060010');
              return data.map((userItem: any) => {
                return {
                  label: userItem.userName + '-' + userItem.loginName,
                  value: userItem.userId,
                };
              });
            },
          };
        },
      },
      {
        label: t('审核结果'),
        field: 'auditResult',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: {
          options: auditResultDict,
        },
      },
      {
        label: t('备注'),
        field: 'reviewRemark',
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
