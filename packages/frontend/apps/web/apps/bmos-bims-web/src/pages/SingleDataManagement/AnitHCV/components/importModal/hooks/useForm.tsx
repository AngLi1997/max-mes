import { getStorageSelectList } from '@/services';
import { FormProps, ModalFormInstance } from '@bmos/components';
import { Button } from 'ant-design-vue';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('试剂'),
        field: 'reagentBatchNo',
        required: true,
        component: 'Select',
        componentProps: {
          fieldNames: {
            label: 'name',
            value: 'batchNo',
          },
          request: async () => {
            const { data } = await getStorageSelectList({ types: [7] });
            return data?.map((item: any) => ({
              ...item,
              name: `${item.batchNo}-${item.materialName} ${item.specification}`,
            }));
          },
        },
      },
      {
        label: t('质控品'),
        field: 'qualityControllerBatchNo',
        required: true,
        component: 'Select',
        componentProps: {
          fieldNames: {
            label: 'name',
            value: 'batchNo',
          },
          request: async () => {
            const { data } = await getStorageSelectList({ types: [8] });
            return data?.map((item: any) => ({
              ...item,
              name: `${item.batchNo}-${item.materialName} ${item.specification}`,
            }));
          },
        },
      },
      // {
      //   label: t('检验次数'),
      //   field: 'checkNum',
      //   required: true,
      //   component: 'Select',
      //   componentProps: {
      //     options: checkNumDict,
      //   },
      // },
      {
        label: t('上传文件'),
        field: 'file',
        required: true,
        component: 'Upload',
        componentProps: () => {
          return {
            beforeUpload: (file: any) => {
              setFormModels({
                file: file ? [file] : [],
              });
              // formModel.file = file ? [file] : [];
              return false;
            },
            onRemove: (_file: any) => {
              setFormModels({
                file: [],
              });
            },
          };
        },
        componentSlots: {
          default: () => <Button type='primary'>{t('上传文件')}</Button>,
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
