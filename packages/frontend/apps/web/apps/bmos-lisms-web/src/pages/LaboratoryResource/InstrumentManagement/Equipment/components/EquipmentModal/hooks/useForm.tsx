import { useDict } from '@/stores/dictStore';
import { yesOrNoEnum } from '@/types';
import { FormProps, ModalFormInstance } from '@bmos/components';

const { getDict } = useDict();

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  const { InspectionProjectDict } = getDicts();

  const formProps = reactive<FormProps>({
    initialValues: {
      active: yesOrNoEnum.YES,
    },
    schemas: [
      {
        label: t('设备名称'),
        field: 'instrumentName',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('设备型号'),
        field: 'model',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('设备编号'),
        field: 'instrumentNo',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('设备类型'),
        field: 'type',
        required: true,
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('设备类型');
          },
        },
      },
      {
        label: t('设备厂家'),
        field: 'manufacturer',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('检验项目'),
        field: 'inspectionItems',
        required: true,
        component: 'Select',
        componentProps: {
          // 多选
          mode: 'multiple',
          options: InspectionProjectDict,
        },
      },
      {
        label: t('点检日期'),
        field: 'spotCheckedDate',
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('负责人'),
        field: 'principal',
        component: 'Input',
        componentProps: {
          maxlength: 10,
          showCount: true,
        },
      },
      {
        label: t('联系方式'),
        field: 'phone',
        component: 'Input',
        componentProps: {
          maxlength: 20,
          showCount: true,
        },
        // dynamicRules: () => {
        //   return [
        //     {
        //       pattern:
        //         /^0\d{2,3}[\- ]?[1-9]\d{6,7}|[48]00[\- ]?[1-9]\d{2}[\- ]?\d{4}|(010|02\d|0[3-9]\d{2})-?(\d{6,8})|(?:0|86|\+86)?1[3-9]\d{9}|^$/,
        //       message: t('请输入正确的联系方式'),
        //     },
        //   ];
        // },
      },
      {
        label: t('启用'),
        field: 'active',
        component: 'Switch',
        componentProps: {
          checkedValue: yesOrNoEnum.YES,
          unCheckedValue: yesOrNoEnum.NO,
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
