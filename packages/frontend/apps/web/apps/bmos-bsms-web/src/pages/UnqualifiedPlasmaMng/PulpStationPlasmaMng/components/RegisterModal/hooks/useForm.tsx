import { usePlasmaStation } from '@/stores/plasmaStation';
import { FormProps, ModalFormInstance } from '@bmos/components';

const { getPlasmaStations } = usePlasmaStation();

export const useForm = () => {
  const { inspectTypeDict, nucleicAcidFlagDict, unqualifiedProjectDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    initialValues: {
      inspectType: 2,
      nucleicAcidFlag: 0,
    },
    labelWidth: 140,
    schemas: [
      {
        label: t('来源单位'),
        field: 'originOrgCode',
        required: true,
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
      {
        label: t('拒绝日期'),
        field: 'rejectDate',
        required: true,
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
          disabledDate: (current: any) => {
            return current && current > Date.now();
          },
        },
      },
      {
        label: t('献浆者编号'),
        field: 'plasmaDonorNo',
        required: true,
        component: 'Input',
      },
      {
        label: t('检测类型'),
        field: 'inspectType',
        required: true,
        component: 'Select',
        componentProps: () => {
          return {
            options: inspectTypeDict,
            onChange: (val: any) => {
              // const no = formModel.sampleNo || formModel.plasmaNo;
              updateSchema({
                field: 'no',
                label: val !== 1 ? t('血浆编号') : t('标本编号'),
              });
            },
          };
        },
      },
      {
        label: t('血浆编号'),
        field: 'no',
        required: true,
        component: 'Input',
      },
      {
        label: t('不合格项目'),
        field: 'unqualifiedItems',
        required: true,
        component: 'Select',
        componentProps: {
          options: unqualifiedProjectDict.filter((item: any) => item.value > 1 && item.value < 8),
          mode: 'multiple',
        },
      },
      {
        label: t('核酸检测'),
        field: 'nucleicAcidFlag',
        required: true,
        component: 'Select',
        componentProps: {
          options: nucleicAcidFlagDict,
        },
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
