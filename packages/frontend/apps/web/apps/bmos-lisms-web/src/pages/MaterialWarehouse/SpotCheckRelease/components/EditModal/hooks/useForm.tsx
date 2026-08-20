import { materialUseFileUpload } from '@/services';
import { UploadOutlined } from '@ant-design/icons-vue';
import { FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
import { Button, UploadProps, message } from 'ant-design-vue';

export const useForm = (changeFile: (info: any) => void) => {
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
        label: t('上传文件'),
        field: 'fileList',
        component: 'Upload',
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            accept: '.png, .jpg, .jpeg, .gif, .bmp, .pdf, .xlsx, .xls, .xlsm, .doc, .docx, .txt, .ppt, .pptx, .tf',
            maxCount: 5,
            customRequest,
            showUploadList: false,
            onChange: (info: any) => changeFile(info),
            disabled: formModel.fileList && formModel.fileList.length >= 5,
          };
        },
        componentSlots: {
          default: ({ formModel }: RenderCallbackParams) => {
            return (
              <Button disabled={formModel.fileList && formModel.fileList.length >= 5} type='default'>
                <UploadOutlined></UploadOutlined>
                {t('选择文件')}
              </Button>
            );
          },
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
