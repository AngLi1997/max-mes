import { FormProps, ModalFormInstance } from '@bmos/components';
import { getStorageSelectList, getUserList } from '@/services';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('试剂'),
        field: 'reagentBatchNo',
        required: true,
        colProps: {
          span: 8,
        },
        component: 'Select',
        componentProps: {

          fieldNames: {
            label: 'name',
            value: 'batchNo',
          },
          request: async () => {
            const { data } = await getStorageSelectList({ types: [3] });
            return data?.map((item: any) => ({
              ...item,
              name: `${item.batchNo}-${item.materialName} ${item.specification}`,
            }));
          }
        },
      },
      {
        label: t('质控品'),
        field: 'qualityControllerBatchNo',
        required: true,
        colProps: {
          span: 8,
        },
        component: 'Select',
        componentProps: {
          fieldNames: {
            label: 'name',
            value: 'batchNo',
          },
          request: async () => {
            const { data } = await getStorageSelectList({ types: [4] });
            return data?.map((item: any) => ({
              ...item,
              name: `${item.batchNo}-${item.materialName} ${item.specification}`,
            }));
          }
        },
      },
      {
        label: t('检验人'),
        field: 'checkBy',
        colProps: {
          span: 8,
        },
        component: 'Select',
        componentProps: ({ formInstance }: any) => {
          return {
            fieldNames: {
              label: 'userName',
              value: 'userId',
            },
            showSearch: true,
            filterOption: false,
            request: async () => {
              const { data } = await getUserList();
              return data;
            },
            onSearch: async (input: string, option: any) => {
              const { data } = await getUserList({ userName: input });
              formInstance.updateSchema({
                field: 'checkBy',
                componentProps: {
                  options: data || [],
                },
              });
            }
          }

        },
      }
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
