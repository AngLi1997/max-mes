import { materialUseFileUpload } from '@/services';
import { UploadOutlined } from '@ant-design/icons-vue';
import { FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
import { Button, UploadProps, message } from 'ant-design-vue';

export const useForm = () => {
  const { passResultDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const customRequest: UploadProps['customRequest'] = (options: any) => {
    const formData = new FormData();
    formData.append('file', options.file);
    materialUseFileUpload(formData)
      .then((res: any) => {
        options.onSuccess(res.data as any);
      })
      .catch((error: any) => {
        error.message && message.error(error.message);
        options.onError(error);
      });
  };

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('入库单号'),
        field: 'inWarehouseNo',
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('物料编号'),
        field: 'materialNo',
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('物料名称'),
        field: 'materialName',
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('供应商'),
        field: 'supplierName',
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('入库日期'),
        field: 'inWarehouseDate',
        component: 'Input',
        componentProps: {
          disabled: true,
        },
      },
      {
        label: t('上传文件'),
        field: 'fileList',
        component: 'Upload',
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            accept: '.png, .jpg, .jpeg, .gif, .bmp, .pdf, .xlsx, .xls, .xlsm, .doc, .docx, .txt, .ppt, .pptx, .tf',
            maxCount: 5,
            customRequest,
            disabled: formModel.fileList && formModel.fileList.length >= 5,
            onChange: (info: any) => {
              if (info.file.status === 'done') {
                formModel.fileList = info.fileList.map((item: any) => {
                  return {
                    ...item,
                    name: item.uid === info.file.uid ? `${item.name}(${item.response.fileSize})` : item.name,
                  };
                });
              }
            },
          };
        },
        componentSlots: {
          default: ({ formModel }: RenderCallbackParams) => (
            <Button disabled={formModel.fileList && formModel.fileList.length >= 5} type='default'>
              <UploadOutlined></UploadOutlined>
              {t('选择文件')}
            </Button>
          ),
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              message: t('请上传文件'),
              trigger: 'blur',
              validator: () => {
                if (!formModel.fileList || formModel.fileList.length === 0) {
                  return Promise.reject(t('请上传文件'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        label: t('放行结果'),
        field: 'passResult',
        required: true,
        component: 'Select',
        componentProps: {
          options: passResultDict,
        },
      },
      {
        label: t('备注'),
        field: 'passRemark',
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
