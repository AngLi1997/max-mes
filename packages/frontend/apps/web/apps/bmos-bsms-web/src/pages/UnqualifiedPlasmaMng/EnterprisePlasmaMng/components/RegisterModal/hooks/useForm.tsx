import { usePlasmaStation } from '@/stores/plasmaStation';
import { FormProps, ModalFormInstance } from '@bmos/components';

const { getPlasmaStations } = usePlasmaStation();

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 140,
    schemas: [
      {
        label: t('来源单位'),
        field: 'batchLog',
        required: true,
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
      {
        label: t('拒绝日期'),
        field: 'subBoxLog',
        required: true,
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('献浆者编号'),
        field: 'titerDown',
        component: 'Input',
      },
      {
        label: t('检测类型'),
        field: 'titerUp',
        component: 'Select',
        componentProps: {
          options: [
            {
              label: '类型1',
              value: 1,
            },
          ],
        },
      },
      {
        label: t('血浆编号'),
        field: 'sortingType',
        required: true,
        component: 'Input',
      },
      {
        label: t('不合格项目'),
        field: 'typeDescribe',
        required: true,
        component: 'Select',
        componentProps: {
          options: [
            {
              label: '项目1',
              value: 1,
            },
            {
              label: '项目2',
              value: 2,
            },
          ],
          mode: 'multiple',
        },
      },
      {
        label: t('核酸检测'),
        field: 'useFlag',
        required: true,
        component: 'Select',
        componentProps: {
          options: [
            {
              label: '核酸检测',
              value: 1,
            },
          ],
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
