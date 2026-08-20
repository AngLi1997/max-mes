import type { FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
export const useModalForm = () => {
  const { outTypeDict, qualityStatusDict, warehouseDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const outTypeMap = reactive({
    1: [4],
    2: [1],
    3: [1, 4],
  });

  const formProps = reactive<FormProps>({
    initialValues: {},
    labelWidth: 140,
    schemas: [
      {
        label: t('质量状态'),
        field: 'qualityStatus',
        required: true,
        component: 'Select',
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            options: qualityStatusDict,
            onChange: (val: 1 | 2 | 3) => {
              setFormModels({
                outType: null,
              });
              if (!val) {
                formInstance?.updateSchema({
                  field: 'type',
                  componentProps: {
                    options: [],
                  },
                });
                return;
              }
              formInstance?.updateSchema({
                field: 'type',
                componentProps: {
                  options: outTypeDict.filter((item: any) => !outTypeMap[val].includes(item.value)),
                },
              });
            },
          };
        },
      },
      {
        label: t('出库类别'),
        field: 'type',
        required: true,
        component: 'Select',
        componentProps: {
          options: [],
        },
      },
      {
        label: t('出库仓库'),
        field: 'warehouseId',
        vIf: getWarehouseConfigByCode.value,
        required: true,
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
      // {
      //   label: t('出库批号'),
      //   field: 'batchNo',
      //   required: true,
      //   component: ({ formModel }: RenderCallbackParams) => {
      //     return (
      //       <>
      //         <Input v-model:value={formModel.batchNo} style={{ width: '90%' }}>
      //           {{
      //             addonBefore: () => (
      //               <Select v-model:value={formModel.addonBefore} options={batchNoType} style={{ width: '70px' }} />
      //             ),
      //           }}
      //         </Input>
      //         <Tooltip style={{ width: '120px' }} trigger='hover'>
      //           {{
      //             title: () => (
      //               <>
      //                 <span>{t('首字母+年份+月份+流水号')}：</span>
      //                 <br />
      //                 <span>{t('T202301001，代表2023年 01月001批流水号，投料的出库计划')}</span>
      //               </>
      //             ),
      //             default: () => <QuestionCircleOutlined style={{ marginLeft: '8px', height: '36px' }} />,
      //           }}
      //         </Tooltip>
      //       </>
      //     );
      //   },
      // },
      {
        label: t('出库日期'),
        field: 'outPlanDate',
        required: true,
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
          picker: 'date',
        },
      },
      {
        label: t('出库单血浆类型'),
        field: 'deliveryPlasmaType',
        required: true,
        component: 'Input',
        componentProps: {
          maxlength: 30,
          showCount: true,
        },
      },
      {
        label: t('备注'),
        field: 'remark',
        component: 'InputTextArea',
        useMaxLengthRule: false,
        componentProps: {
          maxlength: 300,
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
